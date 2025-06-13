package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.Utente;
import controller.LoginResult;
import entity.Proprietario;

public class UtenteDAO {

    public LoginResult login(String username, String password) throws SQLException, ClassNotFoundException {
        String query = "SELECT  nome, cognome, password, tipoUtente FROM utente WHERE username = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    int tipoUtente = rs.getInt("tipoUtente");
                    String nome = rs.getString("nome");
                    String cognome = rs.getString("cognome");

                    if (dbPassword.equals(password)) {
                        return new LoginResult(true, tipoUtente, "Login effettuato con successo.", nome, cognome, username);
                    } else {
                        return new LoginResult(false, 0, "Password errata.", nome, cognome, username);
                    }
                } else {
                    return new LoginResult(false, 0, "Username non trovato.", "", "", username);
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

        if (username == null || username.isEmpty() ||
                nome == null || nome.isEmpty() ||
                cognome == null || cognome.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty()) {
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

    public void aggiornaUtente(Utente utente, String username) throws SQLException, ClassNotFoundException {
        // Lista per costruire dinamicamente la query
        List<String> setClauses = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        // Aggiungi sempre i campi base (assumendo che siano sempre valorizzati)
        setClauses.add("nome = ?");
        parameters.add(utente.getNome());

        setClauses.add("cognome = ?");
        parameters.add(utente.getCognome());

        setClauses.add("email = ?");
        parameters.add(utente.getEmail());

        // Aggiungi password solo se valorizzata
        if (utente.getPassword() != null && !utente.getPassword().trim().isEmpty()) {
            setClauses.add("password = ?");
            parameters.add(utente.getPassword());
        }

        // Aggiungi immagineProfilo solo se valorizzata
        if (utente.getImmagineProfilo() != null && !utente.getImmagineProfilo().trim().isEmpty()) {
            setClauses.add("immagineProfilo = ?");
            parameters.add(utente.getImmagineProfilo());
        }

        setClauses.add("username = ?");
        parameters.add(utente.getUsername());

        // Costruisci la query dinamicamente
        String query = "UPDATE utente SET " + String.join(", ", setClauses) + " WHERE username = ?";
        parameters.add(username); // parametro per la WHERE clause

        try (
                Connection conn = DBConnectionManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            // Imposta tutti i parametri
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Nessun utente aggiornato.");
            }
        }
    }

    public Proprietario getUtenteByUsername(String username) throws SQLException, ClassNotFoundException {
        String query = "SELECT username, nome, cognome, email, password, immagineProfilo FROM utente WHERE username = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Proprietario p = new Proprietario();
                    p.setUsername(rs.getString("username"));
                    p.setNome(rs.getString("nome"));
                    p.setCognome(rs.getString("cognome"));
                    p.setEmail(rs.getString("email"));
                    p.setImmagineProfilo(rs.getString("immagineProfilo"));

                    return p;
                } else {
                    throw new SQLException("Utente non trovato con username: " + username);
                }
            }
        }
    }
}

