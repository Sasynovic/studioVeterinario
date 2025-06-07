package dao;

import java.sql.*;

import models.Utente;
import models.Amministratore;
import models.Veterinario;
import models.Proprietario;

import config.DBConnectionManager;

public class UtenteDAO {
        public Utente login (String username, String password) throws SQLException, ClassNotFoundException {
            String query = "SELECT username, nome, cognome, email, password, immagineProfilo, tipoUtente " +
                    "FROM utente WHERE username = ?";

            try (
                    Connection conn = DBConnectionManager.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(query)
            ) {
                stmt.setString(1, username);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Confronta la password (meglio se hashata in futuro)
                        String pwdFromDb = rs.getString("password");

                        if (pwdFromDb.equals(password)) {
                            int tipo = rs.getInt("tipoutente");

                            switch(tipo){
                                case 1: // Amministratore
                                    return new Amministratore(
                                        rs.getString("username"),
                                        rs.getString("nome"),
                                        rs.getString("cognome"),
                                        rs.getString("email"),
                                        pwdFromDb,
                                        rs.getString("immagineProfilo")
                                    );
                                case 2: // Veterinario
                                    return new Veterinario(
                                        rs.getString("username"),
                                        rs.getString("nome"),
                                        rs.getString("cognome"),
                                        rs.getString("email"),
                                        pwdFromDb,
                                        rs.getString("immagineProfilo")
                                    );
                                case 3: // Proprietario
                                    return new Proprietario(
                                        rs.getString("username"),
                                        rs.getString("nome"),
                                        rs.getString("cognome"),
                                        rs.getString("email"),
                                        pwdFromDb,
                                        rs.getString("immagineProfilo")
                                    );
                                default:
                                    throw new SQLException("Tipo utente sconosciuto: " + tipo);
                            }
                        }
                    }
                }
            }

            return null; // Credenziali errate
        }

        public Utente registrazione (String username, String nome, String cognome, String email, String password, String immagineProfilo) throws SQLException, ClassNotFoundException {
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
    }

