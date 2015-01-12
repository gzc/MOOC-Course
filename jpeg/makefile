sample : main.o JPEG.o
	g++ -o sample main.o JPEG.o `pkg-config opencv --cflags --libs`
main.o : main.cpp JPEG.h
	g++ -c main.cpp
JPEG.o : JPEG.h JPEG.cpp
	g++ -c JPEG.cpp

clean :
	rm sample main.o JPEG.o
