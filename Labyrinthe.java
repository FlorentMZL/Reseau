public class Labyrinthe{

    private int longueur; 
    private int largeur; 
    private int[][] lab; // 1 = mur, 0 = pas un mur
    
    public Labyrinthe(){
        this.longueur = 10; 
        this.largeur = 10; 
        this.lab = this.remplirtableau1();
    }
    public Labyrinthe( int x, int y){
        this.longueur = x; 
        this.largeur = y;
        this.lab = this.generateLab(x, y);
    }


    public int [][] remplirtableau1 (){
        int [][] tab = 
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

        return tab; 
    }


    public int [][] generateLab(int x, int y){
        int [][] tab = new int[x][y];
        return tab;
    }

    public boolean isMur (int x, int y){
        if (this.lab[x][y] == 0){
            return false; 
        }
        return true; 
    }

}
   