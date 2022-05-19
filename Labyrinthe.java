import java.lang.Math;

public class Labyrinthe{

    private int longueur; 
    private int largeur; 
    private Case[][] lab; 
    
    public Labyrinthe(){
        this.longueur = 10; 
        this.largeur = 10; 
        this.lab = this.remplirtableau1(longueur, largeur);
    }
    public Labyrinthe( int x, int y){
        this.longueur = x; 
        this.largeur = y;
        this.lab = this.remplirtableau1(x, y);
    }


    public Case[][] remplirtableau (){
        Random random = new Random();
        int nb;
        nb = random.nextInt(6);
        Case[][] tab = new Case[10][10];
        if(nb==0){
            tab=remplirtableau0;
        }
        else if(nb==1){
            tab=remplirtableau1;
        }
        else if(nb==2){
            tab=remplirtableau2;
        }
        else if(nb==3){
            tab=remplirtableau3;
        }
        else if(nb==4){
            tab=remplirtableau4;
        }
        else if(nb==5){
            tab=remplirtableau5;
        }
        return tab; 
    }
    
    public void mouvementGhost(Case c, int x, int y){
        if(isGhost(x,y)){
            Random random = new Random();
            int nb;
            nb = random.nextInt(4);
            if(nb==0){ //le fantome va a  gauche
                if(x-1>-1 && !isMur(x-1,y){
                    this.tab[x-1][y].fantome=true;
                    this.tab[x][y].fantome=false;
                }
            }
            else if(nb==1){//haut
                if(y-1>-1 && !isMur(x,y-1){
                    this.tab[x][y-1].fantome=true;
                    this.tab[x][y].fantome=false;
                }
            }
            else if(nb==2){//droite
                if(x+1>-1 && !isMur(x+1,y){
                    this.tab[x+1][y].fantome=true;
                    this.tab[x][y].fantome=false;
                }
            }
            else if(nb==3){//bas
                if(y+1>-1 && !isMur(x,y+1){
                    this.tab[x][y+1].fantome=true;
                    this.tab[x][y].fantome=false;
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
        Case a=new Case(1, false, false);
        Case b=new Case(0, false, false);
        Case[][] tab =new Case[10][10];
        int [][] t =
                {{1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,0,1,0,1,0,1,0,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,0,1,0,1,0,1,0,1,0,1},
                {0,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,0,1,0,1,0,1,0,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,0,1,0,1,0,1,0,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0},
                {1,1,0,1,0,1,0,1,0,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1}};
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                if(t[i][j]==1){
                    tab[i][j]=a;
                }else{
                    tab[i][j]=b;
                }
            }
        }
        return tab;
    }
    
    public Case[][] remplirtableau1 (){
        Case a=new Case(1, false, false);
        Case b=new Case(0, false, false);
        Case[][] tab =
                {{a,a,a,a,a,a,a,a,a,a},
                {a,a,b,a,a,a,b,a,a,a},
                {a,a,b,a,a,a,b,b,b,b},
                {b,b,b,a,a,a,b,a,a,a},
                {a,a,b,a,a,a,b,a,a,a},
                {a,a,b,b,b,b,b,a,a,a},
                {a,b,b,a,b,a,a,a,b,a},
                {a,a,a,a,b,a,a,a,b,a},
                {a,a,a,a,b,b,b,b,b,a},
                {a,a,a,a,a,a,a,a,a,a}};
        return tab;
    }
    
    public Case[][] remplirtableau2 (){
        Case a=new Case(1, false, false);
        Case b=new Case(0, false, false);
        Case[][] tab =
                {{a,a,a,a,a,a,a,a,a,a},
                {b,b,b,a,a,a,b,a,a,a},
                {a,a,b,a,b,b,b,b,b,a},
                {a,b,b,a,a,a,b,a,a,a},
                {a,a,b,a,a,a,b,a,a,a},
                {a,a,b,b,b,b,b,a,a,a},
                {a,b,b,a,b,a,a,a,b,b},
                {a,a,b,a,b,a,a,a,b,a},
                {a,a,b,a,b,b,b,b,b,a},
                {a,a,a,a,a,a,a,a,a,a}};
        return tab;
    }
    
    public Case[][] remplirtableau3 (){
        Case a=new Case(1, false, false);
        Case b=new Case(0, false, false);
        Case[][] tab =
                {{a,a,a,a,a,a,a,a,a,a},
                {a,a,b,a,a,a,b,a,a,a},
                {a,a,b,b,b,a,b,b,b,a},
                {a,b,b,a,a,a,b,a,a,a},
                {a,a,b,a,a,a,b,a,b,b},
                {a,a,b,b,b,b,b,a,b,a},
                {b,b,b,a,b,a,a,a,b,a},
                {a,a,a,a,b,a,a,a,b,a},
                {a,a,a,a,b,b,b,b,b,a},
                {a,a,a,a,a,a,a,a,a,a}};
        return tab;
    }
    
    public Case[][] remplirtableau4 (){
        Case a=new Case(1, false, false);
        Case b=new Case(0, false, false);
        Case[][] tab =
                {{a,a,a,a,a,a,a,a,a,a},
                {a,a,b,a,b,a,b,a,a,a},
                {a,a,b,a,b,a,b,a,a,a},
                {a,b,b,b,b,a,b,a,a,a},
                {a,a,b,a,a,a,b,b,b,b},
                {a,a,b,b,b,b,b,a,a,a},
                {a,b,b,a,b,a,a,a,b,a},
                {a,a,a,a,b,a,a,a,b,a},
                {b,b,b,b,b,b,b,b,b,a},
                {a,a,a,a,a,a,a,a,a,a}};
        return tab;
    }
    
    public Case[][] remplirtableau5 (){
        Case a=new Case(1, false, false);
        Case b=new Case(0, false, false);
        Case[][] tab =
                {{a,a,a,a,a,a,a,a,a,a},
                {a,a,b,a,a,a,b,b,b,a},
                {a,a,b,a,a,a,b,a,b,a},
                {a,b,b,a,a,a,b,a,b,b},
                {a,a,b,a,a,a,b,a,a,a},
                {a,a,b,b,b,b,b,a,a,a},
                {b,b,b,a,b,a,a,a,b,a},
                {a,a,b,a,b,a,a,a,b,a},
                {a,b,b,a,b,b,b,b,b,a},
                {a,a,a,a,a,a,a,a,a,a}};
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
        if (this.lab[x][y].fantome){
            return true;
        }
        return false;
    }

}
   
