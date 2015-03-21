#include "aes.h"
#include <stdio.h>
#include <stdlib.h>
#include "cbcmac.h"

int cbcmac(unsigned char *message, int length, unsigned char *tag){
    int i;
    int rounded_length = ROUND_TO_BLOCK(length);
    int curr_block = 0, num_blocks = (rounded_length>>4);
    unsigned char mblock[rounded_length], last_tag[16], cblock_curr[16];
    unsigned char key[16] = {
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    }; // the actual key has been replaced with the 0-key
    AES_KEY AESkey;

    AES_set_encrypt_key((const unsigned char *)key, 128, &AESkey);


    for (i=0; i<length; i++) {
        mblock[i] = message[i];
    }

    // Pad with zeros to fill block
    for (i=length; i<rounded_length; i++) {
        mblock[i] = 0x00;
    }

    // Set IV to null string
    for (i=0; i<16; i++) {
        last_tag[i] = 0x00;
    }

    while (curr_block < num_blocks) {
        for(i=0; i<16; i++) {
            cblock_curr[i] = mblock[(curr_block*16)+i] ^ last_tag[i];
        }

        AES_encrypt((const unsigned char*) cblock_curr, last_tag, (const AES_KEY *) &AESkey);

        curr_block++;
    }

    for(i=0; i<16; i++) {
        tag[i] = last_tag[i];
    }

    return 0;
}
