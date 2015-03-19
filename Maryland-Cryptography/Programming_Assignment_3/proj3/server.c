/**
 * Server that decrypts incoming ciphertexts
 *
 * Clients send messages as an unsigned char array of
 * the form < CIPHERTEXT, BLOCKLEN, NULL-BYTE >,
 * where CIPHERTEXT contains BLOCKLEN blocks (and so contains 16*BLOCKLEN bytes),
 * BLOCKLEN is a byte indicating the number of blocks in the CIPHERTEXT,
 * and NULL-BYTE is a null byte.
 * The server returns a result indicating whether or not the ciphertext decrypted correctly,
 * using the decryption code from cbc-decrypt.c
 *
 * Server spawns new threads to handle multiple clients simultaneously
 *
 */

#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include "cbc-decrypt.h"
#include <pthread.h>
#include <time.h>
#include <syscall.h>

#define CTEXT_LEN 48
#define BUF_SIZE 4096
#define BACKLOG 5

void * connection_handler(void *sd);

char * get_time() {
  time_t curr_time;
  curr_time = time(0);
  char *p = ctime(&curr_time);
  p[strlen(p)-1] = 0;
  return p;
}

int main(int argc, char **argv) {
  int listenfd, sd;
  struct sockaddr_in incoming_addr;
  struct sockaddr_storage client_addr;
  socklen_t client_addr_len;
  char ipstr[INET_ADDRSTRLEN];
  pthread_t thread_id;

  if (argc < 2) {
    printf("Usage: ./server <port>\n");
    exit(-1);
  }

  // Setup socket
  if ((listenfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
    perror("Could not create socket.\n");
    exit(errno);
  }

  // Clear addr struct
  bzero(&incoming_addr, sizeof(incoming_addr));

  // Setup socket ip, port (given by first command line arg), and type
  incoming_addr.sin_family = AF_INET;
  incoming_addr.sin_port = htons(atoi(argv[1]));
  incoming_addr.sin_addr.s_addr = htonl(INADDR_ANY);

  // Bind to port
  if (bind(listenfd, (struct sockaddr *)&incoming_addr, sizeof(incoming_addr))) {
    perror("Could not bind to port.\n");
    exit(errno);
  }

  // Begin listening on port
  if (listen(listenfd, BACKLOG) != 0) {
    perror("Error calling listen.\n");
    exit(errno);
  }

 while (1) {
    if ((sd = accept(listenfd, NULL, NULL)) < 0) {
      perror("Error accepting connection.\n");
      exit(errno);
    }
    client_addr_len = sizeof(client_addr);
    getpeername(sd, (struct sockaddr *) &client_addr, &client_addr_len);
    struct sockaddr_in *s = (struct sockaddr_in *) &client_addr;
    inet_ntop(AF_INET, &s->sin_addr, ipstr, sizeof(ipstr));
    printf("[%s] Recieved connection from %s:%d\n", get_time(), ipstr, ntohs(s->sin_port));
    fflush(stdout);

    if (pthread_create(&thread_id, NULL, connection_handler, (void*) &sd) < 0) {
        perror("Could not create thread.\n");
        exit(errno);
    }
    pthread_detach(thread_id); // Ensures no memory leak since parent never waits on child threads
  }
}

/*
 * Used by each thread to handle connection to a client
 *
 * Decrypts ciphertexts from a client, returning 1
 * if the padding is correct, 0 if it is incorrect,
 * or -1 if the packet was malformed.
 *
 */
void * connection_handler(void *sd) {
    unsigned char buf[BUF_SIZE], ctext[CTEXT_LEN];
    char response[2];
    int block_len, rc;
    int sock = *(int *) sd;
    pthread_t my_id = pthread_self();

    printf("[%s] New thread (tid=%lu) spawned successfully.\n", get_time(), syscall(SYS_gettid));
    fflush(stdout);

    bzero(&buf, BUF_SIZE);

    // Continue reading from client until they terminate the connection
    // buf will be of form < Ciphertext(48) | Blocklen(1) | Null-terminator(1) >
    while (read(sock,buf,BUF_SIZE)) {

      block_len = (int) buf[0];

//[DEBUG INCOMING MSGS]
//int i; for(i=0;i<(block_len*16+2);i++) { printf("%02x|", buf[i]); } printf("\n");

      if (block_len < 1 || block_len > 3 || buf[(block_len*16)+1] != '\0') {
        printf("Malformed Packet! (block_len=%d)\n", block_len);
        rc = -1;
      } else {
        memcpy(ctext, buf+1, (block_len*16));
 	rc = cbcdec(ctext, block_len);
      }

      response[1] = '\0';
      sprintf(response, "%d", rc);
      write(sock,response,2);

      // clear buffer for next read
      bzero(&buf, BUF_SIZE);
    }
    printf("[%s] Client disconnected. Shutting down thread (tid=%lu)\n", get_time(), syscall(SYS_gettid));
    fflush(stdout);

    if (close(sock) < 0) {
      perror("Error closing socket.\n");
      exit(errno);
    }

    pthread_exit(0);
    return 0;
}
