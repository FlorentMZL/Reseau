#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <time.h>
#include <fcntl.h>
#include <inttypes.h>
#include <sys/types.h>
#define MAX_NAME 10;

int port = 4242;
int main(int argc, char **argv) {

    struct sockaddr_in adress_sock;
    adress_sock.sin_family = AF_INET;
    adress_sock.sin_port = htons(port);
    inet_aton("127.0.0.1",&adress_sock.sin_addr);
    int descr=socket(PF_INET,SOCK_STREAM,0);
    if (descr<0){
      perror("socket");
      exit(1);
    }
    
    int r=connect(descr,(struct sockaddr *)&adress_sock,sizeof(struct sockaddr_in));
    if (r == -1){
      perror("connexion");
      exit(1);
    }
    
  
    uint16_t a;
    char intbuf [2];

    int recu = recv(descr, &a, sizeof(uint16_t), 0);

    
    printf( "%" PRIu16, a);
   

  return 0;
}
