import java.util.ArrayList;


public class  ListepartiesClass{
    static int nbMult;
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
    public ArrayList<Partie> getListe(){
        return this.listeParties;
    }
    public void add(Partie e){
        this.listeParties.add(e);
        e.setNumero(this.increment());
        e.setIP("225.10."+nbMult+".4");
        nbMult+=1;
    }
    public void remove(Partie p){
        listeParties.remove(p);
    }
    public int nonCommencées(){
        int i = 0;
        for(Partie p:listeParties){
            if (p.getLock()==false){
                i++;
            }
        }
        return i;

    }

}