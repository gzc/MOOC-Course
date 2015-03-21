// Assumes block length of 16
#define ROUND_TO_BLOCK(n) (n%16==0?n:(((n>>4)+1)<<4))

int cbcmac(unsigned char *message, int length, unsigned char *tag);
