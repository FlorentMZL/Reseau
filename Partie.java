import java.util.*;
public class Partie {
    private int numero; //Numero de la partie
    private int [] tailleLab; //Taille du labyrinthe
    private int nbJoueurs; 
    private ArrayList<Joueur> JoueurNonValide; //Joueurs n'ayant pas envoyé start
    private ArrayList<Joueur> listeJoueurs; //Joueurs inscrits dans la partie (peu importe s'ils ont envoyé start)

    public Partie(int n, int[] taille, Joueur j ){
        this.numero = n; 
        this.tailleLab = taille; 
        this.nbJoueurs = 1; 
        this.listeJoueurs = new ArrayList<Joueur>();
        this.JoueurNonValide = new ArrayList<Joueur>();
        listeJoueurs.add(j);
        JoueurNonValide.add(j);

    }
    public int[] getTaille (){
        return this.tailleLab;
    }
    public int nombreJoueurs (){
        return this.nbJoueurs;
    }
    public ArrayList<Joueur> listJoueurs(){
        return this.listeJoueurs;
    }

}
