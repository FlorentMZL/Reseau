Les membres du groupe numéro 41:
    -MAZELET Florent 
    -AMIRI Mohamed Aziz 
    
Comment compiler le projet:
    -make
Effacer les fichiers inutiles : 
    -make clean
Comment executer le projet:
    Il faut faire make pour compiler, puis "java Serveur" pour lancer le serveur. 
    Pour lancer le client, ./client lance le client qui utilisera le port 4242 et l'ip localhost. 
    ./client -port utilisera localhost et le port spécifié.
    ./client -port -ip utilisera le et l'ip en paramètre. 


    Une liste d'options apparaitra : 
    Usage : Entrez le nombre correspondant à l'action souhaitée 
    -1 : Lister les parties en attente 
    -2 : Se désinscrire de la partie sélectionnée
    -3 -m : Demander la taille du labyrinthe d'une partie m 
    -4 -m : Demander la liste des joueurs inscrits à une partie m 
    -5 -pseudo -port : Créer une partie. pseudo de 8 caractères alphanumeriques
    -6 -pseudo -port -m : rejoint la partie m avec un pseudo de 8 caractères alphanumeriques
    -7 : \"start\" la partie. N'est utilisable que si vous etes dans une partie
    -8 -m : Ajuster le nombre de fantomes à m. Seulement si vous avez créé la partie
    -9 : Créer des equipes aleatoires
    

    les options 7,8,9 ne sont utilisables qu'après avoir rejoint une partie. 
    Exemple : 

    Si je veux rejoindre la partie 4 j'écris dans le terminal : 6 [username] [port] 4
    Attention à la syntaxe des messages. Si les messages ne produisent rien c'est qu'ils sont mal écrits
    ou alors que vous ne respectez pas certaines conditions (être dans une partie, etre créateur de la partie..)

    une fois après avoir mis "start", le client attend de recevoir un message du serveur. 


    Partie : 
    Il y a différentes commandes : 


    z -n: se déplacer de n cases vers le haut
    s -n : se déplacer de n cases vers le bas 
    q -n : se déplacer de n cases vers la gauche
    d -n : se déplacer de n cases vers la droite
    g : lister tous les joueurs de la partie 
    l : quitter la partie
    m -message : envoyer un message a tous les joueurs
    p -id -message: envoyer un message au joueur id

    Exemple : 
    "z 4" vous fera déplacer dans le labyrinthe de 4 cases vers le haut. 




    Coté serveur : 

    Le serveur affiche toutes les commandes qu'il reçoit/délivre. Il affiche aussi l'état des labyrinthes.







L'architecture de notre projet:
    -Nous avons fait le client en C avec les différentes commandes de connexion et de création de partie.
    -Nous avons programmé le jeu avec les différentes classe Labyrinthe, Case, Joueur... en JAVA.
    -Nous avons également programmé le serveur en JAVA.


    Les commandes sont envoyées dans le même paquet, mais sont reçues par le client a l'aide de plusieurs appels 
    à la fonction recv (les données sont de cette manière assignées plus facilement). Le serveur reçoit les commandes
    du client en un seul Read. 

    Coté client : 
        Le programme analyse le terminal. C'est le joueur qui rentre dans le terminal ce qu'il veut faire a l'aide du manuel
        d'usage. le programme analyse ensuite la requete et envoie les requetes au serveur si besoin. 
        les messages UDP et le multicast sont receptionnés par 2 threads différents.


    Coté serveur : 
    
    Il y a 1 thread par client. La classe s'occupant de ce thread est la classe "ThreadClass".
    Cette classe a une liste de parties qu'elle partage avec les autres threads. Une partie vide est supprimée
    de la liste.
    Le serveur reçoit les requetes client. Un client est un "joueur" si il s'est inscrit a une partie. 
    Quand un joueur s'inscrit a une partie,le serveur vérifie que le port et le nom du client ne sont pas déja utilisés.
    Si le client fait "start", alors le serveur attend que tous les autres joueurs de la partie le fassent. 
    A chaque partie est associée un labyrinthe avec 5 fantomes, ce nombre est modifiable par le créateur
    de la partie. Il y a 8 labyrinthes 10x10 différents qui sont choisis aléatoirement. Les labyrinthes sont un 
    tableau en 2 dimensions de cases de taille 12x12, de manière à ce que le labyrinthe 10x10 soit entouré de murs. 
    Le créateur de la partie peut décider de créer des équipes. elles sont fait simplement en parcourant
    la liste des joueurs et en mettant 1 joueur dans une équipe, et celui d'après dans le parcours dans l'autre liste.
    Chaque fantome ne vaut pas le meme nombre de points. Leur nombre de points est entre 1 et 3 et est décidé aléatoirement 
    a la création des fantomes. 
    Les commandes reçues par le serveur sont traitées comme indiqué dans le protocole.

    Options : Le créateur d'une partie peut décider du nombre de fantomes : il envoie "NBFAN n***" ou n est un entier sur 1 octet. Le serveur répond "FANOK***" si le nombre a été mis a jour. "DUNNO***" sinon.
    Le créateur peut décider s'il y aura des équipes. Si il  envoie "TEAMS***", alors en fonction de si la partie etait
    deja avec des equipes ou pas, elle met son boolean team à jour. Si team etait true, alors il devient false, et inversement. Le serveur renvoie "TEAMN***" si il n'y a pas d'équipes, "TEAM!***" si il y en a. 

    En jeu, le joueur peut savoir son numéro d'équipe en envoyait "MTEAM***". Le serveur lui répond "NTEAM***" si il n'y a
    pas d'équipes, "YTEAM n***" sinon, ou n est un entier sur 1 octet (0 ou 1 en l'occurence car il n'y a que deux équipes)


    Le serveur et le client ont été testés avec des clients/Serveurs d'autres groupes et il n'y a pas eu d'erreurs importantes. 
