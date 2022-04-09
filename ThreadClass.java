import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
public class ThreadClass implements Runnable{
    public Socket socket; 
    private ArrayList<Partie> listeParties;



    public ThreadClass (Socket s, ArrayList<Partie> liste){
        this.socket = s; 
        this.listeParties = liste;

    }

    public void run(){
        try{ 
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    

    }
    private void addPartie(Partie p){
        synchronized(listeParties){
            listeParties.add(p);
        }
    }
}
