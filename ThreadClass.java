import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.*;
import java.nio.charset.Charset;
import java.lang.*;
import java.math.BigInteger;
public class ThreadClass implements Runnable{
    public Socket socket; 
    private ListepartiesClass listeParties;
    private Joueur j1;


    public ThreadClass (Socket s,   ListepartiesClass liste){
        this.socket = s; 
        this.listeParties = liste;
        this.j1 = null;

    }
    public byte[] concatBytes (byte[] t1, byte[] t2){
        byte[] t3 = new byte[t1.length + t2.length];
        for(int i = 0; i<t1.length; i++){
            t3[i] = t1[i];
        }
        for (int j = t1.length +1; j< t3.length; j++){
            t3[j]=t2[j-t1.length-1];
        }
        return t3;
    } 
    
    public void run(){
        try{ 
            InputStream isr = socket.getInputStream();
            OutputStream osr = socket.getOutputStream();
            InputStreamReader ir = new InputStreamReader(isr);
            BufferedReader br = new BufferedReader(ir);
            OutputStreamWriter or = new OutputStreamWriter(osr);
            PrintWriter pw = new PrintWriter(or);
            
            String gamesEnvoi = "GAMES ";
            
            gamesEnvoi = "GAMES " + (char)listeParties.taille();
            gamesEnvoi = gamesEnvoi + "***";
            pw.print(gamesEnvoi);
            
            pw.flush();
            String envoieListeGames = "";
            
            for (Partie p : listeParties.getListe()){
            envoieListeGames = "OGAME " + (char)p.getNumero() + " " + p.getNbJoueurs() + "***"; 
            pw.print(envoieListeGames);  
            pw.flush();
            }
            
            char[] buf = new char[6];
            while(true){
                
                br.read(buf);
                
                String req1 = new String(buf);
                if (req1.equals("NEWPL ")){
                    char[] idport = new char[16];
                    br.read(idport);
                    String idportString = new String(idport);
                    System.out.println(idportString);
                    if (!checkEtoiles(idportString)){
                        pw.print("REGNO***" + idportString);
                        pw.flush();
                    }
                    else{
                        String idString = idportString.substring(0,8);
                        String portString = idportString.substring(9,13);
                        if (checkPort(portString)){
                            this.j1 = new Joueur(idString, portString);
                            int[] a = {10,10};
                            Partie nouvelle = new Partie(a, this.j1);
                            addPartie(nouvelle);
                            pw.print("REGOK "+ (char)nouvelle.getNumero());
                           
                            pw.flush();
                        }
                        else {
                            pw.print("REGNO***");
                            pw.flush();
                        }
                    }
                }
                else if (req1.equals("REGIS ")){
                    
                    char[]idportm = new char[18];
                    br.read(idportm);
                    String idportmS = new String(idportm);
                    if (checkEtoiles(idportmS)){
                        String idString = idportmS.substring(0,8);
                        String portString = idportmS.substring(9,13);
                        int numeroPartie = (int)idportmS.substring(14,15).charAt(0);
                        if (checkPort(portString)){
                            boolean check = false;
                            for(Partie p :listeParties.getListe()){
                                if (p.getNumero()== numeroPartie){
                                    this.j1 = new Joueur(idString, portString);
                                    p.ajouterJoueur(this.j1);
                                    pw.print("REGOK "+ (char)p.getNumero());
                                    pw.flush();
                                    check = true;
                                    break;
                                }
                            
                            }
                            if (!check){
                                pw.print("REGNO***");
                                pw.flush();
                            }
                        }
                    }
                   
                    else{
                        
                        pw.print("REGNO***");
                        pw.flush();
                    }
                    
                    
                    
                }
                else if (req1.equals("UNREG*")){
                    char[] et = new char[2];
                    br.read(et);
                    String etS = new String(et);
                    if (etS.equals("**")){
                        if (this.j1 == null){
                            pw.print("DUNNO***");
                            pw.flush();
                            
                        }
                        else{
                            int numpartiesup =0;
                            for (Partie p : listeParties.getListe()){
                                if (p.getlisteJoueurs().contains(this.j1)){
                                    p.supprimerJoueur(this.j1);
                                    this.j1 = null;
                                    break;
                                }
                            }
                            pw.print("UNROK " + (char)numpartiesup + "***");
                            pw.flush();
                        }
                        
                    }
                    else{
                        pw.print("DUNNO***");
                        pw.flush();
                        System.out.println( "probleme sah");
                    }

                }
                else if (req1.equals("GAME?*")){
                    char[] et = new char[2];
                    br.read(et);
                    String etS = new String(et);
                    if (etS.equals("**")){
                        envoiGames(pw);
                        
                    }
                    else {
                        System.out.println("problemeEtoilesGame?");
                    }
                }        
                else if (req1.equals("LIST?")){
                    char[] suite = new char[5];
                    br.read(suite);
                    String suiteS = new String(suite);
                    if (!checkEtoiles(suiteS)){
                        System.out.println("probleme étoiles");
                        pw.print("DUNNO***");
                        pw.flush();
                    }
                    else {

                        boolean check = false;
                        int numeroPartie = (int)suiteS.substring(1,3).charAt(0);
                        for (Partie p : listeParties.getListe()){
                            if (p.getNumero()==numeroPartie){
                                listerJoueurs(p,pw);
                                check = true;
                                break;
                            }
                        }
                        if (!check){
                            pw.print("DUNNO***");
                            pw.flush();
                        }
                    }
                }
                else if(req1.equals("SIZE? ")) {
                    char[] suite = new char[5];
                    br.read(suite);
                    String suiteS = new String(suite);
                    if(!checkEtoiles(suiteS)){
                        System.out.println("probleme etoiles size");
                        pw.print("DUNNO***");
                        pw.flush();
                    }

                    else{
                        int numeroPartie = (int)suiteS.substring(1,3).charAt(0);
                        for(Partie p : listeParties.getListe()){
                            if(p.getNumero()==numeroPartie){
                                
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
    private boolean checkEtoiles(String s){
        if (s.substring(s.length()-3, s.length()).equals("***")){
            return true;
        }
        else return false;
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
    private void envoiGames (PrintWriter pw){
        String gamesEnvoi = "GAMES ";
            
            gamesEnvoi = "GAMES " + (char)listeParties.taille();
            gamesEnvoi = gamesEnvoi + "***";
           
            pw.print(gamesEnvoi);
            
            pw.flush();
            String envoieListeGames = "";
            
            for (Partie p : listeParties.getListe()){
            envoieListeGames = "OGAME " + (char)p.getNumero() + " " + p.getNbJoueurs() + "***"; 
            pw.print(envoieListeGames);  
            pw.flush();
            }
    }
    private void listerJoueurs(Partie p, PrintWriter pw){
       for(Joueur j : p.getlisteJoueurs()){
            pw.print("PLAYR "+ j.identifiant+"***");
            pw.flush();
        }
        
    }
    
}
