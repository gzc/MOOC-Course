/*************************************************************************
	> File Name: HUFFMAN.h
	> Author: 
	> Mail: 
	> Created Time: Mon 12 Jan 2015 11:20:02 PM CST
 ************************************************************************/

#include <queue>
#include <algorithm>
#include <fstream>
#include <map>

using namespace std;

#ifndef _HUFFMAN_H
#define _HUFFMAN_H

class HUFFMAN {
    typedef map<char, vector<bool> > huffman_dict;

    struct Htree {
        Htree *left;
        Htree *right;
        int weight;
        Htree() {left = right = NULL; weight = 0;}
        Htree(Htree *l, Htree *r, int v) {left = l; right = r; weight = v;}
    
        bool operator < (const Htree* tree) const {  
            return weight > tree->weight;    
        }
    };


    Htree *huffman_Tree;
    huffman_dict dict;

    void buildTree(map<char, double> probabilities);

public:
    
    void encode(string filename);
    void decode(string filename);
};

#endif
