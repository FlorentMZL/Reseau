import java.util.Random;
public class Fantome {
    private int x;
    private int y; 
    private int point;
    public Fantome(int x, int y){
        this.x = x;
        this.y = y;
        Random random = new Random();
        int nb;
        nb = random.nextInt(3);
        this.point = nb+1;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public int getPoint(){
        return point;
    }
}
