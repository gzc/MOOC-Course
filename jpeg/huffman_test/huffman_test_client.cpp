/*************************************************************************
	> File Name: huffman_test_client.cpp
	> Author: 
	> Mail: 
	> Created Time: Tue 13 Jan 2015 07:59:40 PM CST
 ************************************************************************/

#include<iostream>
#include "../HUFFMAN.h"
using namespace std;

int main()
{
    string data;
    cin >> data;

    string filename = "compressed";
    HUFFMAN huffman;
    huffman.encode(data, filename);

    return 0;
}
