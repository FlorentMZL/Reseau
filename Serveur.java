import java.net.*;
import java.util.Random;
import java.io.*;
import java.lang.*;

public class Serveur{

    public static void main (String[] args){
        try{
            ServerSocket server = new ServerSocket(4242);
            while(true){
                Socket socket = server.accept();
                ThreadClass serv = new ThreadClass(socket);
                Thread t = new Thread(serv);
                t.start();
            


            }
            }catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
        }   
    }
    
}
