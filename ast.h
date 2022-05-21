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
#include <ctype.h>
#include <pthread.h>
char* port_number = "4444";

typedef struct attributsPartie{
    char* adresseIp;
    int port;

}attributsPartie;


char* substr(char s [], int m, int n){
    int longueur = n-m;
    char *dest =malloc(longueur+1);
    int j = 0;
    for(int i = m; i<n && s[i]!='\0'; i++){
        dest[j] = s[i];
        j++;
    }
    dest[j]='\0';
    return dest;
}


int recevoirNbGmes (int descr){

    char hellorec[7];
    int a;
    char buff1[3];
    int recu = recv(descr,hellorec,6, 0 );
    hellorec[recu]='\0';
    recv(descr, &a,sizeof(uint8_t), 0);
     recv(descr, buff1, 3, 0);
    return a; 
}

void afficherparties(int descr, uint8_t nombre){
    char bufOGAME [6];
    uint8_t num; 
    uint8_t joueursInscrits;
    for(char i = 0; i<nombre; i++){
        recv(descr, bufOGAME, 6, 0);
        recv(descr, &num, sizeof(uint8_t), 0);
        recv(descr, bufOGAME, 1, 0);
        recv(descr, &joueursInscrits, sizeof(uint8_t), 0);
        recv(descr, bufOGAME, 3,0);
        printf ("Partie %" PRIu8 ": % " PRIu8 " joueurs inscrits.\n", num, joueursInscrits);
    }



}
void afficherUsage(){
    printf("Usage : Entrez le nombre correspondant à l'action souhaitée :\n\
    -1 : Lister les parties en attente \n\
    -2 : Se désinscrire de la partie sélectionnée\n\
    -3 -m : Demander la taille du labyrinthe d'une partie m \n\
    -4 -m : Demander la liste des joueurs inscrits à une partie m \n\
    -5 -pseudo -port : Créer une partie. pseudo de 8 caractères alphanumeriques\n\
    -6 -pseudo -port -m : rejoint la partie m avec un pseudo de 8 caractères alphanumeriques\n\
    -7 : \"start\" la partie. N'est utilisable que si vous etes dans une partie\n\
    -8 -m : Ajuster le nombre de fantomes à m. Seulement si vous avez créé la partie\n ");
    
}
int unreg(int descr){
    char repBuf[11];
    send(descr, "UNREG***",8,0 ); 
    int recu = recv(descr, repBuf, 10, 0 );
    repBuf[recu] = '\0';
    if (strlen(repBuf)==8){
        printf("Vous n'etes inscrit à aucune partie.\n");
        return 0;
    }
    else {
        printf("Vous avez été désinscrit.\n");
        return 0;
    }
}

void listeJoueurs(int descr, int num){
    char a = num;
    char buf[10];
    char *liststr ="LIST? ";
    
    strcpy(buf, liststr);
    buf[6] = a;
    buf[7] = '*';
    buf[8] = '*';
    buf[9]='*';
    char bufrec [13];
    send(descr, buf,10, 0);
    
    int recu = recv (descr, bufrec, 12, 0);
    bufrec[recu] = '\0';
    if (strlen(bufrec)<10){//On verifie si c'est un DUNNO ou pas
        printf("La partie n'existe pas, ou alors vous etes deja dans une partie\n");
    }
    else{
        int nombreJoueurs = bufrec[8];

        char idjoueur[9];
        for(int i=0;i<nombreJoueurs;i++){
            recu = recv(descr,idjoueur,6,0);
            recu = recv(descr, idjoueur,8,0);
            
            idjoueur[recu] = '\0';
            printf("Joueur %d  : %s\n", i+1, idjoueur);
            recu = recv(descr, idjoueur,3,0);
        }
    }

}
void recevoirDim( int descr, int num){
    char b = num;
    char *envoi = "SIZE? ";
    char bufenvoi [10];
    strcpy(bufenvoi, envoi);
    bufenvoi[6] = b;
    bufenvoi[7] = '*';
    bufenvoi[8] = '*';
    bufenvoi[9] = '*';
    send(descr, bufenvoi, 10,0);
    char bufrec[17];
    int recu = recv(descr, bufrec, 6,0);
    bufrec[recu] = '\0';
    if(bufrec[0] == 'D'){//On verifie si c'est un DUNNO
    printf("la partie n'existe pas, ou alors vous etes déjà dans une partie\n");
    recv(descr, bufrec, 2,0);
    }
    else {
        uint16_t longueur ;
        uint16_t largeur;
        recu = recv(descr, bufrec,2,0);//prendre tout d'un coup peut etre
        
        recu = recv(descr, &longueur, 2,0);
        recu = recv(descr, bufrec, 1,0);
        recu = recv(descr, &largeur, 2,0);
        printf("Les dimensions du labyrinthe sont : (%" PRIu16 ",%" PRIu16 ")\n", longueur, largeur);// a verifier.
        recu = recv(descr, bufrec, 3,0);

    }
}
int creatPartie(int descr, char id [], char port[]){
    port_number=port;
    char* envoi = "NEWPL ";
    char bufenvoi[22];
    strcpy(bufenvoi, envoi);
    strcat(bufenvoi, id);
    bufenvoi[14]=' ';
    bufenvoi[15] = '\0';
    strcat(bufenvoi,port_number );
    bufenvoi[21] = '*';
    bufenvoi[20] = '*';
    bufenvoi[19] = '*';
    send(descr, bufenvoi, 22, 0);
    char bufrec[11];
    int recu = recv(descr, bufrec, 10,0 );
    if (recu == 8){//On verifie si c'est un REGNO
        printf("erreur lors de la création de partie\n");
        return 0;
    }
    else {
        bufrec[recu] = '\0';
        int a = (int) bufrec[6];
        printf("Vous avez créé la partie de numéro %d\n", a);
        
        return 1;
    }
}
int joinPartie(int descr, char id[], char port[], int m){
    port_number = port;
    char *envoi = "REGIS ";
    char bufenvoi [24];
    strcpy(bufenvoi, envoi);
    strcat(bufenvoi, id);
    bufenvoi[14] = ' ';
    bufenvoi[15] = '\0';
    strcat(bufenvoi, port_number);
    bufenvoi[19] = ' ';
    bufenvoi[20] = m;
    bufenvoi[21] = '*';
    bufenvoi[22] = '*';
    bufenvoi[23] = '*';
    send(descr, bufenvoi, 24, 0);
    char bufrec[11];
    int recu = recv(descr, bufrec, 10,0 );
    if (recu == 8){//On verifie si c'est un REGNO
        printf("erreur de connexion a la partie\n");
        return 0;
    }
    else {
        bufrec[recu] = '\0';
        int a = (int) bufrec[6];
        printf("Vous avez rejoint la partie de numéro %d\n", a);
        return 1;
    }
}
void setFantomes(int descr, int n){
    char* envoi = "NBFAN ";
    char bufenvoi[10];
    strcpy(bufenvoi, envoi);
    char b = n;
    bufenvoi[6] = b;
    bufenvoi[9]='*';
    bufenvoi[8]='*';
    bufenvoi[7]='*';
    send(descr, bufenvoi,10,0);
    char bufrec[8];
    recv(descr, bufrec,8,0);
    if (bufrec[0]=='D'){
        printf("Erreur de modification des fantomes");
    }
    else{
        printf("Fantomes modifiés. ");
    }
}
void sendStart(int descr){
    send(descr, "START***", 8,0);
}
void *ecouteMulticast(void *arg){
    
    attributsPartie partie = *((attributsPartie*)arg);
    int sock = socket(PF_INET, SOCK_DGRAM,0);
    int ok = 1; 
    int r = setsockopt(sock, SOL_SOCKET,SO_REUSEPORT,&ok,sizeof(ok));
    if (r<0){
        printf("erreur setsockopt\n");
        exit(1);
    }
    printf("%d\n", partie.port);
    struct sockaddr_in address_sock;
    address_sock.sin_family = AF_INET;
    address_sock.sin_port=htons(partie.port);
    address_sock.sin_addr.s_addr = htonl(INADDR_ANY);
    r = bind(sock,(struct sockaddr *)&address_sock, sizeof (struct sockaddr_in));
    if (r<0){
        printf("erreur binding multicast\n");
        return NULL;
    }
    else {
    
        struct ip_mreq mreq;
        printf("%s\n", partie.adresseIp);
        mreq.imr_multiaddr.s_addr = inet_addr(partie.adresseIp);
        mreq.imr_interface.s_addr=htonl(INADDR_ANY);
        r=setsockopt(sock, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq));
        if (r<0){
            printf("erreur setsockopt multicast\n");
        }
        if (r==0){
        char tampon[100];
        
            while(1){
                int rec = recv(sock, tampon, 100, 0);
                tampon[rec] = '\0';
                if (tampon[0]=='M'&&strcmp(tampon+rec-3,"+++")==0){
                
                
                char *id = malloc(9);
                id = substr(tampon, 6,14);
                
                tampon[rec-3]='\0';
                    printf("Message recu de %s : %s \n",id,tampon+15);
                free(id);
                }
                else if (tampon[0]=='E'&&strcmp(tampon+rec-3, "+++")==0){
                    printf("Partie terminée.\n");
                    
                    char *id = malloc(9);
                    id = substr(tampon, 6,14);
                    tampon[rec-3]='\0';
                    char pointsG [5];
                    strcpy(pointsG, tampon+15);
                    int nbPointsG = atoi(pointsG);
                    
                    printf("Gagnant : %s, avec %d points.\n", id, nbPointsG);
                    free(id);
                }
                else if (tampon[0]=='S'&&strcmp(tampon+rec-3,"+++")==0){
                    char *id = malloc(9);
                    id = substr(tampon, 6,14);
                    char *pointsG = malloc(5);
                    char *xJ = malloc(4);
                    char *yJ= malloc(4);

                    pointsG=substr(tampon, 15,19);
                    xJ = substr(tampon,20,23 );
                    yJ = substr(tampon, 24,27);
                    int xjI = atoi(xJ);
                    int yJI=atoi(yJ);
                    printf("Le joueur %s a capturé un fantome en position (%d,%d)\n", id, xjI, yJI);
                    free(id);
                    free(pointsG);
                    free(xJ);
                    free(yJ);
                }
                else if (tampon[0]=='G'&& strcmp(tampon+rec-3,"+++")==0){
                    char *xJ = malloc(4);
                    char *yJ= malloc(4);
                    xJ = substr(tampon, 6,9);
                    yJ = substr(tampon, 10,13);
                    int xJI = atoi(xJ);
                    int yJI= atoi(yJ);
                    printf("Fantome en mouvement : (%d,%d)\n", xJI, yJI);
                }
    
            }
        }
    
    } 
    return NULL;
}
void *ecouteUDP(void *arg){
    int sock = socket(PF_INET, SOCK_DGRAM, 0);
    struct sockaddr_in address_sock;
    address_sock.sin_family= AF_INET;
    address_sock.sin_port= htons(atoi(port_number));
    address_sock.sin_addr.s_addr=htonl(INADDR_ANY);
    int r = bind(sock,(struct sockaddr *)&address_sock,sizeof(struct sockaddr_in));
    if (r<0){
        printf("erreur binding udp\n");
        exit(1);
    }
    if (r==0){
        char tampon [100];
        while(1){
             int rec = recv(sock, tampon, 100, 0);
                tampon[rec] = '\0';
                char* id = malloc(9);
                id= substr(tampon, 6,14);
               
                tampon[rec-3]='\0';
                printf("Message recu de %s : %s\n",id,tampon+15);
                free(id);
            }
          
        }
    
    return NULL;
}
void recevoirPos(int descr){
    char bufRecu[21];
        recv(descr,bufRecu, 6, 0);
        bufRecu[7] = '\0'; 
        if (bufRecu[0]=='G'){
            printf("partie finie\n");
        }
        else if (bufRecu[4]=='!'){
            char xpos[4];
            char ypos[4];
            int recx =recv(descr, xpos, 3, 0);
            recv(descr,bufRecu, 1, 0);//' '
            recv(descr, ypos, 3,0);
            xpos[recx] = '\0';
            ypos[recx] = '\0';
            int xnew = atoi(xpos);
            int ynew = atoi(ypos);
            recv(descr,bufRecu,3,0 );//"***"

            printf("Nouvelle position : ligne %d, colonne %d. \n", xnew, ynew);
        }
        else{
        char xpos [4];
        char ypos[4];
        int recx =recv(descr, xpos, 3, 0);
        recv(descr,bufRecu, 1, 0);//' '
        recv(descr, ypos, 3,0);
        xpos[recx] = '\0';
        ypos[recx] = '\0';
        int xnew = atoi(xpos);
        int ynew = atoi(ypos);
        recv(descr,bufRecu,1,0 );
        char nbPoints[5];
        int recpoints =recv(descr, nbPoints, 4,0);
        nbPoints[recpoints]='\0';
        int nbPts = atoi(nbPoints);
        recv(descr, bufRecu, 3,0);//"***"
        printf("Fantome attrapé! Nouvelle position : ligne %d, colonne %d. %d Points\n", xnew, ynew, nbPts);
    }
}
void glisCom(int descr){
    char bufRec[10];
    char identite[9];
    char posX[4];
    char posY[4];
    char nbF[5];
    int posXI;
    int posYI;
    int nbFI;
    send(descr, "GLIS?***", 8,0);
    recv(descr, bufRec, 6, 0);
    
    uint8_t nombreJoueurs;
    
    recv(descr, &nombreJoueurs, 1,0);
    recv(descr, bufRec,3,0);
   
    for(int i = 0; i<nombreJoueurs; i++){
        recv(descr,bufRec,6,0);
        int recu = recv(descr, identite,8,0);
        identite[recu]='\0';
        recv(descr,bufRec, 1,0);
        recu= recv(descr, posX, 3,0);
        posX[recu]='\0';
        recv(descr,bufRec, 1,0);
        recu =recv(descr, posY, 3,0);
        posY[recu]='\0';
        recv(descr,bufRec, 1,0);
        recu = recv(descr, nbF, 4,0);
        nbF[recu]='\0';
        posXI = atoi(posX);
        posYI = atoi(posY);
        nbFI = atoi(nbF);
        printf("Joueur %d : %s, ligne %d, colonne %d, nombre de points : %d.\n",i+1, identite, posXI, posYI, nbFI);
        recv(descr,bufRec, 3,0);
    }
}
void envoiDirection(char s[], int descr, char intS[]){
    char bufenvoi [12];
    bufenvoi[0] = '\0';
    strcat (bufenvoi, s);
    strcat(bufenvoi, intS);
    bufenvoi[11] = '*';
    bufenvoi[10] = '*';
    bufenvoi[9]='*';
    send(descr, bufenvoi, sizeof(bufenvoi), 0);
}


void recupInfosStart(int descr){
    char bufIP [16];
    char poubelle[10];
    char bufPORT[5];
    recv(descr,poubelle, 8,0);
    uint16_t hauteur;
    uint16_t largeur;
    uint8_t fantomes; 
    recv(descr, &hauteur, sizeof(uint16_t), 0);
    recv(descr, poubelle, 1, 0);
    recv(descr, &largeur, sizeof(uint16_t),0);
    recv(descr, poubelle, 1, 0);
    recv(descr, &fantomes, sizeof(uint8_t),0);
    recv(descr, poubelle, 1, 0);
    int recu = recv(descr, bufIP, 15, 0);
    bufIP[recu]='\0';
    for(int i = strlen(bufIP)-1; i>0;i=i-1){
        if (bufIP[i]!='#'){
            bufIP[i+1]='\0';
            break;
        }
    }
  
    recv(descr, poubelle,1,0);
    recu = recv(descr,bufPORT, 4, 0 );
    recv(descr, poubelle, 3,0);
    bufPORT[recu]='\0';
   
    int portInt = atoi(bufPORT);
    attributsPartie p = {
        .adresseIp = bufIP,
        .port = portInt
        
        };
    pthread_t thMulticast;
    pthread_t thUDP ;
    pthread_create(&thUDP, NULL, ecouteUDP, &port_number);
    pthread_create(&thMulticast, NULL, ecouteMulticast,&p );
    char position[20];
    recv(descr, position,15,0 );
    char coordX[4];
    char coordY[4];
    recu=recv(descr, coordX, 3, 0);
    coordX[recu] = '\0';
    recv(descr, poubelle, 1,0);
    recu = recv(descr, coordY, 3,0);
    coordY[recu] = '\0';
    recv(descr, poubelle, 3,0);
    int coordXInt = atoi(coordX);
    int coordYInt = atoi (coordY);
    printf("Taille du labyrinthe : %" PRIu16 "lignes, %" PRIu16 " colonnes.\n", hauteur, largeur);
    printf("Position de départ : ligne %d, colonne %d. Il y a %" PRIu8 " fantomes\n", coordXInt, coordYInt, fantomes);
    while(1==1){
    printf("\n**********\n");
    printf("Commandes : \n\
    z -n: se déplacer de n cases vers le haut\n\
    s -n : se déplacer de n cases vers le bas \n\
    q -n : se déplacer de n cases vers la gauche\n\
    d -n : se déplacer de n cases vers la droite\n\
    g : lister tous les joueurs de la partie \n\
    l : quitter la partie\n\
    m -message : envoyer un message a tous les joueurs\n\
    p -id -message: envoyer un message au joueur id\n");
    char bufScan[300];

     fgets(bufScan, 300,stdin);
    bufScan[strlen(bufScan)-1]='\0';
    if (bufScan[0]=='m'){
        printf("%s\n ", bufScan);
        
        int longueurB = strlen(bufScan);
        char envoimsg [longueurB+6+1+1];
        strcpy(envoimsg,"MALL? ");
        strcat(envoimsg, bufScan+2);
        envoimsg[longueurB + 6]='*';
        envoimsg[longueurB+5]='*';
        envoimsg[longueurB+4]='*';
        envoimsg[longueurB+6+1+1]='\0';
        printf("%s\n", envoimsg);
        send(descr, envoimsg,longueurB+6+1, 0);

    }
    else if (bufScan[0]=='p'){
        int longueurB = strlen(bufScan);
        char envoimsg[longueurB+6+1+8];
        strcpy(envoimsg, "SEND? ");
        strcat(envoimsg, bufScan+2);
        envoimsg[longueurB+4]='*';
        envoimsg[longueurB+5]='*';
        envoimsg[longueurB+6]='*';
        send(descr, envoimsg, longueurB+6+1,0);
        char recums[9];
        int recu = recv(descr,recums , 8,0);
        recums[recu]='\0';
        if (recums[0]=='N'){
            printf("Le message n'a pas pu etre envoyé");

        }
        else{
            printf("Le message a été envoyé\n");
        }
    }
    else if (strlen(bufScan)==1){
        if (bufScan[0]=='l'){
            send(descr, "IQUIT***", 8, 0);
            recv(descr, stdin, 8,0);
        
            exit(0);
        }
        else if (bufScan[0]=='g'){
            glisCom(descr);
        }
    }
    else if (strlen(bufScan)<=5){// si on a recu un message de la forme "z/q/s/d n"
        int longueur = strlen(bufScan);
         
          char intS [4] ;
          int j = 2;
          for(int i = longueur-1; i>=2;i--){
            intS[j]  = bufScan[i];
            j--;
          }
          while(j>=0){
              intS[j] = '0';
              j= j-1;
          }
        intS[4] = '\0';
        int at = atoi(intS);
        printf("%d" ,at);
        if (at ==0){
            printf("mauvais format");
        }

       
            else if (bufScan[0]== 'z') {
                envoiDirection("UPMOV ", descr, intS);
                recevoirPos(descr);
            }
            else if (bufScan[0]=='s'){ 
                envoiDirection("DOMOV ", descr, intS);
                recevoirPos(descr);
            }
            else if (bufScan[0]=='q'){ 
                envoiDirection("LEMOV ", descr, intS);
                recevoirPos(descr);
            }
            else if (bufScan[0]=='d'){ 
                envoiDirection("RIMOV ", descr, intS);
                recevoirPos(descr);
            }
            
        
        }
    
    
    

    }



}    

