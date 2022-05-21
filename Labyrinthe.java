import java.util.ArrayList;
import java.util.Random;

import javax.print.event.PrintEvent;
public class Labyrinthe{

    private int longueur; 
    private int largeur; 
    private Case[][] lab; 
    private int nombreFantomes;
    private ArrayList<Fantome> listeFantomes;

    
    public Labyrinthe(int nb){
        this.longueur = 10; 
        this.largeur = 10; 
        this.lab = this.remplirtableau1();
        nombreFantomes=nb;
        
        this.listeFantomes= new ArrayList<Fantome>();
        this.placerFantome();
        }
    public Labyrinthe( int x, int y){
        this.longueur = x; 
        this.largeur = y;
        this.lab = this.remplirtableau1();
    }

    public int getNbGhost (){
        return this.nombreFantomes;
    }
    public Case[][] remplirtableau (){
        Random random = new Random();
        int nb;
        nb = random.nextInt(6);
        Case[][] tab = new Case[10][10];
        if(nb==0){
            tab=remplirtableau0();
        }
        else if(nb==1){
            tab=remplirtableau1();
        }
        else if(nb==2){
            tab=remplirtableau2();
        }
        else if(nb==3){
            tab=remplirtableau3();
        }
        else if(nb==4){
            tab=remplirtableau4();
        }
        else if(nb==5){
            tab=remplirtableau5();
        }
        return tab; 
    }
    
    public Fantome[] mouvementGhost(){
    
        Fantome[] tab = new Fantome[listeFantomes.size()];
        int i = 0;
        for(Fantome f : listeFantomes){
            int x = f.getX(); 
            int y = f.getY();
                if(isGhost(x,y)){
                    Random random = new Random();
                    int nb;
                    nb = random.nextInt(4);
                    if(nb==0){ //le fantome va a  gauche
                        if( !(isMur(x,y-1)||lab[x][y-1].joueurs.size()!=0||lab[x][y-1].fantome!=null)){
                            this.lab[x][y-1].fantome=f;
                            f.setY(y-1);
                            this.lab[x][y].fantome=null;
                            tab[i] = f;
                            i+=1;
                        }
                    }
                    else if(nb==1){//haut
                        if(!(isMur(x-1,y)||lab[x-1][y].joueurs.size()!=0||lab[x-1][y].fantome!=null)){
                            this.lab[x-1][y].fantome=f;
                            this.lab[x][y].fantome=null;
                            f.setX(x-1);
                            tab[i] =f;
                            i+=1;
                        }
                    }
                    else if(nb==2){//droite
                        if(!(isMur(x,y+1)||lab[x][y+1].joueurs.size()!=0||lab[x][y+1].fantome!=null)){
                            this.lab[x][y+1].fantome=f;
                            this.lab[x][y].fantome=null;
                            f.setY(y+1);
                            tab[i] = f;
                            i+=1;
                        }
                    }
                    else if(nb==3){//bas
                        if(!(isMur(x+1,y)||lab[x+1][y].joueurs.size()!=0||lab[x+1][y].fantome!=null)){
                            this.lab[x+1][y].fantome=f;
                            this.lab[x][y].fantome=null;
                            f.setX(x+1);
                            tab[i] = f;
                            i+=1;
                        }
                        
                    }
                }
                else{
                    System.out.println("pas de fantome a cette case");
                    return null;
                }
            
        }
        return tab;    
    }
    
  synchronized  public int[] mouvementJoueur(Joueur j, String direction, int nb ){// D, G, H, B
        int x=j.getX();
        int y=j.getY();
        if(direction.equals("B")){
            for(int i=1;i<=nb;i++){
                if(!isMur(x+i,y)){
                    if(isGhost(x+i,y)){
                        this.lab[x+i][y].joueurs.add(j);
                        this.lab[x+i-1][y].joueurs.remove(j);
                        j.ajouterFantome();
                        nombreFantomes-=1;
                        listeFantomes.remove(this.lab[x+i][y].fantome);
                        this.lab[x+i][y].fantome =null;
                        int[] t = new int[3];
                        t[0] = x+i; t[1] = y;t[2] = 1;
                        return t;
                    }else{
                    this.lab[x+i][y].joueurs.add(j);
                    this.lab[x+i-1][y].joueurs.remove(j);
                    
                    }
                }
                else{
                    
                    int[] t = new int[2];
                    t[0] = x+i-1; t[1] = y;
                    return t;
                    
                }
            
            }
            int[] t = new int[2];
            
            t[0] = x+nb; t[1] = y;
            return t;
        }
    
        
        else if(direction.equals("H")){
            for(int i=1;i<=nb;i++){
                if (!isMur(x-i,y)){
                    if(isGhost(x-i,y)){
                        this.lab[x-i][y].joueurs.add(j);
                        this.lab[x-i+1][y].joueurs.remove(j);
                        j.ajouterFantome();
                        nombreFantomes-=1;
                        listeFantomes.remove(this.lab[x-i][y].fantome);
                        this.lab[x-i][y].fantome =null;
                        int[] t = new int[3];
                        t[0] = x-i; t[1] = y;t[2] = 1;
                        return t;
                    }else{
                    this.lab[x-i][y].joueurs.add(j);
                    this.lab[x-i+1][y].joueurs.remove(j);
                    
                    }
                }
                else {
                    int[] t = new int[2];
                    t[0] = x-i+1; t[1] = y;
                    return t;
                }
            }
            int[] t = new int[2];
                    t[0] = x-nb; t[1] = y;
                    return t;
        }
        
        else if(direction.equals("G")){
            for(int i=1;i<=nb;i++){
                if(!isMur(x,y-i)){
                    if(isGhost(x,y-i)){
                        this.lab[x][y-i].joueurs.add(j);
                        this.lab[x][y-i+1].joueurs.remove(j);
                        listeFantomes.remove(this.lab[x][y-i].fantome);
                        this.lab[x][y-i].fantome =null;
                        j.ajouterFantome();
                        nombreFantomes-=1;
                        int[] t = new int[3];
                        t[0] = x; t[1] = y-i;t[2] = 1;
                        return t;
                    }else{
                    this.lab[x][y-i].joueurs.add(j);
                    this.lab[x][y-i+1].joueurs.remove(j);
                    
                    }
                }
                else {
                    int[] t = new int[2];
                    t[0] = x; t[1] = y-i+1;
                    return t;
                }
            }
            int[] t = new int[2];
                    t[0] = x; t[1] = y-nb;
                    return t;
        }
        else if(direction.equals("D")){
            for(int i=1;i<=nb;i++){

                if(!isMur(x,y+i)){
                    if(isGhost(x,y+i)){
                        this.lab[x][y+i].joueurs.add(j);
                        this.lab[x][y+i-1].joueurs.remove(j);
                        j.ajouterFantome();
                        nombreFantomes-=1;
                        listeFantomes.remove(this.lab[x][y+i].fantome);
                        this.lab[x][y+i].fantome =null;
                        int[] t = new int[3];
                        t[0] = x; t[1] = y+i; t[2] = 1;
                        return t;
                    }else{
                    this.lab[x][y+i].joueurs.add(j);
                    this.lab[x][y+i-1].joueurs.remove(j);
                    
                    }
                }
                else {
                    int[] t = new int[2];
                    t[0] = x; t[1] = y+i-1;
                    return t;
                }
            }
            int[] t = new int[2];
                    t[0] = x; t[1] = y+nb;
                    return t;
        }
        else {
            return null;
        }
        
    }
    
    public void afficheLabyrinthe(){
        for(int i=0; i<this.lab[0].length;i++){
            System.out.println();
            for(int j=0;j<this.lab[i].length;j++){
                if(!isMur(i,j) && (this.lab[i][j].joueurs.isEmpty()) && isGhost(i,j)){
                    System.out.print("F ");
                }else if((!isMur(i,j)) &&this.lab[i][j].joueurs.isEmpty()&& !isGhost(i,j)){
                    System.out.print("o ");
                }else if(isMur(i,j)){
                    System.out.print("X ");
                }else{
                    System.out.print("J ");
                }
            }
        }
        
    }

                   
                   
                   
    
    /*public Case[][] generateLabAlea(int x, int y){
        Case[][] tab = new Case[x][y];
        for(int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                tab[i][j]=new Case();
            }
        }
    }*/
    
    //templates de labyrinthe
    public Case[][] remplirtableau0 (){
        
        Case[][] tab =new Case[12][12];
        int [][] t =
                {{1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,0,1,0,1,0,1,0,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,0,1,0,1,0,1,0,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,0,1,0,1,0,1,0,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,0,1,0,1,0,1,0,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,0,1,0,1,0,1,0,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1}};
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                if(t[i][j]==1){
                    tab[i][j]=new Case(1, null, null);
                }else{
                    tab[i][j]=new Case(0, new ArrayList<Joueur>(), null);
                }
            }
        }
        return tab;
    }
    
    public Case[][] remplirtableau1 (){
        Case[][] tab =new Case[12][12];
        Case b=new Case(0, new ArrayList<Joueur>(), null);
        int[][] t =
        {{1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,0,0,1,1,0,0,1,1,1,1},
        {1,1,0,1,1,1,0,1,1,1,0,1},
        {1,1,0,1,1,1,0,0,0,0,0,1},
        {1,0,0,1,1,1,0,1,1,1,1,1},
        {1,1,0,1,1,1,0,0,0,1,0,1},
        {1,1,0,0,0,0,0,1,1,1,0,1},
        {1,0,0,1,0,1,1,1,0,0,0,1},
        {1,1,1,1,0,1,1,1,0,1,1,1},
        {1,1,1,1,0,0,0,0,0,1,1,1},
        {1,1,1,1,0,1,1,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1}};
       
        for(int i=0;i<12;i++){
            for(int j=0;j<12;j++){
                if(t[i][j]==1){
                    tab[i][j]=new Case(1, null, null);
                }else{
                    tab[i][j]=new Case(0, new ArrayList<Joueur>(), null);
                }
            }
        }
        return tab;
    }
    
    public Case[][] remplirtableau2 (){
        Case tab[][] = new Case[12][12];
        int[][] t =
        {{1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,1,1,1,0,1,1,0,0,1},
        {1,1,0,1,1,1,0,1,0,0,1,1},
        {1,1,0,1,0,0,0,0,0,0,0,1},
        {1,0,0,1,1,1,0,1,1,1,0,1},
        {1,1,0,1,1,1,0,1,1,1,1,1},
        {1,1,0,0,0,0,0,1,1,1,0,1},
        {1,0,0,1,0,1,1,1,0,0,0,1},
        {1,1,0,1,0,1,1,1,0,1,1,1},
        {1,1,0,1,0,0,0,0,0,0,0,1},
        {1,1,0,0,1,1,1,0,0,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1}};

        for(int i=0;i<12;i++){
            for(int j=0;j<12;j++){
                if(t[i][j]==1){
                    tab[i][j]=new Case(1, null, null);
                }else{
                    tab[i][j]=new Case(0, new ArrayList<Joueur>(), null);
                }
            }
        }
        return tab;
    }
    
    public Case[][] remplirtableau3 (){
        Case tab[][] = new Case[12][12];
        int[][] t =
               
        {{1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,1,0,0,0,0,0,1,1,1},
        {1,1,0,1,0,1,0,1,1,1,0,1},
        {1,1,0,1,0,1,0,1,1,1,0,1},
        {1,0,0,0,0,1,0,1,1,1,0,1},
        {1,1,0,1,1,1,0,0,0,0,0,1},
        {1,1,0,0,0,0,0,1,1,1,1,1},
        {1,0,0,1,0,1,1,1,0,1,1,1},
        {1,1,1,1,0,1,1,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,0,1,1,1,0,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1}};

        for(int i=0;i<12;i++){
            for(int j=0;j<12;j++){
                if(t[i][j]==1){
                    tab[i][j]=new Case(1, null, null);
                }else{
                    tab[i][j]=new Case(0, new ArrayList<Joueur>(), null);
                }
            }
        }
        return tab;
    }
    
    public Case[][] remplirtableau4 (){
        Case tab[][] = new Case[12][12];
        int[][] t =
        {{1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,0,0,0,1,0,1,1,1,1,1},
        {1,1,0,1,1,1,0,1,1,0,0,1},
        {1,1,0,0,0,1,0,0,0,1,0,1},
        {1,0,0,1,1,1,0,1,1,1,0,1},
        {1,1,0,1,1,1,0,1,0,0,0,1},
        {1,1,0,0,0,0,0,1,0,1,0,1},
        {1,0,0,1,0,1,1,1,0,1,0,1},
        {1,1,1,1,0,1,1,1,0,1,1,1},
        {1,1,1,0,0,0,0,0,0,1,1,1},
        {1,1,0,0,1,0,0,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1}};

        for(int i=0;i<12;i++){
            for(int j=0;j<12;j++){
                if(t[i][j]==1){
                    tab[i][j]=new Case(1, null, null);
                }else{
                    tab[i][j]=new Case(0, new ArrayList<Joueur>(), null);
                }
            }
        }
        return tab;
    }
    
    public Case[][] remplirtableau5 (){
        Case tab[][] = new Case[12][12];
        int[][] t =
                
        {{1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,0,0,0,0,1,1,1,1,1,1},
        {1,1,0,1,1,1,0,0,0,1,1,1},
        {1,1,0,1,1,1,0,1,0,0,0,1},
        {1,0,0,1,1,1,0,1,0,0,1,1},
        {1,1,0,1,1,1,0,1,1,1,0,1},
        {1,1,0,0,0,0,0,1,1,1,0,1},
        {1,0,0,1,0,1,1,1,0,1,0,1},
        {1,1,0,1,0,1,1,1,0,0,0,1},
        {1,0,0,1,0,0,0,0,0,1,0,1},
        {1,0,1,1,1,0,0,1,1,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1}};
     
     
        for(int i=0;i<12;i++){
            for(int j=0;j<12;j++){
                if(t[i][j]==1){
                    tab[i][j]=new Case(1, null, null);
                }else{
                    tab[i][j]=new Case(0, new ArrayList<Joueur>(), null);
                }
            }
        }
        
    
        return tab;
    }

    


   /* public int [][] generateLab(int x, int y){
        int [][] tab = new int[x][y];
        return tab;
    }*/

    public boolean isMur (int x, int y){
        if (this.lab[x][y].mur == 0){
            return false; 
        }
        return true; 
    }
    public boolean isGhost (int x, int y){
        if (this.lab[x][y].fantome!=null){
            return true;
        }
        return false;
    }
    public void enleverFantome(Fantome f){
        if(this.lab[f.getX()][f.getY()].enleverFantome()){
            this.nombreFantomes -=1;
            this.listeFantomes.remove(f);
        }
    }

    public void placerFantome(){
        int a = this.nombreFantomes;
        int rand = new Random().nextInt((lab[0].length-1))+1;//nombre aléatoire entre 1 et 11.
        int rand2 = new Random().nextInt((lab[0].length-1))+1;
       
        for(int i = 0; i<a; i++){
            while (isMur(rand, rand2)||isGhost(rand, rand2)){
               
                rand = new Random().nextInt((lab[0].length-1))+1;
                rand2 = new Random().nextInt((lab[0].length-1))+1;
           }
           lab[rand][rand2].fantome = new Fantome(rand, rand2);
          
           listeFantomes.add(lab[rand][rand2].fantome);
           
    
        }
        
    }

    synchronized public int[] placerJoueur(Joueur j){
        int rand = new Random().nextInt((lab[0].length-1))+1;//nombre aléatoire entre 1 et 11.
        int rand2 = new Random().nextInt((lab[0].length-1))+1;
        
            while (isMur(rand, rand2)){
                rand = new Random().nextInt((lab[0].length-1))+1;
                rand2 = new Random().nextInt((lab[0].length-1))+1;
           }
           
           lab[rand][rand2].joueurs.add(j);
           j.setX(rand);
           j.setY(rand2);
           int[] t = new int[2];
           t[0]=rand; t[1]=rand2;
           return t;
        
    }
    public void setFantomes(int n){
        for(Fantome f : listeFantomes){
            int x = f.getX();
            int y = f.getY();
            f = null;
            lab[x][y].fantome=null;
            
        }
        
        this.nombreFantomes=n;
        placerFantome();
    }
}
   
