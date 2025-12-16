package mv.sdd.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Lecture du fichier d'actions
public class ActionFileReader {
    public static List<Action> readActions(String filePath) throws IOException {
        List<Action> actions = new ArrayList<>();

        // TODO : Ajouter le code qui permet de lire et parser un fichier d'actions

        List<String> lignes = Files.readAllLines(Path.of(filePath));

        for (String ligne : lignes) {

            if (ligne == null) continue;

            ligne = ligne.trim();

            // Ignorer lignes vides et commentaires
            if (ligne.isEmpty() || ligne.startsWith("#")) {
                continue;
            }

            // Parser la ligne avec ActionParser
            Action action = ActionParser.parseLigne(ligne);
            actions.add(action);
        }

        return actions;
    }
}
