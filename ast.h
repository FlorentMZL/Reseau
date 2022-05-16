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
#define MAX_NAME 10;




int recevoirNbGmes (int descr){

    char hellorec[6];
    int a;
    char buff1[3];
    int recu = recv(descr,hellorec,6, 0 );
    hellorec[recu]='\0';
    
   
    
    int qs=recv(descr, &a,sizeof(uint8_t), 0);

    int qsda = recv(descr, buff1, 3, 0);
    printf("%d", a);
    buff1[qsda]='\0';
   
    return a; 
}

void afficherparties(int descr, int nombre){
    char bufOGAME [6];
    int num; 
    int joueursInscrits;
    for(int i = 0; i<nombre, i++){
        int recu = recv(descr, bufOGAME, 6, 0);
        int recuNumero = recv(descr, &num, sizeof(uint8_t), 0);
        recv(descr, NULL, 1, 0);
        int recuJoueurs = recv(descr, &joueursInscrits, sizeof(uint8_t), 0);
        printf ("Partie %d, %d joueurs inscrits.\n" num, joueursInscrits);
    }



}
void afficherUsage(){
    printf("Usage : Entrez le nombre correspondant à l'action souhaitée :\n\
    -1 : Lister les parties en attente \n\
    -2 : Se désinscrire de la partie sélectionnée\n\
    -3 -m : Demander la taille du labyrinthe d'une partie m \n\
    -4 -m : Demander la liste des joueurs inscrits à une partie m \n");
    
}
void unreg(int descr){
    char repBuf[20];
    int envoi = send(descr, "UNREG***",8,0 ); 
    int recu = recv(descr, repBuf, 20, 0 );
    repBuf[recu] = '\0';
    if (strlen(repBuf)==8){
        printf("Vous n'etes inscrit à aucune partie.%s\n", repBuf);
    }
    else {
        printf("Vous avez été désinscrit.\n");
    }
}/*
void listGames(int descr){
    send(descr, "GAME?***", 8,0);
    int a = recevoirNbGmes(descr);
    
    printf("Il y a %d parties en attente\n",a );
    for(int i = 0; i<a, i++){
        int recu = recv(descr, bufOGAME, 6, 0);
        int recuNumero = recv(descr, &num, sizeof(uint8_t), 0);
        recv(descr, NULL, 1, 0);
        int recuJoueurs = recv(descr, &joueursInscrits, sizeof(uint8_t), 0);
        printf ("Partie %d, %d joueurs inscrits.\n" num, joueursInscrits);
    }
}*/