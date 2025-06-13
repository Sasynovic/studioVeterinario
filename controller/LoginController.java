package controller;

import entity.Utente;

import java.sql.SQLException;

public class LoginController {

    public LoginResult login(String username, String password) throws SQLException, ClassNotFoundException {

        Utente utente = new Utente();
        return utente.loginUtente(username, password);
    }
}
