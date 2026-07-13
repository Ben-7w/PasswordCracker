import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe utilitaire regroupant le calcul de hash MD5.
 *
 * Pourquoi cette classe existe :
 * Le sujet interdit explicitement les duplications de code (section 7,
 * "Contraintes"). Or DictionaryHashCracker ET BruteForceHashCracker ont
 * chacune besoin de calculer un hash MD5 et de le comparer au hash cible.
 * Plutot que d'écrire deux fois le même code de hachage, on le centralise
 * ici, et les deux stratégies appellent simplement MD5Util.hashMD5(...).
 *
 * Attention : cette classe ne fait PAS partie du diagramme UML imposé par
 * le sujet (HashCracker / DictionaryHashCracker / BruteForceHashCracker /
 * HashCrackerFactory). C'est un simple outil technique interne, à mentionner
 * dans la section "Architecture" du README comme une classe de support.
 */
public class MD5Util {

    // Constructeur privé : la classe ne contient que des méthodes statiques,
    // elle n'a donc pas de raison d'être instanciée (new MD5Util() n'a pas de sens ici).
    private MD5Util() {
    }

    /**
     * Calcule le hash MD5 d'une chaîne de caractères.
     *
     * @param input Le texte à hacher (un mot du dictionnaire, ou une
     *              combinaison générée par la force brute).
     * @return Le hash MD5 en hexadécimal minuscule (même format que les
     *         exemples du sujet, ex. "e7247759c1633c0f9f1485f3690294a9").
     */
    public static String hashMD5(String input) {
        try {
            // On demande à Java une instance de l'algorithme MD5.
            // C'est fourni nativement par le JDK, aucune librairie externe requise.
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // digest() calcule le hash et renvoie un tableau d'octets bruts
            // (16 octets pour du MD5) : ce n'est pas encore une chaîne lisible.
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // On transforme chaque octet en 2 chiffres hexadécimaux et on
            // concatène le tout pour obtenir la chaîne finale du hash.
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // ex: on veut "0a", pas juste "a"
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // Ne devrait jamais se produire : MD5 fait partie des algorithmes
            // standards livrés avec le JDK. On la transforme en exception non
            // vérifiée pour ne pas alourdir la signature des méthodes appelantes.
            throw new RuntimeException("Algorithme MD5 indisponible sur cette machine.", e);
        }
    }
}
