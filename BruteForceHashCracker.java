/**
 * Stratégie de cassage par force brute (section 4.2 du sujet).
 *
 * Principe : on génère systématiquement toutes les combinaisons
 * possibles de lettres minuscules (a-z), de longueur 1 à 4, dans
 * l'ordre "a, b, ..., z, aa, ab, ..., zzzz", et on teste chacune
 * jusqu'à trouver une correspondance.
 */
public class BruteForceHashCracker implements HashCracker {

    // Alphabet imposé par le sujet : lettres minuscules uniquement
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    // Longueur maximale imposée par le sujet ("a" jusqu'à "zzzz")
    private static final int MAX_LENGTH = 4;

    @Override
    public String crack(String hash) {
        // On teste d'abord toutes les combinaisons de longueur 1, puis 2,
        // puis 3, puis 4 - c'est l'ordre suggéré par les exemples du sujet
        // ("a, b, ... aa, ab, ...").
        for (int length = 1; length <= MAX_LENGTH; length++) {
            String result = tryAllCombinations(new char[length], 0, hash);
            if (result != null) {
                return result; // trouvé : on arrête tout de suite, pas besoin
                                // de tester les longueurs restantes
            }
        }
        // Aucune combinaison jusqu'à 4 lettres ne correspond au hash
        return null;
    }

    /**
     * Génère récursivement toutes les combinaisons possibles pour un
     * tableau de caractères donné, position par position, et teste
     * chaque combinaison dès qu'elle est entièrement remplie.
     *
     * Exemple pour combo.length = 2 :
     * position 0 -> on essaie 'a','b',...,'z' pour la 1ère lettre
     *   pour chacune, position 1 -> on essaie 'a',...,'z' pour la 2e lettre
     *     quand position == 2 (tableau plein) -> on teste le mot complet
     *
     * @param combo    Tableau en cours de remplissage (sa taille = la
     *                 longueur de combinaison actuellement testée).
     * @param position Indice du caractère en cours de choix.
     * @param hash     Hash cible à retrouver.
     * @return Le mot trouvé, ou null si aucune combinaison ne correspond.
     */
    private String tryAllCombinations(char[] combo, int position, String hash) {
        // Cas de base : le tableau est entièrement rempli -> on a une
        // combinaison complète à tester
        if (position == combo.length) {
            String candidate = new String(combo);
            String candidateHash = MD5Util.hashMD5(candidate);
            if (candidateHash.equalsIgnoreCase(hash)) {
                return candidate; // trouvé !
            }
            return null; // cette combinaison précise ne correspond pas
        }

        // Sinon, on essaie chaque lettre de l'alphabet à la position
        // courante, puis on continue récursivement sur la position suivante
        for (int i = 0; i < ALPHABET.length(); i++) {
            combo[position] = ALPHABET.charAt(i);
            String result = tryAllCombinations(combo, position + 1, hash);
            if (result != null) {
                return result; // dès qu'on trouve, on remonte immédiatement
                                // sans tester les lettres suivantes
            }
        }

        return null; // aucune lettre à cette position n'a mené à une correspondance
    }
}
