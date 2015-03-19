#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>

#define NOFLAGS 0
#define BLOCK_LENGTH 16

extern int sockfd;

int Oracle_Connect() {
  struct sockaddr_in servaddr, cliaddr;
  int rc;

  sockfd = socket(AF_INET, SOCK_STREAM, 0);

  bzero(&servaddr, sizeof(servaddr));
  servaddr.sin_family = AF_INET;
  servaddr.sin_addr.s_addr=inet_addr("54.165.60.84");
  servaddr.sin_port=htons(80);

  if(!connect(sockfd, (struct sockaddr *)&servaddr, sizeof(servaddr))) {
    printf("Connected to server successfully.\n");
    return 0;
  } else {
    perror("Failed to connect to oracle");
    return -1;
  }
}

int Oracle_Disconnect() {
  if(!close(sockfd)) {
    printf("Connection closed successfully.\n");
    return 0;
  } else {
    perror("[WARNING]: You haven't connected to the server yet");
    return -1;
  }
}

// Packet Structure: < num_blocks(1) || ciphertext(16*num_blocks) || null-terminator(1) >
int Oracle_Send(unsigned char* ctext, int num_blocks) {
  int ctext_len = num_blocks * BLOCK_LENGTH;
  unsigned char message[(ctext_len)+2];
  char recvbit[2];

  message[0] = num_blocks;
  memcpy((message+1), ctext, ctext_len);
  message[ctext_len+1] = '\0';

  if(!send(sockfd, message, ctext_len+2, NOFLAGS)) {
    perror("[WARNING]: You haven't connected to the server yet");
    return -1;
  }
  if(!recv(sockfd, recvbit, 2, NOFLAGS)) {
    perror("[ERROR]: Recv failed");
    return -1;
  }
  recvbit[1] = '\0';

  return atoi(recvbit);
}
