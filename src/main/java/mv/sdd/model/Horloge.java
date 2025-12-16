package mv.sdd.model;

public class Horloge {
    private int tempsSimule = 0;

    public int getTempsSimule() {
        return tempsSimule;
    }

    public void avancerTempsSimule(int minutes) {
        this.tempsSimule += minutes;
    }
}
