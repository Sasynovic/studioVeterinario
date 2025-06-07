package models;

public class Amministratore extends Utente {

    public Amministratore(){super();}

    public Amministratore(String username, String nome, String cognome, String email, String password, String immagineProfilo) {
        super(username, nome, cognome, email, password, immagineProfilo, 1); // 1 per Amministratore
    }
}