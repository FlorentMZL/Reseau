import java.util.*;

import javax.swing.plaf.synth.SynthCheckBoxMenuItemUI;
public class Partie {
    private int numero; //Numero de la partie
    private int [] tailleLab; //Taille du labyrinthe
    private int nbJoueurs; 
    private ArrayList<Joueur> joueurNonValide; //Joueurs n'ayant pas envoyé start
    private ArrayList<Joueur> listeJoueurs; //Joueurs inscrits dans la partie (peu importe s'ils ont envoyé start)
    private boolean lock;//Savoir quand on peut commencer une partie
    public Partie( int[] taille, Joueur j ){
      
        this.tailleLab = taille; 
        this.nbJoueurs = 1; 
        this.listeJoueurs = new ArrayList<Joueur>();
        this.joueurNonValide = new ArrayList<Joueur>();
        listeJoueurs.add(j);
        joueurNonValide.add(j);
        lock = false;
    }
    public Partie(Joueur j){
        listeJoueurs.add(j);
        joueurNonValide.add(j);
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
    public void setNumero(int n){
        this.numero = n;
    }
    public void ajouterJoueur(Joueur j){
        listeJoueurs.add(j);
        joueurNonValide.add(j);
        this.nbJoueurs+=1;

    }
    public void supprimerJoueur(Joueur j){
        this.listeJoueurs.remove(j);
        this.joueurNonValide.remove(j);
        this.nbJoueurs -=1;
    }
    public boolean getLock(){
        return this.lock;
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
            char d = (char) tailleLab[1];
            String cS = String.valueOf(c);
            String dS = String.valueOf(d);    
            result[1] = dS + cS;
        }
        if (this.tailleLab[0]>255){
            result[0] = convert2bytes(this.tailleLab[0]);
        }
        if (this.tailleLab[1]>255){
            result[1] = convert2bytes(this.tailleLab[1]);
        }
        return result;
    }
    public String convert2bytes(int a){
        String ecriturebin = Integer.toBinaryString(a);
            int longueur= ecriturebin.length();
            String zeros = "0000000000000000";
            if(longueur<16){
                ecriturebin = zeros.substring(0,16-longueur).concat(ecriturebin);
            }
            else{
                ecriturebin = ecriturebin.substring(longueur-16);
            }
            String b2 = ecriturebin.substring(0,8);
            String b1 = ecriturebin.substring(8,16);
            char b1c = (char)Integer.parseInt(b1);
            String b1cS = String.valueOf(b1c);
            char b2c = (char)Integer.parseInt(b2);
            String b2cS = String.valueOf(b2c);
            String tab = b1cS + b2cS;
            return tab;
        }

        
    public void startJoueur(Joueur j){
        joueurNonValide.remove(j);
        
            if (this.joueurNonValide.size()==0&&this.listeJoueurs.size()!=0){
              this.lock = true;
            }
        }
    
    public boolean verifStart(){
        if (this.joueurNonValide.size()==0&&this.listeJoueurs.size()!=0){
            this.lock = true;
            return this.lock;
        }
        return this.lock;
    }
    }

