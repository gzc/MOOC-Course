#include <stdio.h>

int main(int argc, char *argv[]){
  FILE *fpIn;
  FILE *fpOut;
  char c1, c2;

  if (argc != 3) {
    printf("Usage: hex2ascii <infile> <outfile>\n");
    return 1;
  }

  fpIn = fopen(argv[1], "r");
  fpOut = fopen(argv[2], "w");

  while (1) {
    if (fscanf(fpIn, "%c", &c1)==EOF) break;
    fscanf(fpIn, "%c", &c2);
    if ( (c1 >= 48) && (c1 <= 57) ) c1-=48;
    if ( (c2 >= 48) && (c2 <= 57) ) c2-=48;
    if ( (c1 >= 65) && (c1 <= 70) ) c1-=55;
    if ( (c2 >= 65) && (c2 <= 70) ) c2-=55;
    fprintf(fpOut, "%c", 16*c1+c2);
  }

  fclose(fpIn);
  fclose(fpOut);

  return 0;
}
