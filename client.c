#include "ast.h"


int port = 4242;
char ip[50];
int main(int argc, char **argv) {
    if (argc==2){
      port = atoi(argv[1]);
      char *ipR = "127.0.0.1";
      strcpy(ip, ipR);
    }
    else if (argc==3){
      port = atoi(argv[1]);
      char ipR [strlen(argv[2])-1] ;
      strcpy(ipR, argv[2]);
      ipR[strlen(argv[2])]='\0';
      strcpy(ip, ipR);
    }
    else{
      strcpy(ip, "127.0.0.1");
    }

    struct sockaddr_in adress_sock;
    adress_sock.sin_family = AF_INET;
    adress_sock.sin_port = htons(port);
    inet_aton(ip,&adress_sock.sin_addr);
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
    uint8_t nombreparties = recevoirNbGmes(descr);
    int joined = 0;//Savoir si le joueur a rejoint une partie ou pas
    printf("Il y a % "PRIu8 "d parties en attente\n",nombreparties );

    afficherparties(descr, nombreparties);
    
    while (start ==0){

      afficherUsage();
      fgets(bufScan, 20,stdin); 
      bufScan[strlen(bufScan)-1]='\0';//fgets lit les retours a la ligne donc on enleve le dernier caractere
     
       if (strlen(bufScan)==1){
        switch (bufScan[0]){
          case '2' :
            unreg(descr);
            break;
          case '1' : 
            send(descr, "GAME?***", 8,0);
            char hellorec[7];
            uint8_t a;
            char buff1[4];
            int recu = recv(descr,hellorec,6, 0 );
            hellorec[recu]='\0';
            recv(descr, &a,sizeof(uint8_t), 0);
            recv(descr, buff1, 3, 0);
            printf("GAMES %" PRIu8 "\n" , a);
            afficherparties(descr, a);
            break;
          case'7' : 
            if (joined ==0){
              printf("vous n'avez pas rejoint de partie\n");

            }
            else {
              sendStart(descr);
              printf("en attente du démarrage de la partie.\n");
              recupInfosStart(descr);
            }
            break;
          case '9':
            send(descr, "TEAMS***", 8,0);
            char recteam[8];
            recv(descr, recteam, 8,0);
            if (recteam[4]=='!'){
              printf("Partie par équipes !\n");
            }
            else{
              printf("Pas d'équipes!\n");
            }
            break;
        }
      }
      else {
        int longueur = strlen(bufScan);
        if (longueur <=5){
          
          int l = longueur - 2; 
          char *intS = malloc(l+1);
          intS = substr(bufScan, 2,longueur);
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
          else if (bufScan[0]=='8'){
            setFantomes(descr, valeurInt);
          }
          
        
        }
        else{
          int longueur = strlen(bufScan);
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
          char port[5];
          j = 0; 
          for (int i = 11; i<15&&bufScan[i]!='\0';i++){
              port[j]=bufScan[i];
              j++;
            }
          port[j] = '\0';

          if (alph==1){
            if (bufScan[0]=='5'){
             joined= creatPartie(descr, intS, port);
            }         
            else if (bufScan[0] == '6'){
              j=0;
              char numPartie [3];
              for(int i = 15; i<longueur && bufScan[i]!='\0';i++){
            
            numPartie[j]  = bufScan[i];
            j++;
          }
          numPartie[j]='\0';
          int numpartieI = atoi(numPartie);
          if (numpartieI==0){
            printf("erreur de syntaxe\n");
          }
          
          joined = joinPartie(descr, intS,port, numpartieI);
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