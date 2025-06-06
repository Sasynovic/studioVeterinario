public abstract class Utente {
    protected String username;
    protected String password;

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public final boolean login(String username, String password) {
        // Controllo semplice, in realtà potresti aggiungere hash, sicurezza, ecc.
        return this.username.equals(username) && this.password.equals(password);
    }

}


