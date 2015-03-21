#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>

#define NOFLAGS 0
#define BLOCK_LENGTH 16

extern int macfd, vrfyfd;

int Oracle_Connect() {
  struct sockaddr_in mac_servaddr, mac_cliaddr, vrfy_servaddr, vrfy_cliaddr;

  macfd = socket(AF_INET, SOCK_STREAM, 0);
  vrfyfd = socket(AF_INET, SOCK_STREAM, 0);

  bzero(&mac_servaddr, sizeof(mac_servaddr));
  bzero(&vrfy_servaddr, sizeof(vrfy_servaddr));

  mac_servaddr.sin_family = AF_INET;
  vrfy_servaddr.sin_family = AF_INET;
  mac_servaddr.sin_addr.s_addr=inet_addr("54.165.60.84");
  vrfy_servaddr.sin_addr.s_addr=inet_addr("54.165.60.84");
  mac_servaddr.sin_port=htons(81);
  vrfy_servaddr.sin_port=htons(82);

  if( !connect(macfd, (struct sockaddr *)&mac_servaddr, sizeof(mac_servaddr)) &&
      !connect(vrfyfd, (struct sockaddr *)&vrfy_servaddr, sizeof(vrfy_servaddr)) ) {
    printf("Connected to server successfully.\n");
    return 0;
  } else {
    perror("Failed to connect to oracle");
    return -1;
  }
}

int Oracle_Disconnect() {
  if(!close(macfd) && !close(vrfyfd)) {
    printf("Connection closed successfully.\n");
    return 0;
  } else {
    perror("[WARNING]: You haven't connected to the server yet");
    return -1;
  }
}

// < mlength (1) || message (mlength) || 0 >
int Mac(unsigned char *message, int mlength, unsigned char *tag) {
  unsigned char out[mlength+2];
  unsigned char in[16];
  int i;

  out[0] = mlength;
  memcpy((out+1), message, mlength);
  out[1+mlength] = '\0';

  if(!send(macfd, out, (mlength + 2), NOFLAGS)) {
    perror("[WARNING]: You haven't connected to the server yet");
    return -1;
  }
  if(!recv(macfd, in, 16, NOFLAGS)) {
    perror("[ERROR]: Recv failed");
    return -1;
  }
  for (i=0; i<16; i++) {
    tag[i] = in[i];
  }

  return 0;
}

int Vrfy(unsigned char *message, int mlength, unsigned char *tag) {
  unsigned char out[mlength+2+16];
  char in[2];
  int i;

  out[0] = mlength;
  memcpy((out+1), message, mlength);
  memcpy((out+1+mlength), tag, 16);
  out[1+mlength+16] = '\0';

  if(!send(vrfyfd, out, (mlength + 2 + 16), NOFLAGS)) {
    perror("[WARNING]: You haven't connected to the server yet");
    return -1;
  }
  if(!recv(vrfyfd, in, 2, NOFLAGS)) {
    perror("[ERROR]: Recv failed");
    return -1;
  }

  return atoi(in);
}
