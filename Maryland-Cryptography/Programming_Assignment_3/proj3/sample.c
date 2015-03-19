#include "oracle.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

// Read a ciphertext from a file, send it to the server, and get back a result.
// If you run this using the challenge ciphertext (which was generated correctly),
// you should get back a 1. If you modify bytes of the challenge ciphertext, you
// may get a different result...

// Note that this code assumes a 3-block ciphertext.

int main(int argc, char *argv[]) {
  unsigned char ctext[48]; // allocate space for 48 bytes, i.e., 3 blocks
  unsigned char message[32];
  unsigned char backup[48];
  int i, tmp, ret;
  FILE *fpIn;

  if (argc != 2) {
    printf("Usage: sample <filename>\n");
    return -1;
  }

  fpIn = fopen(argv[1], "r");

  for(i=0; i<48; i++) {
    fscanf(fpIn, "%02x", &tmp);
    ctext[i] = tmp;
    backup[i] = tmp;
  }

  fclose(fpIn);

  Oracle_Connect(); /* Note: this only needs to be called
		       ** once **, at the beginning of your program;
		       you can then use Oracle_Send as many times
		       as you like, and end by calling Oracle_Disconnect once */

  int index = 0;
  
  /*
  while(1) {
    unsigned char c = ctext[index];
  	ctext[index] = 0;
  	ret = Oracle_Send(ctext, 3); 
  	ctext[index]  = c;
  	if(ret == 0) {
  		break;
  	}
  	index++;
  }*/
  
  index = 21;
  int padding = 11;

  printf("index = %d\n", index);
  
  /*
  for(i = 20; i >= 16; --i) {
  
  	printf("i : %d\n", i);
  
  	for(int j = i+1;j < 32;j++) {
  		ctext[j] = ctext[j] ^ (unsigned char)(padding) ^ (unsigned char)(padding+1);
  	}
  	
  	if(ctext[i] != backup[i]) printf("ERROR\n");
  	unsigned char ch = ctext[i];
  	int K;
  	for(int k = 0;k < 256;++k) {
  		ctext[i] = (unsigned char)k;
  		ret = Oracle_Send(ctext, 3);
  		if(ret == 1) {
  			K = k;
  			message[i] = (unsigned char)k ^ (unsigned char)(padding+1) ^ ch;
  			break;
  		}
  	}
  	padding ++;
  	printf("k : %d  char : %c \n", K, message[i]);
  }*/
  padding = 0;
  for(i = 15; i >= 0; --i) {
  
  	printf("i : %d\n", i);
  
  	for(int j = i+1;j < 16;j++) {
  		ctext[j] = ctext[j] ^ (unsigned char)(padding) ^ (unsigned char)(padding+1);
  	}
  	
  	if(ctext[i] != backup[i]) printf("ERROR\n");
  	unsigned char ch = ctext[i];
  	int K;
  	for(int k = 0;k < 256;++k) {
  		ctext[i] = (unsigned char)k;
  		ret = Oracle_Send(ctext, 2);
  		if(ret == 1) {
  			K = k;
  			message[i] = (unsigned char)k ^ (unsigned char)(padding+1) ^ ch;
  			break;
  		}
  	}
  	padding ++;
  	printf("k : %d  char : %d \n", K, message[i]);
  }
  
  
  
  printf("plaintext : %s\n", message);
  
  Oracle_Disconnect();
}
