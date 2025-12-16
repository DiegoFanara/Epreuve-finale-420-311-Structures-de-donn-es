package mv.sdd.sim;

import mv.sdd.io.Action;
import mv.sdd.utils.Logger;
import mv.sdd.io.ActionType;
import mv.sdd.model.*;
import mv.sdd.sim.thread.Cuisinier;
import mv.sdd.utils.Constantes;
import mv.sdd.utils.Formatter;

import java.util.*;

public class Restaurant {
    private final Logger logger;
    // TODO : Ajouter les attributs nécessaires ainsi que les getters et les setters

    //Temps
    private final Horloge horloge;

    //Statistique
    private final Stats stats;

    //Clients
    private final Map<Integer, Client> clients;

    //Commande
    private final Queue<Commande> fileCommande;

    //En Preparation
    private final List<Commande> commandesEnPreparation;

    //Cuisinier
    private Cuisinier cuisinier;

    private Thread threadCuisinier;

    //Etat Service
    private boolean serviceEnCours;

    // TODO : Ajouter le(s) constructeur(s)
    public Restaurant(Logger logger) {
        this.logger = logger;

        this.horloge = new Horloge();

        this.stats = new Stats(horloge);

        this.clients = new LinkedHashMap<>();

        this.fileCommande = new LinkedList<>();

        this.commandesEnPreparation = new ArrayList<>();

        this.serviceEnCours = false;
    }

    // TODO : implémenter les méthodes suivantes

    // Méthode appelée depuis App pour chaque action

    public void executerAction(Action action) {

        ActionType type = action.getType();

        switch (type) {

            case DEMARRER_SERVICE -> {
                demarrerService(
                        action.getParam1(), // durer max
                        action.getParam2()  // nombre de cuisiniers
                );
            }

            case AJOUTER_CLIENT -> {
                ajouterClient(
                        action.getParam1(), // id
                        action.getParam3(), // nom
                        action.getParam2()  // patience
                );
            }

            case PASSER_COMMANDE -> {
                MenuPlat plat = MenuPlat.valueOf(action.getParam3());
                passerCommande(
                        action.getParam1(), // id client
                        plat
                );
            }

            case AVANCER_TEMPS -> {
                avancerTemps(action.getParam1()); // minutes
            }

            case AFFICHER_ETAT -> {
                afficherEtat();
            }

            case AFFICHER_STATS -> {
                afficherStatistiques();
            }

            case QUITTER -> {
                arreterService();
            }
        }
    }


    public void demarrerService(int dureeMax, int nbCuisiniers) {

        serviceEnCours = true;

        logger.logLine(String.format(
                Constantes.DEMARRER_SERVICE,
                dureeMax,
                nbCuisiniers
        ));

        //thread cuisinier
        cuisinier = new Cuisinier(this);
        threadCuisinier = new Thread(cuisinier);
        threadCuisinier.start();

    }

    public void avancerTemps(int minutes) {

        logger.logLine(Constantes.AVANCER_TEMPS + minutes);

        for (int i = 0; i < minutes; i++) {
            tick();
        }

    }

    public void arreterService() {

        serviceEnCours = false;

        if (cuisinier != null) {
            cuisinier.arreter();
        }

        afficherStatistiques();
    }

    // TODO : Déclarer et implémenter les méthodes suivantes

    // tick()

    private void tick() {

        horloge.avancerTempsSimule(1);

        diminuerPatienceClients();

        avancerCommandesEnPreparation();
    }

    // afficherEtat()

    public void afficherEtat() {

        int nbClients = clients.size();
        int nbServis = stats.getNbServis();
        int nbFaches = stats.getNbFaches();
        int nbEnFile = fileCommande.size();
        int nbEnPreparation = commandesEnPreparation.size();

        logger.logLine(
                Formatter.resumeEtat(
                        horloge.getTempsSimule(),
                        nbClients,
                        nbServis,
                        nbFaches,
                        nbEnFile,
                        nbEnPreparation
                )
        );

        for (Client client : clients.values()) {

            MenuPlat plat = null;

            for (Commande c : fileCommande) {
                if (c.getClient().equals(client)) {
                    plat = c.getPlats().get(0); // premier plat
                    break;
                }
            }

            logger.logLine(
                    Formatter.clientLine(client, plat)
            );
        }
    }

    // afficherStatistiques()

    public void afficherStatistiques() {

        logger.logLine(Constantes.HEADER_AFFICHER_STATS);

        logger.logLine(stats.toString());
    }

    // Client ajouterClient(int id, String nom, int patienceInitiale)

    public Client ajouterClient(int id, String nom, int patienceInitiale) {

        Client client = new Client(id, nom, patienceInitiale);

        clients.put(id, client);

        stats.incrementerTotalClients();

        logger.logLine(
                Formatter.eventArriveeClient(
                        horloge.getTempsSimule(),
                        client
                )
        );

        return client;
    }

    // Commande passerCommande(int idClient, MenuPlat codePlat)

    public Commande passerCommande(int idClient, MenuPlat codePlat) {

        Client client = clients.get(idClient);
        if (client == null) {
            return null;
        }

        Commande commandeExistante = null;
        for (Commande c : fileCommande) {
            if (c.getClient().equals(client)) {
                commandeExistante = c;
                break;
            }
        }

        if (commandeExistante == null) {
            Commande commande = new Commande(client, codePlat);
            fileCommande.add(commande);

            logger.logLine(
                    Formatter.eventCommandeCree(
                            horloge.getTempsSimule(),
                            commande.getId(),
                            client,
                            codePlat
                    )
            );

            return commande;
        }

        commandeExistante.ajouterPlat(codePlat);

        logger.logLine(
                Formatter.eventCommandeCree(
                        horloge.getTempsSimule(),
                        commandeExistante.getId(),
                        client,
                        codePlat
                )
        );

        return commandeExistante;
    }

    // retirerProchaineCommande(): Commande

    public synchronized Commande retirerProchaineCommande() {

        if (fileCommande.isEmpty()) {
            return null;
        }

        // enleve la prochaine commande (FIFO)
        Commande commande = fileCommande.poll();

        // demarre
        commande.demarrerPreparation();

        // ajoute a la preparation
        commandesEnPreparation.add(commande);

        logger.logLine(
                Formatter.eventCommandeDebut(
                        horloge.getTempsSimule(),
                        commande.getId(),
                        commande.getTempsRestant()
                )
        );

        return commande;
    }

    // marquerCommandeTerminee(Commande commande)

    private void marquerCommandeTerminee(Commande commande) {

        Client client = commande.getClient();

        // entrain detre servi
        client.setEtat(EtatClient.SERVI);

        stats.incrementerNbServis();

        stats.incrementerChiffreAffaires(commande.calculerMontant());

        for (MenuPlat plat : commande.getPlats()) {
            stats.incrementerVentesParPlat(plat);
        }

        logger.logLine(
                Formatter.eventCommandeTerminee(
                        horloge.getTempsSimule(),
                        commande.getId(),
                        client
                )
        );
    }

    // Client creerClient(String nom, int patienceInitiale)

    //adapter avec un id
    private Client creerClient(int id, String nom, int patienceInitiale) {
        return new Client(id, nom, patienceInitiale);
    }

    // Commande creerCommandePourClient(Client client)

    //recevoir menuplats sinon pas utilisable
    private Commande creerCommandePourClient(Client client, MenuPlat codePlat) {
        return new Commande(client, codePlat);
    }

    // TODO : implémenter d'autres sous-méthodes qui seront appelées par les méthodes principales
    //  pour améliorer la lisibilité des méthodes en les découpant au besoin (éviter les trés longues méthodes)
    //  exemple : on peut avoir une méthode diminuerPatienceClients()
    //  qui permet de diminuer la patience des clients (appelée par tick())

    private void diminuerPatienceClients() {

        for (Client client : clients.values()) {

            int patienceAvant = client.getPatience();

            client.diminuerPatience(1);

            //le cliente se fache
            if (patienceAvant > 0 && client.getPatience() == 0) {

                stats.incrementerNbFaches();

                logger.logLine(
                        Formatter.eventClientFache(
                                horloge.getTempsSimule(),
                                client
                        )
                );
            }
        }
    }

    private void avancerCommandesEnPreparation() {

        Iterator<Commande> iterator = commandesEnPreparation.iterator();

        while (iterator.hasNext()) {

            Commande commande = iterator.next();

            //minute passe
            commande.decrementerTempsRestant(1);

            if (commande.estTermineeParTemps()) {

                iterator.remove();

                marquerCommandeTerminee(commande);
            }
        }
    }

}
