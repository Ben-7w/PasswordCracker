/**
 * Point d'entrée en ligne de commande de l'outil passwordCracker.
 *
 * Usage :
 *   java PasswordCracker -m <BRUTE|DICO> -h <hashMD5>
 *
 * Exemples (repris du sujet) :
 *   java PasswordCracker -m BRUTE -h e7247759c1633c0f9f1485f3690294a9
 *   java PasswordCracker -m DICO  -h e7247759c1633c0f9f1485f3690294a9
 *
 * Rôle de cette classe (section 7 du sujet - Contraintes) :
 * - Parser les arguments de la ligne de commande.
 * - Déléguer la création de la stratégie à HashCrackerFactory : cette
 *   classe ne fait JAMAIS "new DictionaryHashCracker()" ou
 *   "new BruteForceHashCracker()" elle-même, sinon ça violerait la
 *   contrainte centrale du sujet (aucune instanciation directe dans le
 *   programme principal).
 * - Lancer le cassage et mesurer le temps d'exécution.
 * - Afficher le résultat au format exact exigé par le sujet.
 */
public class PasswordCracker {

    public static void main(String[] args) {

        // --- 1. Lecture des arguments -m et -h ---
        // On parcourt le tableau args[] à la recherche des drapeaux -m et -h.
        // Dès qu'on trouve un drapeau, on lit la valeur juste après lui.
        String method = null;
        String hash = null;

        for (int i = 0; i < args.length; i++) {
            if ("-m".equals(args[i]) && i + 1 < args.length) {
                method = args[i + 1];
                i++; // on saute la valeur qu'on vient de consommer
            } else if ("-h".equals(args[i]) && i + 1 < args.length) {
                hash = args[i + 1];
                i++;
            }
        }

        // --- 2. Validation minimale avant de continuer ---
        // Si -m ou -h manque, inutile d'aller plus loin : on explique
        // l'usage attendu et on s'arrête proprement (pas d'exception
        // qui remonte avec une pile d'erreurs illisible).
        if (method == null || hash == null) {
            System.out.println("Usage : java PasswordCracker -m <BRUTE|DICO> -h <hashMD5>");
            System.out.println("Exemple : java PasswordCracker -m BRUTE -h e7247759c1633c0f9f1485f3690294a9");
            return;
        }

        // --- 3. Création de la stratégie via la Fabrique ---
        // C'est ICI, et seulement ici, qu'on s'adresse à HashCrackerFactory.
        // Le "if/else" qui choisit DICO ou BRUTE est entièrement caché à
        // l'intérieur de la fabrique : cette classe main ne le voit jamais.
        HashCracker cracker;
        try {
            cracker = HashCrackerFactory.create(method);
        } catch (IllegalArgumentException e) {
            // method ne correspond ni à DICO ni à BRUTE (ou est nul) :
            // la fabrique l'a détecté elle-même et a levé une exception
            // claire, qu'on affiche proprement ici plutôt que de crasher.
            System.out.println("Erreur : " + e.getMessage());
            return;
        }

        // --- 4. Lancement du cassage, avec mesure du temps d'exécution ---
        long debut = System.currentTimeMillis();
        String motDePasse = cracker.crack(hash);
        long fin = System.currentTimeMillis();
        long dureeMs = fin - debut;

        // --- 5. Affichage du résultat, au format EXACT exigé par le sujet ---
        if (motDePasse != null) {
            System.out.println("Password found: " + motDePasse);
        } else {
            System.out.println("Password not found");
        }

        // --- 6. Informations complémentaires ---
        // Autorisées explicitement par le sujet (section 3 : "Toute
        // information pertinente pourra également être affichée : temps
        // d'exécution, nombre de tentatives, etc."). On ajoute ces lignes
        // APRÈS le résultat principal pour ne jamais polluer le format
        // attendu ci-dessus.
        System.out.println("Temps d'execution : " + dureeMs + " ms");
        System.out.println("Methode utilisee : " + method.toUpperCase());
    }
}
