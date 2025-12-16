package mv.sdd.io;

// NE PAS MODIFIER
public class ActionParser {

    public static Action parseLigne(String ligne) {
        String[] parts = ligne.split(";");
        String code = parts[0].trim();

        ActionType type = ActionType.valueOf(code); // si les codes matchent l’enum

        return switch (type) {
            case DEMARRER_SERVICE -> new Action(
                    type,
                    parseInt(parts, 1),  // duree
                    parseInt(parts, 2),  // nbCuisiniers
                    null
            );
            case AVANCER_TEMPS -> new Action(
                    type,
                    parseInt(parts, 1),  // minutes
                    0,
                    null
            );
            case AJOUTER_CLIENT -> new Action(
                    type,
                    parseInt(parts, 1),  // idClient
                    parseInt(parts, 3),  // patience
                    parts[2].trim()      // nom
            );
            case PASSER_COMMANDE -> new Action(
                    type,
                    parseInt(parts, 1),  // idClient
                    0,
                    parts[2].trim()      // codePlat
            );
            case AFFICHER_ETAT, AFFICHER_STATS, QUITTER -> new Action(
                    type,
                    0,
                    0,
                    null
            );
        };
    }

    private static Integer parseInt(String[] parts, int index) {
        return Integer.parseInt(parts[index].trim());
    }
}
