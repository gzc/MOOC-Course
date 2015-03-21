sample: sample.o oracle.o
	gcc -o sample sample.o oracle.o

vrfy:   vrfy.o cbcmac.o aes_core.o
	gcc -0 vrfy vrfy.o cbcmac.o aes_core.o -lpthread

mac:    mac.o cbcmac.o aes_core.o
	gcc -o mac mac.o cbcmac.o aes_core.o -lpthread

vrfy.o: vrfy.c cbcmac.h
	gcc -c vrfy.c

mac.o:  mac.c cbcmac.h
	gcc -c mac.c

cbcmac.o: cbcmac.c cbcmac.h
	gcc -c cbcmac.c

aes_core.o: aes_core.c
	gcc -c aes_core.c

sample.o: sample.c oracle.h
	gcc -c -g sample.c

oracle.o: oracle.c oracle.h
	gcc -c oracle.c
