#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include "cbcmac.h"
#include <pthread.h>
#include <time.h>
#include <syscall.h>

#define MAX_LENGTH 512
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
 * Receives a message and a (purported) tag,
 * and returns 1 if verification succeeds and 0 if it fails.
 * For convenience, it returns -1 if there is some other error
 *
 */
void * connection_handler(void *sd) {
    unsigned char buf[BUF_SIZE], message[MAX_LENGTH];
    unsigned char tag[16];
    unsigned char test_tag[16];
    char response[2];
    int mlength, match, i;
    int sock = *(int *) sd;
    pthread_t my_id = pthread_self();

    printf("[%s] New thread (tid=%lu) spawned successfully.\n", get_time(), syscall(SYS_gettid));
    fflush(stdout);

    bzero(&buf, BUF_SIZE);
    bzero(&message, MAX_LENGTH);

    // Continue reading from client until they terminate the connection
    // contents of buf should  be of form
    //    < mlength (1) | msg (mlength) | tag (16) | 0 >
    while (read(sock,buf,BUF_SIZE)) {
      bzero(&tag, 16);

      mlength = (int) buf[0];

      if (mlength > 0 && mlength <= MAX_LENGTH) {
        match = 1;

        memcpy(message, buf+1, mlength);
        memcpy(test_tag, buf+1+mlength, 16);

        // Compute what tag should be
        cbcmac(message, mlength, tag);

        // Verify that query tag matches computed tag
        for(i=0; i<16; i++) {
          if (tag[i] != test_tag[i]) {
            match = 0;
            break;
          }
        }
      } else {
        // error code
        match = -1;
      }

      response[1] = '\0';
      sprintf(response, "%d", match);
      write(sock,response,2);

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
