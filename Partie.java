import java.util.*;
public class Partie {
    private int numero; //Numero de la partie
    private int [] tailleLab; //Taille du labyrinthe
    private int nbJoueurs; 
    private ArrayList<Joueur> JoueurNonValide; //Joueurs n'ayant pas envoyé start
    private ArrayList<Joueur> listeJoueurs; //Joueurs inscrits dans la partie (peu importe s'ils ont envoyé start)

    public Partie( int[] taille, Joueur j ){
      
        this.tailleLab = taille; 
        this.nbJoueurs = 1; 
        this.listeJoueurs = new ArrayList<Joueur>();
        this.JoueurNonValide = new ArrayList<Joueur>();
        listeJoueurs.add(j);
        JoueurNonValide.add(j);

    }
    public Partie(Joueur j){
        listeJoueurs.add(j);
        JoueurNonValide.add(j);
    }
    public int[] getTaille (){
        return this.tailleLab;
    }
    public int nombreJoueurs (){
        return this.nbJoueurs;
    }
    public ArrayList<Joueur> getlisteJoueurs(){
        return this.listeJoueurs;
    }
    public int getNumero(){
        return this.numero;
    }
    public int getNbJoueurs(){
        return this.nbJoueurs;
    }
    public void setNumero(byte n){
        this.numero = n;
    }
    public void ajouterJoueur(Joueur j){
        listeJoueurs.add(j);
        JoueurNonValide.add(j);

    }
    public void supprimerJoueur(Joueur j){
        this.listeJoueurs.remove(j);
        this.nbJoueurs -=1;
    }
    
    public String[] longueurLargeur (){
        //int -> binaire sur 16bits, on coupe en deux pour avoir 2x 8 bits et on les rearrange pour avoir du 
        //little-endian. Ensuite on retransforme en char et on envoie les chars pour envoyer un entier sur 2 octets
        String[] result = new String [2];
        if(this.tailleLab[0]<=255){
            char c = (char) 0;
            char d = (char) tailleLab[0];
            String cS = String.valueOf(c);
            String dS = String.valueOf(d);    
            result[0] = dS + cS;
        }   
        if (this.tailleLab[1]<=255){
            char c = (char) 0;
            char d = (char) tailleLab[0];
            String cS = String.valueOf(c);
            String dS = String.valueOf(d);    
            result[1] = dS + cS;
        }
        if (this.tailleLab[0]>255){
            
        }
    }
}
