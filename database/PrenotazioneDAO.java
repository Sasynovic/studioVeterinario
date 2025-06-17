package database;

import java.sql.*;
import java.util.Date;

import entity.Prenotazione;

public class PrenotazioneDAO {

    public void create(Prenotazione p)  throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO prenotazione (data, orario, idStato) VALUES (?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, new java.sql.Date(p.getData().getTime()));
            stmt.setInt(2, p.getOrario());
            stmt.setInt(3, p.getIdStato());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Nessuna prenotazione inserita.");
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'inserimento della prenotazione: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Driver JDBC non trovato: " + e.getMessage(), e);
        }
    }

    public void update(Prenotazione prenotazione) throws SQLException, ClassNotFoundException {
        String query = "UPDATE prenotazione SET idStato = ?, chipAnimale = ?";
        boolean hasIdVisita = prenotazione.getIdVisita() > 0;

        if (hasIdVisita) {
            query += ", idVisita = ?";
        }

        query += " WHERE data = ? AND orario = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, prenotazione.getIdStato());
            stmt.setInt(2, prenotazione.getChipAnimale());

            if (hasIdVisita) {
                stmt.setInt(3, prenotazione.getIdVisita());
                stmt.setDate(4, new java.sql.Date(prenotazione.getData().getTime()));
                stmt.setInt(5, prenotazione.getOrario());
            } else {
                stmt.setDate(3, new java.sql.Date(prenotazione.getData().getTime()));
                stmt.setInt(4, prenotazione.getOrario());
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Nessuna prenotazione aggiornata.");
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'aggiornamento della prenotazione: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Driver JDBC non trovato: " + e.getMessage(), e);
        }
    }

    public void updateVet(Date data, int ora, int idVisita, int idStato) throws SQLException, ClassNotFoundException {
        String query = "UPDATE prenotazione SET idStato = ?, idVisita = ? WHERE data = ? AND orario = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idStato);
            stmt.setInt(2, idVisita);
            stmt.setDate(3, new java.sql.Date(data.getTime()));
            stmt.setInt(4, ora);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Prenotazione non trovata");
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'aggiornamento della prenotazione: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Driver JDBC non trovato: " + e.getMessage(), e);
        }
    }
}
