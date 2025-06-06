package models;

public class Amministratore extends Utente {

    public Amministratore(String nome,  String cognome, String username, String password, String email) {
        super(nome, cognome, username, password, email, 1);
    }

    @Override
    public void registrazione() {
        // Implement registration logic for Amministratore
        System.out.println("Registrazione dell'amministratore: " + nome + " " + cognome);
        // Additional logic can be added here
    }

    @Override
    public String toString() {
        return "Amministratore{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}