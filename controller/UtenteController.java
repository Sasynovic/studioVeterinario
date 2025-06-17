package controller;

import dto.LoginDTO;
import entity.Utente;

import java.sql.SQLException;
import java.util.List;

public class UtenteController {

    public LoginDTO login(String username, String password) throws SQLException, ClassNotFoundException {
        Utente utente = new Utente();
        return utente.loginUtente(username, password);
    }

    public void addUser(String username, String nome, String cognome, String email, String password, String immagineProfilo) throws SQLException, ClassNotFoundException {
        // il tipo utente è impostato a 3 per i nuovi utenti in quanto solo i proprietari possono registrarsi
        Utente utente = new Utente(username, nome, cognome, email, password, immagineProfilo, 3);
        utente.save(utente);
    }

    public List<Utente> getUser(String username) throws SQLException, ClassNotFoundException {
        Utente utente = new Utente();
        return utente.getUtenti(username);
    }

    public void updateUser(String username, String nome, String cognome, String email, String password, String immagineProfilo, String usernameOld) throws SQLException, ClassNotFoundException {
        Utente utente = new Utente();
            utente.setUsername(username);
            utente.setNome(nome);
            utente.setCognome(cognome);
            utente.setEmail(email);
            utente.setPassword(password);
            utente.setImmagineProfilo(immagineProfilo);
        utente.updateUtente(utente, usernameOld);
    }
}
