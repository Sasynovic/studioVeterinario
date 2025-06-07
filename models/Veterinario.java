package models;

public class Veterinario extends Utente {

    public Veterinario(){super();}

    public Veterinario(String username, String nome, String cognome, String email, String password, String immagineProfilo) {
        super(username, nome, cognome, email, password, immagineProfilo, 2); // 2 per Cliente
    }
}