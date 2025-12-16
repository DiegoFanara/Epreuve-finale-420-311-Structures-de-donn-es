package mv.sdd.model;

import mv.sdd.utils.Constantes;

import java.util.EnumMap;
import java.util.Map;

public class Stats {

    private final Horloge horloge;

    private int totalClients = 0;

    private int nbServis = 0;
    
    private int nbFaches = 0;

    private double chiffreAffaires = 0;

    // TODO : remplacer Object par le bon type et initilaliser l'attribut avec la bonne valeur
    //  et ajuster les getters et les setters
    
    private Map<MenuPlat, Integer> ventesParPlat;

    // TODO: au besoin ajuster le constructeur et/ou ajouter d'autres
    public Stats(Horloge horloge) {
        this.horloge = horloge;
        // TODO : compléter le code manquant
        this.ventesParPlat = new EnumMap<>(MenuPlat.class);

        for (MenuPlat plat : MenuPlat.values()) {
            ventesParPlat.put(plat, 0);
        }
    }

    public void incrementerTotalClients() {
        totalClients++;
    }

    public void incrementerNbServis() {
        nbServis++;
    }

    public void incrementerNbFaches() {
        nbFaches++;
    }

    public void incrementerChiffreAffaires(double montant) {
        this.chiffreAffaires += montant;
    }

    public static String statsPlatLine(MenuPlat codePlat, int quantite) {
        return "\n" + "\t\t" + codePlat + " : " + quantite;
    }

    // TODO : ajouter incrementerVentesParPlat(MenuPlat codePlat) et autres méthodes au besoin

    public void incrementerVentesParPlat(MenuPlat codePlat) {
        ventesParPlat.put(codePlat, ventesParPlat.get(codePlat) + 1);
    }

    //Getter client facher et servis
    public int getNbServis() {
        return nbServis;
    }

    public int getNbFaches() {
        return nbFaches;
    }

    public String toString() {

        String chaine = String.format(
                Constantes.STATS_GENERAL,
                horloge.getTempsSimule(),
                totalClients,
                nbServis,
                nbFaches,
                chiffreAffaires
        );

        // TODO : ajouter le code pour concaténer avec statsPlatLines les lignes des quantités vendus par plat (à l'aide de ventesParPlat),
        //  sachant que la méthode statsPlatLine sert à formater une ligne et retourne une chaine

        for (MenuPlat plat : MenuPlat.values()) {
            chaine += statsPlatLine(plat, ventesParPlat.get(plat));
        }

        return chaine;
    }
}
