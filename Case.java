public class Case{
    public int mur; // 1 = mur, 0 = pas un mur
    public boolean joueur; //présence de joueur ou pas
    public boolean fantome; //présence de fantome ou pas
    
    public Case(){
        this.mur=0;
        this.joueur=false;
        this.fantome=false;
    }
    
    public Case(int m, boolean j, boolean f){
        this.mur=m;
        this.joueur=j;
        this.fantome=f;
    }
    
}
