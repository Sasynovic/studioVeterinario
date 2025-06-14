package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entity.Prenotazione;

public class PrenotazioneDAO {

    public List<Prenotazione> read(Date data, int stato) throws SQLException, ClassNotFoundException {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        String query = "SELECT data, orario, idStato, chipAnimale, idVisita FROM prenotazione WHERE 1 = 1";

        if (data != null) {
            query += " AND data = ?";
        }
        if (stato >= 0) {
            query += " AND idStato = ?";
        }

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, data);
            if (stato >= 0) {
                stmt.setInt(2, stato);
            }
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Prenotazione prenotazione = new Prenotazione();

                prenotazione.setData(rs.getDate("data"));
                prenotazione.setOrario(rs.getInt("orario"));
                prenotazione.setChipAnimale(rs.getInt("chipAnimale"));
                prenotazione.setIdStato(rs.getInt("idStato"));
                prenotazione.setIdVisita(rs.getInt("idVisita"));

                prenotazioni.add(prenotazione);
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la lettura delle prenotazioni: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Driver JDBC non trovato: " + e.getMessage());
        }

        return prenotazioni;
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

}
