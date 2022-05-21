import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.lang.*;

public class Serveur{

static ArrayList<Partie> ls = new ArrayList<Partie>();
static ListepartiesClass  lsc = new ListepartiesClass(ls);
    public static void main (String[] args){
        int port = 4242;
        try{
            if (args.length>0){
                port = Integer.parseInt(args[1]);
            }
            ServerSocket server = new ServerSocket(port);
            while(true){
               
              
                Socket socket = server.accept();
                
                ThreadClass serv = new ThreadClass(socket, lsc);
                
                Thread t = new Thread(serv);
                t.start();
                
            }
            
            
            }catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
        }   
    }
    
}
