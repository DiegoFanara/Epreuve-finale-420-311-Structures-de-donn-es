package mv.sdd.sim.thread;

import mv.sdd.sim.Restaurant;

public class Cuisinier implements Runnable {

    private final Restaurant restaurant;
    private boolean actif = true;

    public Cuisinier(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void arreter() {
        actif = false;
    }

    @Override
    public void run() {

        while (actif) {

            restaurant.retirerProchaineCommande();

            Thread.yield();

        }
    }
}
