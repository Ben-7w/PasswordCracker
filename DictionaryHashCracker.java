import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Stratégie de cassage par dictionnaire (section 4.1 du sujet).
 *
 * Implémente HashCracker : c'est une "stratégie concrète" au sens du
 * pattern Simple Factory, elle ne fait qu'apporter un comportement
 * précis à un contrat déjà fixé par l'interface.
 *
 * Principe : on lit un fichier texte contenant une liste de mots, on
 * calcule le hash MD5 de chacun, et on compare au hash recherché.
 * Dès qu'un mot correspond, on le retourne immédiatement.
 */
public class DictionaryHashCracker implements HashCracker {

    // Chemin du fichier dictionnaire. L'interface HashCracker impose la
    // signature crack(String hash) - donc impossible de faire transiter
    // un chemin de fichier personnalisé par cette méthode. On fixe donc
    // un nom de fichier attendu dans le dossier d'exécution du programme.
    private static final String DICTIONARY_PATH = "dictionnaire.txt";

    @Override
    public String crack(String hash) {
        // BufferedReader : on lit le fichier ligne par ligne au lieu de
        // tout charger en mémoire d'un coup. C'est la directive explicite
        // du sujet ("classes optimisées pour la lecture de flux") - utile
        // si le dictionnaire contient des millions de mots.
        try (BufferedReader reader = new BufferedReader(
                new FileReader(DICTIONARY_PATH, StandardCharsets.UTF_8))) {

            String word;
            // readLine() renvoie null quand on atteint la fin du fichier :
            // c'est notre condition d'arrêt naturelle de boucle.
            while ((word = reader.readLine()) != null) {

                // On retire les espaces et retours à la ligne parasites
                // (utile si le fichier vient d'un éditeur Windows par exemple)
                word = word.trim();

                // On ignore les lignes vides pour ne pas hacher "" inutilement
                if (word.isEmpty()) {
                    continue;
                }

                // Hash MD5 du mot courant, via la méthode utilitaire partagée
                String wordHash = MD5Util.hashMD5(word);

                // Comparaison insensible à la casse : un hash MD5 peut être
                // fourni en majuscules ou en minuscules selon la source
                if (wordHash.equalsIgnoreCase(hash)) {
                    return word; // trouvé : on s'arrête tout de suite
                }
            }

        } catch (IOException e) {
            // Le fichier n'existe pas ou n'est pas lisible : on affiche un
            // message clair plutôt que de laisser planter le programme avec
            // une pile d'erreurs Java brute et illisible pour l'utilisateur.
            System.err.println("Erreur : impossible de lire le fichier dictionnaire '"
                    + DICTIONARY_PATH + "' (" + e.getMessage() + ")");
        }

        // Aucun mot du dictionnaire ne correspond au hash recherché
        return null;
    }
}
