public class HashCrackerFactory {

    /**
     * Crée et retourne l'instance appropriée de HashCracker.
     * * @param method La méthode de cassage ("DICO" ou "BRUTE").
     * @return Une instance implémentant HashCracker.
     * @throws IllegalArgumentException si la méthode n'est pas reconnue.
     */
    public static HashCracker create(String method) {
        if (method == null) {
            throw new IllegalArgumentException("La methode de cassage ne peut pas etre nulle.");
        }

        // On rend la vérification insensible à la casse pour être plus robuste
        if (method.equalsIgnoreCase("DICO")) {
            return new DictionaryHashCracker();
        } else if (method.equalsIgnoreCase("BRUTE")) {
            return new BruteForceHashCracker();
        } else {
            throw new IllegalArgumentException("Methode de cassage inconnue : " + method + ". Utilisez DICO ou BRUTE.");
        }
    }
}
