sample : main.o JPEG.o HUFFMAN.o
	g++ -o sample main.o HUFFMAN.o JPEG.o `pkg-config opencv --cflags --libs`
main.o : main.cpp JPEG.h
	g++ -c main.cpp
JPEG.o : JPEG.h JPEG.cpp HUFFMAN.h
	g++ -c JPEG.cpp
HUFFMAN.o : HUFFMAN.h HUFFMAN.cpp
	g++ -c HUFFMAN.cpp

clean :
	rm sample main.o JPEG.o HUFFMAN.o
