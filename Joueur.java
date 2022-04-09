public class Joueur {
    String identifiant; //8 caractères
    String port; //4 caractères numériques. 

    //Position du joueur sur un labyrinthe. 
    int x; 
    int y; 

    public Joueur (String id, String port){
        this.identifiant = id;
        this.port = port; 
    }

}
