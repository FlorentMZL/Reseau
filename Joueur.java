public class Joueur {
    private String identifiant; //8 caractères
    private String port; //4 caractères numériques. 
    private Partie p;
    //Position du joueur sur un labyrinthe. 
    public int x;
    public int y; 

    public Joueur (String id, String port){
        this.identifiant = id;
        this.port = port; 
    }
    public String getId(){
        return this.identifiant;

    }
    public String getPort(){
        return this.port;
    }
    public Partie getPartie(){
        return this.p;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public void setPartie(Partie p){
        this.p = p;
    }
}
