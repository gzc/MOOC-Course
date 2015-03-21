#include "oracle.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#define MAX_LENGTH 512

int main(int argc, char *argv[]) {
    unsigned char message[MAX_LENGTH];
    unsigned char tag[16];
    int i, mlength, ret;
    FILE *fpIn;

    if (argc != 2) {
        printf("Usage: sample <filename>\n");
        return -1;
    }

    fpIn = fopen(argv[1], "r");
    for(i=0; i<MAX_LENGTH; i++) {
      if (fscanf(fpIn, "%c", &message[i]) == EOF) break;
    }
    fclose(fpIn);

    mlength = i;

    Oracle_Connect();

    // send message (of length mlength) to the Mac oracle to obtain tag
    Mac(message, mlength, tag);

    // send message (of length mlength) and tag to be verified
    ret = Vrfy(message, mlength, tag);

    if (ret==1) {
        printf("Message verified successfully!\n");
    } else {
        printf("Message verficiation failed.\n");
    }

    Oracle_Disconnect();
}
