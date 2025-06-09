package entity;

public class Proprietario extends Utente {

    public Proprietario(){super();}

    public Proprietario(String username, String nome, String cognome, String email, String password, String immagineProfilo) {
        super(username, nome, cognome, email, password, immagineProfilo, 3); // 3 per Cliente
    }
}