import java.util.ArrayList;


public class Case{
    public int mur; // 1 = mur, 0 = pas un mur
    public ArrayList<Joueur> joueurs; //présence de joueur ou pas
    public Fantome fantome; //présence de fantome ou pas
    
    public Case(){
        this.mur=0;
        this.joueurs=new ArrayList<Joueur>();
        this.fantome=null;
    }
    
    public Case(int m, ArrayList<Joueur> a, Fantome f){
        this.mur=m;
        this.joueurs=a;
        this.fantome=f;
    }
    public boolean enleverFantome(){
        if (this.fantome!=null){
            this.fantome = null;
            return true;
        }
        else {
            return false;
        }
    }
}
