import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
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

            InputStreamReader ir = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(ir);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            pw.print("GAMES " +  listeParties.taille() + "***");
            pw.flush();
            for (int i = 0; i< listeParties.taille(); i++){
                pw.print("OGAME "+ (Integer.valueOf(listeParties.get(i).getNumero())).byteValue() +" " + listeParties.get(i).getJoueurs()+ "***");
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
                            pw.flush();
                        }
                    }
                }
                else if (req1.equals("REGIS")){
                    char[] idportm = new char[21];
                    br.read(idportm);
                    String idportmS = new String(idportm);
                    String endString = new String(idportmS.substring(18,21));
                    if (!(endString.equals("***"))){
                        
                        pw.print("AAAREGNO "+ endString);
                        pw.flush();
                    }
                    else{
                        int a = Integer.parseInt(idportmS.substring(17,18));
                        for(Partie p :listeParties.getListe()){
                            if (p.getNumero()== a){
                                this.j1 = new Joueur(idportmS.substring(1, 9), idportmS.substring(10,14));
                                p.ajouterJoueur(this.j1);
                                pw.print("REGOK "+ Integer.valueOf(p.getNumero()).byteValue());
                                pw.flush();
                                break;
                            }

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
