public class Joueur {
    private String identifiant; //8 caractères
    private String port; //4 caractères numériques. 
    private Partie p;
    //Position du joueur sur un labyrinthe. 
    public int x;
    public int y; 
    public int nbFantomes;

    public Joueur (String id, String port){
        this.identifiant = id;
        this.port = port; 
        this.nbFantomes=0;
        
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
    public void ajouterFantome(){
        this.nbFantomes+=1;
        
    }
    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }
}
