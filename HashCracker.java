public interface HashCracker {
    /**
     * Tente de retrouver le mot de passe à partir de son hash.
     * @param hash L'empreinte MD5 à casser.
     * @return Le mot de passe en clair s'il est trouvé, sinon null.
     */
    String crack(String hash);
}
