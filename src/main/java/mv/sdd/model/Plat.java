package mv.sdd.model;

public class Plat {
    private final MenuPlat code;
    private final String nom;
    private final int tempsPreparation; // en minutes simulées
    private final double prix;

    public Plat(MenuPlat code, String nom, int tempsPreparation, double prix) {
        this.code = code;
        this.nom = nom;
        this.tempsPreparation = tempsPreparation;
        this.prix = prix;
    }

    public MenuPlat getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

    public int getTempsPreparation() {
        return tempsPreparation;
    }

    public double getPrix() {
        return prix;
    }
}
