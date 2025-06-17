package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.Agenda;

public class AgendaDAO {

    public List<Agenda> readDisponibili(Date data) throws SQLException, ClassNotFoundException {
        String query = "SELECT p.data, p.orario, p.chipAnimale, p.chipAnimale, " +
                "s.nomeStato, " +
                "a.nome AS nomeAnimale, " +
                "u.nome, u.cognome " +
                "FROM prenotazione p " +
                "LEFT JOIN stato s ON s.idStato = p.idStato " +
                "LEFT JOIN animale a ON a.chip = p.chipAnimale " +
                "LEFT JOIN utente u ON u.username = a.usernameUtente " +
                "WHERE p.data = ? AND p.idStato = 0 " +
                "ORDER BY p.orario";

        List<Agenda> prenotazioni = new ArrayList<>();

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

                    Agenda result = new Agenda(prenotazioneData, orario, nomeStato, chipAnimale, nomeAnimale, nomeProprietario);
                    prenotazioni.add(result);
                }
            }catch (SQLException e) {
                throw new SQLException("Errore durante la lettura delle prenotazioni: " + e.getMessage(), e);
            }
        }
        return prenotazioni;
    }


    public List<Agenda> readPrenotazioniToday(Date data) throws SQLException, ClassNotFoundException {
        String query = "SELECT p.data, p.orario, p.chipAnimale, p.chipAnimale, " +
                "s.nomeStato, " +
                "a.nome AS nomeAnimale, " +
                "u.nome, u.cognome " +
                "FROM prenotazione p " +
                "LEFT JOIN stato s ON s.idStato = p.idStato " +
                "LEFT JOIN animale a ON a.chip = p.chipAnimale " +
                "LEFT JOIN utente u ON u.username = a.usernameUtente " +
                "WHERE p.data = ? AND p.idStato = 1 " +
                "ORDER BY p.orario";

        List<Agenda> prenotazioni = new ArrayList<>();

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

                    Agenda result = new Agenda(prenotazioneData, orario, nomeStato, chipAnimale, nomeAnimale, nomeProprietario);
                    prenotazioni.add(result);
                }
            }catch (SQLException e) {
                throw new SQLException("Errore durante la lettura delle prenotazioni: " + e.getMessage(), e);
            }
        }
        return prenotazioni;
    }

    public List<Agenda> readVisiteToday(Date data) throws SQLException, ClassNotFoundException {
        String query = "SELECT p.data, p.orario, p.chipAnimale, p.chipAnimale, " +
                "s.nomeStato, " +
                "a.nome AS nomeAnimale, " +
                "u.nome, u.cognome " +
                "FROM prenotazione p " +
                "LEFT JOIN stato s ON s.idStato = p.idStato " +
                "LEFT JOIN animale a ON a.chip = p.chipAnimale " +
                "LEFT JOIN utente u ON u.username = a.usernameUtente " +
                "WHERE p.data = ? AND p.idStato >= 2 " +
                "ORDER BY p.orario";

        List<Agenda> prenotazioni = new ArrayList<>();

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

                    Agenda result = new Agenda(prenotazioneData, orario, nomeStato, chipAnimale, nomeAnimale, nomeProprietario);
                    prenotazioni.add(result);
                }
            }catch (SQLException e) {
                throw new SQLException("Errore durante la lettura delle prenotazioni: " + e.getMessage(), e);
            }
        }
        return prenotazioni;
    }

    public List<Agenda> readVaccinazioniYear(Date data) throws SQLException, ClassNotFoundException {
        String query = "SELECT p.data, p.orario, p.chipAnimale, p.chipAnimale, " +
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
        List<Agenda> prenotazioni = new ArrayList<>();

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

                    Agenda result = new Agenda(prenotazioneData, orario, nomeStato, chipAnimale, nomeAnimale, nomeProprietario);
                    prenotazioni.add(result);
                }
            }catch (SQLException e) {
                throw new SQLException("Errore durante la lettura delle prenotazioni: " + e.getMessage(), e);
            }
        }
        return prenotazioni;
    }
}
