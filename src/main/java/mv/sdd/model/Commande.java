package mv.sdd.model;


import mv.sdd.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

public class Commande {

    private int id;

    private static int nbCmd = 0;

    private final Client client;

    private EtatCommande etat = EtatCommande.EN_ATTENTE;

    private int tempsRestant; // en minutes simulées
    // TODO : ajouter l'attribut plats et son getter avec le bon type et le choix de la SdD adéquat

    private final List<MenuPlat> plats = new ArrayList<>();

    // TODO : Ajout du ou des constructeur(s) nécessaires ou compléter au besoin
    public Commande(Client client, MenuPlat plat) {
        id = ++nbCmd;
        this.client = client;
        // À compléter
        this.plats.add(plat);
        this.tempsRestant = 0; // demarrerPreparation()
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public EtatCommande getEtat() {
        return etat;
    }

    public int getTempsRestant() {
        return tempsRestant;
    }

    public void setEtat(EtatCommande etat) {
        this.etat = etat;
    }

    // TODO : Ajoutez la méthode ajouterPlat tant que on attend
    public void ajouterPlat(MenuPlat plat) {
        if (etat == EtatCommande.EN_ATTENTE){
            plats.add(plat);
        }
    }

    // TODO : Ajoutez la méthode demarrerPreparation
    public void demarrerPreparation() {
        this.etat = EtatCommande.EN_PREPARATION;
        this.tempsRestant = calculerTempsPreparationTotal();
    }

    // TODO : Ajoutez la méthode decrementerTempsRestant
    public void decrementerTempsRestant(int minutes) {
        if (etat != EtatCommande.EN_PREPARATION) return;

        tempsRestant -= minutes;

        if (tempsRestant < 0) {
            tempsRestant = 0;
        }
    }

    // TODO : Ajoutez la méthode estTermineeParTemps
    public boolean estTermineeParTemps() {
        return etat == EtatCommande.EN_PREPARATION && tempsRestant <= 0;
    }

    // TODO : Ajoutez la méthode calculerTempsPreparationTotal
    public int calculerTempsPreparationTotal() {
        int total = 0;
        for (MenuPlat code : plats) {
            total += Constantes.MENU.get(code).getTempsPreparation();
        }
        return total;
    }


    // TODO : Ajoutez la méthode calculerMontant
    public double calculerMontant() {
        double total = 0.0;
        for (MenuPlat code : plats) {
            total += Constantes.MENU.get(code).getPrix();
        }
        return total;
    }

    //Methode getPlats
    public List<MenuPlat> getPlats() {
        return List.copyOf(plats);
    }


}
