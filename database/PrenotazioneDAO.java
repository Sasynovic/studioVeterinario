package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import controller.PrenotationResult;
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

    public List<PrenotationResult> readPrenotazioniToday(Date data) throws SQLException, ClassNotFoundException {
        String query = "SELECT p.data, p.orario, p.chipAnimale, p.chipAnimale, " +
                "s.nomeStato, " +
                "a.nome AS nomeAnimale, " +
                "u.nome, u.cognome " +
                "FROM prenotazione p " +
                "LEFT JOIN stato s ON s.idStato = p.idStato " +
                "LEFT JOIN animale a ON a.chip = p.chipAnimale " +
                "LEFT JOIN utente u ON u.username = a.usernameUtente " +
                "WHERE p.data = ? AND p.idStato > 0 " +
                "ORDER BY p.orario";


        List<PrenotationResult> prenotazioni = new ArrayList<>();

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, new java.sql.Date(data.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Date prenotazioneData = rs.getDate("data");
                    int orario = rs.getInt("orario");
                    String nomeStato = rs.getString("nomeStato");
                    int chipAnimale = rs.getInt("chipAnimale");
                    String nomeAnimale = rs.getString("nomeAnimale");
                    String nomeProprietario = rs.getString("nome") + " " + rs.getString("cognome");

                    PrenotationResult result = new PrenotationResult(prenotazioneData, orario, nomeStato, chipAnimale, nomeAnimale, nomeProprietario);
                    prenotazioni.add(result);
                }
            }catch (SQLException e) {
                throw new SQLException("Errore durante la lettura delle prenotazioni: " + e.getMessage(), e);
            }
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

    public List<PrenotationResult> readVaccinazioniYear(Date data) throws SQLException, ClassNotFoundException {String query = "SELECT p.data, p.orario, p.chipAnimale, p.chipAnimale, " +
            "s.nomeStato, " +
            "a.nome AS nomeAnimale, " +
            "u.nome, u.cognome " +
            "FROM prenotazione p " +
            "LEFT JOIN stato s ON s.idStato = p.idStato " +
            "LEFT JOIN animale a ON a.chip = p.chipAnimale " +
            "LEFT JOIN utente u ON u.username = a.usernameUtente " +
            "LEFT JOIN visita v ON p.idVisita = v.idVisita " +
            "WHERE p.data <= ? AND v.tipoVisita = 'Vaccinazione' " +
            "ORDER BY p.orario";


        List<PrenotationResult> prenotazioni = new ArrayList<>();

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, new java.sql.Date(data.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Date prenotazioneData = rs.getDate("data");
                    int orario = rs.getInt("orario");
                    String nomeStato = rs.getString("nomeStato");
                    int chipAnimale = rs.getInt("chipAnimale");
                    String nomeAnimale = rs.getString("nomeAnimale");
                    String nomeProprietario = rs.getString("nome") + " " + rs.getString("cognome");

                    PrenotationResult result = new PrenotationResult(prenotazioneData, orario, nomeStato, chipAnimale, nomeAnimale, nomeProprietario);
                    prenotazioni.add(result);
                }
            }catch (SQLException e) {
                throw new SQLException("Errore durante la lettura delle prenotazioni: " + e.getMessage(), e);
            }
        }
        return prenotazioni;
    }


}
