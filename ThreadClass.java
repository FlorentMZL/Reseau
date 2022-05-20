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
            
           envoiGames(pw);
            
            while(true){
                char[] buf = new char[50];
                if (br.read(buf)==-1||socket.isClosed()){//si le client s'est déconnecté.
                    removeAll(j1);
                    break;
                }
                String bufS = new String (buf);
                String bufCommande = getCommande(bufS);
                System.out.println(bufCommande);
                String req1 = bufCommande.substring(0,6);
                String suite = bufCommande.substring(6,bufCommande.length());

                if (req1.equals("NEWPL ")){
                  
                    
                    if (this.j1 != null){
                        pw.print("REGNO***");
                        pw.flush();
                    }
                    else{
                        String idString = suite.substring(0,8);
                        String portString = suite.substring(9,13);
                        
                        if (checkPort(portString)){
                            
                            this.j1 = new Joueur(idString, portString);
                            int[] a = {10,10};
                            Partie nouvelle = new Partie(a, this.j1);
                            j1.setPartie(nouvelle);
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
                    
                    
                    if (checkEtoiles(suite)&&this.j1 == null){
                        
                        String idString = suite.substring(0,8);
                        System.out.println(idString);
                        String portString = suite.substring(9,13);
                        
                        int numeroPartie = (int)suite.substring(14,15).charAt(0);
                        if (checkPort(portString)){
                            boolean check = false;
                            boolean portcheck = false;
                            for(Partie p :listeParties.getListe()){
                                System.out.println(p.getNumero());
                                System.out.println(numeroPartie);
                                if (!p.getLock()&&p.getNumero()== numeroPartie){
                                    for(Joueur j : p.getlisteJoueurs()){
                                        if (j.getPort().equals(portString)||j.getId().equals(idString)){
                                            System.out.println("Meme port/id");
                                            portcheck=true;break;
                                        }
                                    }
                                    if (portcheck){
                                        break;
                                    }
                                    this.j1 = new Joueur(idString, portString);
                                    p.ajouterJoueur(this.j1);
                                    j1.setPartie(p);
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
                        System.out.println(suite);
                        pw.print("REGNO***");
                        pw.flush();
                    }
                    
                    
                    
                }
                else if (req1.equals("UNREG*")){
                   
                    if (suite.equals("**")){
                        if (this.j1 == null){
                            pw.print("DUNNO***");
                            pw.flush();
                            
                        }
                        else{
                            int numpartiesup =0;
                            for (Partie p : listeParties.getListe()){
                                if (p.getlisteJoueurs().contains(this.j1)){
                                    supprimerJoueur(p,this.j1);
                                    this.verifStart(p, pw, br);
                                    this.j1 = null;
                                    numpartiesup = p.getNumero();
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
                
                    if (suite.equals("**")){
                        envoiGames(pw);
                        
                    }
                    
                }        
                else if (req1.equals("LIST? ")){
                    
                    if (!checkEtoiles(suite)||this.j1!=null){
                        pw.print("DUNNO***");
                        pw.flush();
                    }
                    else {

                        boolean check = false;
                        int numeroPartie = (int)suite.substring(0,1).charAt(0);
                        System.out.println(numeroPartie);
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
                   
                    if(!checkEtoiles(suite)||this.j1!=null){
                        System.out.println(suite);
                        pw.print("DUNNO***");
                        pw.flush();
                    }

                    else{
                        boolean check = false;
                        int numeroPartie = (int)suite.substring(0,1).charAt(0);
                        for(Partie p : listeParties.getListe()){
                            if(p.getNumero()==numeroPartie){
                                String [] ll = p.longueurLargeur();
                                pw.print("SIZE? "+ (char)numeroPartie + " " + ll[0]+ " " + ll[1] + "***");
                                pw.flush();   
                                check = true;
                            }
                            break;
                        }
                        if (!check){
                            pw.print("DUNNO***");
                            pw.flush();
                        }
                    }
                }
                else if (req1.equals("START*")){
                   
                    if (suite.equals("**")&&j1!=null){
                        j1.getPartie().startJoueur(j1);
                        while(!j1.getPartie().getLock()){
                           Thread.sleep(1000);

                        }
                        startPartie(j1.getPartie(), pw, br);

                    }
                    else {

                    }
                }

                
            }
            br.close();
            pw.close();
            return;
                

        
            





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
        synchronized(listeParties){
        String gamesEnvoi = "GAMES ";
            
            gamesEnvoi = "GAMES " + (char)listeParties.nonCommencées();
            gamesEnvoi = gamesEnvoi + "***";
            
            pw.print(gamesEnvoi);
            
            pw.flush();
            String envoieListeGames = "";
            
            for (Partie p : listeParties.getListe()){
                if(!p.getLock()){
                envoieListeGames = "OGAME " + (char)p.getNumero() + " " + (char)p.getNbJoueurs() + "***"; 
                pw.print(envoieListeGames);  
                pw.flush();
                }
            }
        }
    }
    private void listerJoueurs(Partie p, PrintWriter pw){
        pw.print("LIST? "+ (char)p.getNumero() + " "+(char)p.getNbJoueurs()+"***");
        pw.flush();
       for(Joueur j : p.getlisteJoueurs()){
            pw.print("PLAYR "+ j.getId()+"***");
            pw.flush();
        }
        
    }
    private void supprimerJoueur(Partie p, Joueur j){
        if (p.getlisteJoueurs().contains(j)){
            p.supprimerJoueur(j);
        }
        synchronized(p){
            if (p.getlisteJoueurs().size()==0){
                listeParties.remove(p);
            }
        }

    }
    private void removeAll(Joueur j){
        for (Partie p : listeParties.getListe()){
            if (p.getlisteJoueurs().contains(j)){
                supprimerJoueur(p, j);
                if (p.nombreJoueurs()==0){
                    supprimerPartie(p);
                }
                break;
            }
        }
    }
    public void verifStart(Partie p, PrintWriter pw, BufferedReader br){
        if (p.verifStart()){
            this.startPartie(p, pw, br);
        }
    }
    public void supprimerPartie (Partie p){
        this.listeParties.remove(p);
    }
  
    public String getCommande(String s){
        String a = "";
        for(int i = s.length()-1; i>2;i--){
            if (s.charAt(i) =='*'&&s.charAt(i-1)=='*'&&s.charAt(i-2)=='*'){
                a = s.substring(0, i+1);
                break;
            }
        }
        return a ;
    }
    public String remplir(String s){
        while(s.length()<15){
            s = s+"#";

        }
        return s;
    }
    public String completerPos(int a){
        String s = Integer.toString(a);
        while(s.length()!=3){
            s = 0 + s;
        }
        return s;

    }
    public void diffusion_multicast(Partie p,String s){
        try{
            DatagramSocket dso = new DatagramSocket();
            byte[] data = s.getBytes();
            InetSocketAddress ia = new InetSocketAddress(p.getIP(),Integer.parseInt(p.getPort()));
            DatagramPacket paquet = new DatagramPacket(data, data.length, ia);
            dso.send(paquet);
            dso.close();
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }

    }
    public void checkEndPartie(Partie p){
        if (p.getLab().getNbGhost()<=0){
            int maxF = 0;
            Joueur meilleur = null;
            for (Joueur j : p.getlisteJoueurs()){
                if (j.nbFantomes > maxF){
                    meilleur = j;
                    maxF = j.nbFantomes;
                }

            }
            p.finished=true;
            diffusion_multicast(p, "ENDGA " + meilleur.getId() + " " + completerPos(maxF)+ "+++");
        }
    }
    public void retournemouvememnt(Partie p, PrintWriter pw, String mov, int nombrecases){
        Random r = new Random();
        int rand = r.nextInt(2);
        if (rand == 0){
            Fantome [] a =p.lab.mouvementGhost();
            int i = 0;
            while(a[i]!=null){
               
                diffusion_multicast(p, "GHOST "+ completerPos(a[i].getX())+ " "+ completerPos(a[i].getY())+"+++");
                i+=1;
            }
                
            
        }
        int [] coords =p.getLab().mouvementJoueur(this.j1, mov, nombrecases);
        j1.setX(coords[0]);
        j1.setY(coords[1]);
        String coordX = completerPos(coords[0]);
        String coordY = completerPos(coords[1]);
        if (coords.length==3){//Si il a attrapé un fantome
            diffusion_multicast(p, "SCORE "+this.j1.getId()+ " "+ completerPos(this.j1.nbFantomes)+" "+ coordX+ " "+ coordY+"+++");
            checkEndPartie(p);
            String pointsString = completerPos(this.j1.nbFantomes);
            pw.write("MOVEF " + coordX + " " + coordY +" "+ pointsString+"***" );
            pw.flush();
        }
        else {
            pw.write("MOVE! " + coordX + " " + coordY +"***" );
            pw.flush();
        }
        p.lab.afficheLabyrinthe();
    }
    public int diffusion_udp(Partie p, String id, String mes){
        String port = "";
       
        for(Joueur j : p.getlisteJoueurs()){
            if (j.getId().equals(id)){
                port = j.getPort();
            }
            
        }
        
        if (port.equals("")){
            return 0;
        }
        try{
          
            DatagramSocket dso = new DatagramSocket();
            String joueurID = this.j1.getId();
            String concat = "MESSP " + joueurID+" "+mes+"+++";
            byte [] data = concat.getBytes();
            System.out.println(port);
            InetSocketAddress ia = new InetSocketAddress("255.255.255.255", Integer.parseInt(port));
            DatagramPacket paquet = new DatagramPacket(data, data.length, ia);
            dso.send(paquet);
            dso.close();
            
            return 1;
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
            
    }

    public void startPartie(Partie p, PrintWriter pw, BufferedReader br){
        boolean end = false;
        
            String [] ll = p.longueurLargeur();
            String multidif = remplir(p.getIP());
            pw.print("WELCO " + (char)p.getNumero()+ " " +ll[0]+ " " + ll[1]+" " + (char)p.getLab().getNbGhost()+" " +multidif +" "+ p.getPort()+ "***");
            pw.flush();
            int[] coordonnées =p.getLab().placerJoueur(this.j1);
            p.getLab().afficheLabyrinthe();
            pw.print("POSIT " + j1.getId() + " "+ completerPos(coordonnées[0]) + " " + completerPos(coordonnées[1]) + "***");
            pw.flush();
            char[] lireReq = new char[220];
            while(end==false&&!socket.isClosed()){
            try { 
                if (br.read(lireReq)==-1){
                    removeAll(j1);
                    break;
                }
                String bufS = new String (lireReq);
                System.out.println(lireReq);
                String bufCommande = getCommande(bufS);
                System.out.println(bufCommande);
                String req1 = bufCommande.substring(0,6);
                System.out.println(req1);
                String suite = bufCommande.substring(6,bufCommande.length()); 
                if (req1.equals("UPMOV ")&&p.finished==false){
                    
                    int nombrecases = Integer.valueOf(suite.substring(0,3));
                    retournemouvememnt(p, pw, "H", nombrecases);
                }
                else if(req1.equals("DOMOV ")&&p.finished==false){
                    int nombrecases = Integer.valueOf(suite.substring(0,3));
                    retournemouvememnt(p, pw, "B", nombrecases);    
                }
                else if (req1.equals("LEMOV ")&&p.finished==false){
                    int nombrecases = Integer.valueOf(suite.substring(0,3));
                    retournemouvememnt(p, pw, "G", nombrecases);    
                }
                else if (req1.equals("RIMOV ")&&p.finished==false){
                    int nombrecases = Integer.valueOf(suite.substring(0,3));
                    retournemouvememnt(p, pw, "D", nombrecases);    
                }
                else if (req1.equals("IQUIT*")||p.finished == true){
                    pw.print("GOBYE***");
                    try {
                        this.socket.close();
                    }catch (Exception e){
                        System.out.println(e);
                        e.printStackTrace();
                    }
                }
                else if (req1.equals("GLIS?*")&&p.finished==false){
                    synchronized (p.getlisteJoueurs()){
                        int joueurspresents = p.getlisteJoueurs().size();
                        System.out.println("recu");
                        pw.print("GLIS! "+(char)joueurspresents+"***");
                        System.out.println("recu");
                        pw.flush();
                        System.out.println("recu");
                        for(Joueur j : p.getlisteJoueurs()){
                            pw.print("GLYPR "+j.getId() + " "+ completerPos(j.getX())+ " "+ completerPos(j.getY())+" "+ completerPos(j.nbFantomes)+"***");
                            pw.flush();
                        }

                    }
                    
                
                }
                else if (req1.equals("MALL? ")){
                    System.out.println(suite);
                    String suiteSansEtoiles = suite.substring(0, suite.length()-3);

                    if (!(suiteSansEtoiles.contains("***")||suiteSansEtoiles.contains("+++"))){
                        diffusion_multicast(p, "MESSA " + this.j1.getId() + " " + suiteSansEtoiles+"+++");
                    }

                }
                else if (req1.equals("SEND? ")){
                    String suiteSansEtoiles = suite.substring(0, suite.length()-3);
                    System.out.println(suiteSansEtoiles);
                    int a =diffusion_udp(p,suiteSansEtoiles.substring(0,8), suiteSansEtoiles.substring(9,suiteSansEtoiles.length()));
                    if (a==0){
                        pw.print("NSEND***");
                        pw.flush();
                    }
                    if(a!=0){
                        pw.print("SEND!***");
                        pw.flush();
                    }
                }
            }
            catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }
}



