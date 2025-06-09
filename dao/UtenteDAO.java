package dao;

import java.sql.*;

import models.Utente;
import models.Amministratore;
import models.Veterinario;
import models.Proprietario;

import config.DBConnectionManager;

public class UtenteDAO {

    public Boolean login(String username, String password) throws SQLException, ClassNotFoundException {
        String query = "SELECT password FROM utente WHERE username = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    if (dbPassword.equals(password)) {
                        return true;
                    } else {
                        throw new SQLException("Credenziali errate.");
                    }
                } else {
                    throw new SQLException("Utente non trovato.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il login: " + e.getMessage());
            throw e;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trovato: " + e.getMessage());
            throw e;
        }
    }

    public Utente registrazione(String username, String nome, String cognome, String email, String password, String immagineProfilo) throws SQLException, ClassNotFoundException {
            String query = "INSERT INTO utente (username, nome, cognome, email, password, immagineProfilo, tipoUtente) VALUES (?, ?, ?, ?, ?, ?, 3)";

            // Controlla se l'username esiste già
            String checkQuery = "SELECT COUNT(*) FROM utente WHERE username = ?";
            try (
                Connection conn = DBConnectionManager.getConnection();
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery)
            ) {
                checkStmt.setString(1, username);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Username già esistente.");
                    }
                }
            }

            if(username == null || username.isEmpty() ||
               nome == null || nome.isEmpty() ||
               cognome == null || cognome.isEmpty() ||
               email == null || email.isEmpty() ||
               password == null || password.isEmpty() ){
                throw new SQLException("Tutti i campi sono obbligatori.");
            }

            try (
                Connection conn = DBConnectionManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)
            ) {
                stmt.setString(1, username);
                stmt.setString(2, nome);
                stmt.setString(3, cognome);
                stmt.setString(4, email);
                stmt.setString(5, password);
                if (immagineProfilo == null || immagineProfilo.isEmpty()) {
                    immagineProfilo = "default.png"; // Imposta un'immagine di default se non fornita
                }
                stmt.setString(6, immagineProfilo);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    return new Proprietario(username, nome, cognome, email, password, immagineProfilo);
                } else {
                    throw new SQLException("Registrazione fallita.");
                }
            }
        }

    public void aggiornaUtente(Utente utente) throws SQLException, ClassNotFoundException {
            String query = "UPDATE utente SET nome = ?, cognome = ?, email = ?, password = ?, immagineProfilo = ? WHERE username = ?";

            try (
                Connection conn = DBConnectionManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)
            ) {
                stmt.setString(1, utente.getNome());
                stmt.setString(2, utente.getCognome());
                stmt.setString(3, utente.getEmail());
                stmt.setString(4, utente.getPassword());
                stmt.setString(5, utente.getImmagineProfilo());
                stmt.setString(6, utente.getUsername());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Nessun utente aggiornato.");
                }
            }
        }
    }

