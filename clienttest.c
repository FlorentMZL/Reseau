#include "ast.h"

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
    int start = 0; 
    char bufScan [10];
    int nombreparties = recevoirNbGmes(descr);
    printf("Il y a %d parties en attente\n",nombreparties );

    afficherparties(descr, nombreparties);
    
    while (start ==0){

      afficherUsage();
      int lireEntree = scanf("%s", bufScan); 
      if (lireEntree >5){
        printf("Mauvais format");
      }
      else if (lireEntree == 1){
        switch (bufScan[0]){
          case '1' :
          unreg(descr);
          case '2' : 
          send(descr, "GAME?***", 8,0);
          char hellorec[6];
          int a;
          char buff1[3];
          int recu = recv(descr,hellorec,6, 0 );
          hellorec[recu]='\0';
          int qs=recv(descr, &a,sizeof(uint8_t), 0);
          int qsda = recv(descr, buff1, 3, 0);
          printf("%d\n", a);
          buff1[qsda]='\0';
          printf("GAMES %d\n", a);
          afficherparties(descr, a);
           

        }
      }
      else if (lireEntree<=5){
        int longueur = strlen(bufScan);
        int l = longueur - 2; 
        char[] intS [l+1] ;
        for(int i = 2; i<longueur && bufScan[i]!='\0');i++){
          intS [i] = src[i];
        }
        intS[longueur] = '\0';
        int valeurInt = atoi(intS);
        if (valeurInt = 0){
          printf("erreur de syntaxe");
        }
      }

    }
  return 0;

}