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
    char bufScan [20];
    int nombreparties = recevoirNbGmes(descr);
   
    printf("Il y a %d parties en attente\n",nombreparties );

    afficherparties(descr, nombreparties);
    
    while (start ==0){

      afficherUsage();
      int lireEntree = fgets(bufScan, 20,stdin); 
      bufScan[strlen(bufScan)-1]='\0';//fgets lit les retours a la ligne donc on enleve le dernier caractere
     
       if (strlen(bufScan)==1){
        switch (bufScan[0]){
          case '2' :
          unreg(descr);
          case '1' : 
          send(descr, "GAME?***", 8,0);
          char hellorec[7];
          uint8_t a;
          char buff1[4];
          int recu = recv(descr,hellorec,6, 0 );
          hellorec[recu]='\0';
          int qs=recv(descr, &a,sizeof(uint8_t), 0);
          int qsda = recv(descr, buff1, 3, 0);
          printf("GAMES %" PRIu8 "\n" , a);
          afficherparties(descr, a);
           

        }
      }
      else {
        int longueur = strlen(bufScan);
        if (longueur <=5){
          int l = longueur - 2; 
          char intS [l+1] ;
          int j = 0;
          for(int i = 2; i<longueur && bufScan[i]!='\0';i++){
            intS[j]  = bufScan[i];
            j++;
          }
          intS[longueur] = '\0';
          int valeurInt = atoi(intS);
          if (valeurInt == 0){
            printf("erreur de syntaxe\n");
          }
          else if (bufScan[0]=='4'){  
            listeJoueurs(descr,valeurInt);
          } 
          else if (bufScan[0]=='3'){
            recevoirDim(descr, valeurInt);
          }
        }
        else{
          int longueur = strlen(bufScan);
          printf("%s\n",bufScan);
          int l = 8; 
          char intS [l+1] ;
          int j = 0;
          int alph = 1;//boolean
          for(int i = 2; i<10 && bufScan[i]!='\0';i++){
            if (!isalnum(bufScan[i])){
              alph = 0;
            }
            intS[j]  = bufScan[i];
            j++;
          }
          intS[j]='\0';
          if (alph==1){
            if (bufScan[0]=='5'){
              creatPartie(descr, intS);
            }         
            else if (bufScan[0] == '6'){
              j=0;
              char numPartie [3];
              for(int i = 10; i<longueur && bufScan[i]!='\0';i++){
            
            numPartie[j]  = bufScan[i];
            j++;
          }
          numPartie[j]='\0';
          int numpartieI = atoi(numPartie);
          if (numpartieI==0){
            printf("erreur de syntaxe");
          }
          
          joinPartie(descr, intS, numpartieI);
            }
          }

          else {
            printf("Caracteres non alphanumeriques\n");
          }
        }
      }
    }
  return 0;

}