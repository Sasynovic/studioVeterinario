package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.Utente;
import controller.LoginResult;

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

    public void create(Utente utente) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO utente (username, nome, cognome, email, password, immagineProfilo, tipoUtente) VALUES (?, ?, ?, ?, ?, ?, 3)";

        // Validazione dei campi obbligatori
        if (utente.getUsername() == null || utente.getUsername().isEmpty() ||
                utente.getNome() == null || utente.getNome().isEmpty() ||
                utente.getCognome() == null || utente.getCognome().isEmpty() ||
                utente.getEmail() == null || utente.getEmail().isEmpty() ||
                utente.getPassword() == null || utente.getPassword().isEmpty()) {
            throw new SQLException("Tutti i campi sono obbligatori.");
        }

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, utente.getUsername());
            stmt.setString(2, utente.getNome());
            stmt.setString(3, utente.getCognome());
            stmt.setString(4, utente.getEmail());
            stmt.setString(5, utente.getPassword());
            if (utente.getImmagineProfilo() == null || utente.getImmagineProfilo().isEmpty()) {
                utente.setImmagineProfilo("default.png"); // Imposta un'immagine di default se non fornita
            }
            stmt.setString(6, utente.getImmagineProfilo());
            stmt.executeUpdate();
        }catch (SQLException e) {
            throw new SQLException("Inserimento fallito: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Driver JDBC non trovato: " + e.getMessage());
        }
    }

    public List<Utente> read(String username) throws SQLException, ClassNotFoundException {
        String query = "SELECT username, nome, cognome, email, password, immagineProfilo FROM utente";
        if (username != null && !username.trim().isEmpty()) {
            query += " WHERE username = ?";
        }

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List<Utente> proprietari = new ArrayList<>();
                    Utente proprietario = new Utente();

                    proprietario.setUsername(rs.getString("username"));
                    proprietario.setNome(rs.getString("nome"));
                    proprietario.setCognome(rs.getString("cognome"));
                    proprietario.setEmail(rs.getString("email"));
                    proprietario.setImmagineProfilo(rs.getString("immagineProfilo"));

                    proprietari.add(proprietario);

                    return proprietari;
                } else {
                    throw new SQLException("Errore durante il recupero degli utenti.");
                }
            }
        }
    }

    public void update(Utente utente, String usernameOld) throws SQLException, ClassNotFoundException {
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
        parameters.add(usernameOld); // parametro per la WHERE clause

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

}

