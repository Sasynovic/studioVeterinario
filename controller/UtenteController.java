package controller;

import entity.Utente;

import java.sql.SQLException;

public class UtenteController {

    public LoginResult login(String username, String password) throws SQLException, ClassNotFoundException {
        Utente utente = new Utente();
        return utente.loginUtente(username, password);
    }

    public void addUser(String username, String nome, String cognome, String email, String password, String immagineProfilo) throws SQLException, ClassNotFoundException {
        // il tipo utente è impostato a 3 per i nuovi utenti in quanto solo i proprietari possono registrarsi
        Utente utente = new Utente(username, nome, cognome, email, password, immagineProfilo, 3);
        utente.save(utente);
    }
}
