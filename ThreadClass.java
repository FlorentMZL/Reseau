import java.io.*;
import java.net.*;
import java.util.*;
public class ThreadClass implements Runnable{
    public Socket socket; 
    private ListepartiesClass listeParties;
    private Joueur j1;


    public ThreadClass (Socket s,   ListepartiesClass liste){
        this.socket = s; 
        this.listeParties = liste;

    }

    public void run(){
        try{ 
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            int nbgames = listeParties.taille();
            pw.print("GAMES " + nbgames + "***");
            pw.flush();
            for (int i = 0; i<nbgames; i++){
                pw.print("OGAME "+ listeParties.get(i).getNumero() +" " + listeParties.get(i).getJoueurs()+ "***");
            }
            pw.flush();
            char[] buf = new char[5];
            while(true){
                br.read(buf);
                String req1 = new String(buf);
                if (req1.equals("NEWPL")){
                    char[] idport = new char[17];
                    br.read(idport);
                    String idportString = new String(idport);
                    System.out.println(idportString);
                    if (!(idportString.substring(14,17).equals("***"))){
                        pw.print("REGNO***");
                        pw.flush();
                    }
                    else{
                        String idString = idportString.substring(1,9);
                        String portString = idportString.substring(10,14);
                        if (checkPort(portString)){
                            this.j1 = new Joueur(idString, portString);
                            int[] a = {10,10};
                            Partie nouvelle = new Partie(a, this.j1);
                            addPartie(nouvelle);
                            pw.print("REGOK "+ nouvelle.getNumero());
                            pw.flush();
                        }
                        else {
                            pw.print("REGNO***");
                        }
                    }
                }
                
            }
           
                

        
            





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
    private boolean checkPort(String s){
        
        try {
            int a = Integer.parseInt(s);
            return true;
        }
        catch(NumberFormatException e){
            System.out.println("pas un nombre pour le port!");
        }
        return false;
    }
}
