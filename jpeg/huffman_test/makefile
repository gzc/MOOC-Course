test_huffman : huffman_test_client.o HUFFMAN.o
		g++ -o test_huffman huffman_test_client.o HUFFMAN.o 
huffman_test_client.o : huffman_test_client.cpp ../HUFFMAN.h
		g++ -c huffman_test_client.cpp
HUFFMAN.o : ../HUFFMAN.h ../HUFFMAN.cpp
		g++ -c -DDEBUG=1 ../HUFFMAN.cpp

clean :
		rm test_huffman huffman_test_client.o HUFFMAN.o
