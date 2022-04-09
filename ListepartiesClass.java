import java.util.ArrayList;


public class  ListepartiesClass{

    private ArrayList<Partie> listeParties;
    static int idMax;

    public ListepartiesClass (ArrayList<Partie> l){
        this.listeParties = l;
        idMax=0;
    }

    public int increment(){
        idMax+=1;
        return idMax;
        
    }
    public int taille(){
        return this.listeParties.size();
    } 
    public Partie get (int i){
        return listeParties.get(i);
    }
    public void add(Partie e){
        this.listeParties.add(e);
        e.setNumero(this.increment());
    }

}