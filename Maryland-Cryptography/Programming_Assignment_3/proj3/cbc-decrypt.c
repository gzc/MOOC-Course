
#include "aes.h"
#include <stdio.h>
#include <string.h>

/* The input ciphertext is assumed to have length an integer multiple
   of the blocklength.

   The code below is not robust to buffer-overflow attacks;
   exploiting these is not the purpose of this exercise */

int cbcdec(unsigned char* CText, int length){
  unsigned char MBlock[16];
  unsigned char CBlock_cur[16];
  unsigned char CBlock_prev[16];
  unsigned char Key[16];
  int i, j, tmp;
  FILE *fpOut;
  AES_KEY AESkey;

  // This is just for illustration; the actual key used was not the all-0 key!
  Key[0] = Key[1] = Key[2] = Key[3] = 0x00;
  Key[4] = Key[5] = Key[6] = Key[7] = 0x00;
  Key[8] = Key[9] = Key[10] = Key[11] = 0x00;
  Key[12] = Key[13] = Key[14] = Key[15] = 0x00;

  AES_set_decrypt_key((const unsigned char *) Key, 128, &AESkey);

  if (length < 2) return 0;

  for (i=0; i<16; i++)
    CBlock_prev[i] = CText[i];

  j = 1;

  while (j < length) {
    for (i=0; i<16; i++)
      CBlock_cur[i] = CText[16*j+i];

    AES_decrypt((const unsigned char *) CBlock_cur, MBlock, (const AES_KEY *) &AESkey);

    for (i=0; i<16; i++) {
      MBlock[i] ^= CBlock_prev[i];
      // fprintf(fpOut, "%X", MBlock[i]/16), fprintf(fpOut, "%X", MBlock[i]%16);
      // Uncomment this to output the message + the padding for debugging purposes.
      // If we were implementing this for real, we would only output the message
      CBlock_prev[i] = CBlock_cur[i];
    }
    j++;
  }

  j = MBlock[15];
  if ((j==0) || (j>16)) {
   // printf("Error: final byte out of range\n");
    return 0;
  }
  for (i=14; i>=16-j; i--) {
    if (MBlock[i] != j) {
      // printf("Error: incorrect padding\n");
      return 0;
    }
  }

  // printf("Everything fine\n");
  return 1;

}
