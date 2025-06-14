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
}
