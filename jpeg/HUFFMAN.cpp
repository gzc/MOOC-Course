/*************************************************************************
	> File Name: HUFFMAN.cpp
	> Author: 
	> Mail: 
	> Created Time: Mon 12 Jan 2015 11:48:32 PM CST
 ************************************************************************/

#include "HUFFMAN.h"
#include<iostream>
using namespace std;

HUFFMAN::HUFFMAN() {
}

HUFFMAN::~HUFFMAN() {
    destroy(huffman_Tree);
}

void HUFFMAN::buildTree(string &data, string filename) {
    
    map<char, int> frequency;
    for(string::iterator it = data.begin(); it != data.end(); it++) {
        frequency[*it]++;
    }

    #ifdef DEBUG   
        cout << "-------------------- Frequencies of data --------------------" << endl;
        for(map<char, int>::iterator it = frequency.begin(); it != frequency.end(); it++) {
            cout << it->first << "   " << it->second << endl;
        }
        cout << "-------------------- End of frequencies --------------------" << endl;
    #endif

    priority_queue<Htree *, vector<Htree *>, myComparision> pq;

    for(map<char, int>::iterator it = frequency.begin(); it != frequency.end(); it++) {
        Htree *node = new Htree(NULL, NULL, it->second, it->first);
        pq.push(node);
    }

    while(pq.size() > 1) {
        Htree *node1 = pq.top();
        pq.pop();
        Htree *node2 = pq.top();
        pq.pop();
        Htree *node = new Htree(node1, node2, node1->weight+node2->weight, 0);
        pq.push(node);
    }

    huffman_Tree = pq.top();
    vector<bool> code;
    buildDict(huffman_Tree, code);

    #ifdef DEBUG
        cout << "-------------------- Show Encoding Dict --------------------" << endl;

        for(huffman_dict::iterator it = dict.begin(); it != dict.end(); it++) {
            cout << it->first << "    ";
            vector<bool> code = it->second;
            for(vector<bool>::iterator codeit = code.begin(); codeit != code.end(); codeit++) {
                cout << *codeit;
            }
            cout << endl;
        }

        cout << "-------------------- End of Dict --------------------" << endl;
    #endif
}

void HUFFMAN::buildDict(Htree *node, vector<bool> &code) {
    if(isLeaf(node)) {
        dict[node->ch] = code;
    } else {
        code.push_back(false);
        buildDict(node->left, code);
        code.pop_back();
        code.push_back(true);
        buildDict(node->right, code);
        code.pop_back();
    }
}

void HUFFMAN::writeTrie(Htree *node) {
    if (isLeaf(node)) {
        BinaryStdOut.write(true);
        fout << node->ch;
        return;
                
    }
    BinaryStdOut.write(false);
    writeTrie(node->left);
    writeTrie(node->right);
}

void HUFFMAN::encode(string &data, string filename) {
    cout << "-------------------- Start Encoding File : " << filename << " --------------------" << endl;
    buildTree(data, filename);

    fout.open(filename.c_str());  
    if (!fout.is_open()) { 
        cout << "Error opening file"; 
        exit (-1); 
    }

    //first of all, write our huffman tree
    writeTrie(huffman_Tree);

    //Then, write our compressed data
    char c = 0;
    int index = 0;
    for(string::iterator it = data.begin(); it != data.end(); it++) {
        char ch = *it;
        vector<bool> code = dict[ch];
 
        for(vector<bool>::iterator codeit = code.begin(); codeit != code.end(); codeit++) {
            bool v = *codeit;
            c = c | (v << index);
            index++;
            //write this char to file, and reset
            if(index == 8) {
                index = 0;
                fout << c;
                c = 0;
            }

            #ifdef DEBUG
                cout << v;
            #endif
        }
    }
    if(index != 0) fout << c;

    fout.close();
    cout << endl << "-------------------- Finish Writing File --------------------" << endl;
}

void HUFFMAN::decode() {

}

/*
 * delete all the nodes
 * prevent memory leak
 */
void HUFFMAN::destroy(Htree *node) {
    if(node->left) destroy(node->left);
    if(node->right) destroy(node->right);
    delete node;
}

