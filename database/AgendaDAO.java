// ===== DAO AGGIORNATA =====
package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entity.*;

public class AgendaDAO {

    public List<Agenda> VisualizzaAgendaUser(String username) throws SQLException, ClassNotFoundException {
        List<Agenda> agendaItems = new ArrayList<>();

        String query = "SELECT p.tipoVisita, p.descrizione, p.costo, p.chipAnimale, " +
                "u.nome AS nomeVeterinario, u.cognome AS cognomeVeterinario, " +
                "a.nome AS nomeAnimale, " +
                "u2.nome AS nomeProprietario, u2.cognome AS cognomeProprietario, " +
                "s.idslot, s.data, s.orario, s.disponibile " +

                "FROM Prenotazione p " +

                "LEFT JOIN Utente u ON p.usernameVeterinario = u.username " +
                "LEFT JOIN Animale a ON p.chipAnimale = a.chip " +
                "LEFT JOIN Utente u2 ON a.usernameUtente = u2.username " +
                "LEFT JOIN Slot s ON p.idSlot = s.idslot " +

                "WHERE a.usernameUtente = ? " +
                "ORDER BY s.data, s.orario";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionManager.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            while (rs.next()) {
                // ECCO COME POPOLARI TUTTI I DATI
                Agenda agendaItem = new Agenda(
                        rs.getString("tipoVisita"),
                        rs.getString("descrizione"),
                        rs.getDouble("costo"),
                        rs.getString("chipAnimale"),
                        rs.getString("nomeVeterinario"),
                        rs.getString("cognomeVeterinario"),
                        rs.getString("nomeAnimale"),
                        rs.getString("nomeProprietario"),
                        rs.getString("cognomeProprietario"),
                        rs.getString("idslot"),
                        rs.getDate("data").toLocalDate(),
                        rs.getInt("orario"),
                        rs.getBoolean("disponibile")
                );

                agendaItems.add(agendaItem);
            }

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBConnectionManager.closeConnection(conn);
        }

        return agendaItems;
    }

    public void stampaAgendaUser(String username) throws SQLException, ClassNotFoundException {
        List<Agenda> agenda = VisualizzaAgendaUser(username);

        System.out.println("=== AGENDA UTENTE: " + username + " ===");
        if (agenda.isEmpty()) {
            System.out.println("Nessuna prenotazione trovata.");
            return;
        }

        for (Agenda item : agenda) {
            System.out.println("──────────────────────────────────────────");
            System.out.println("Tipo Visita: " + item.getTipoVisita());
            System.out.println("Animale: " + item.getNomeAnimale() + " (Chip: " + item.getChipAnimale() + ")");
            System.out.println("Veterinario: Dr. " + item.getNomeVeterinario() + " " + item.getCognomeVeterinario());
            System.out.println("Data e Ora: " + item.getData() + " alle " + item.getOrario());
            System.out.println("Costo: €" + String.format("%.2f", item.getCosto()));
            System.out.println("Descrizione: " + item.getDescrizione());
            System.out.println("Proprietario: " + item.getNomeProprietario() + " " + item.getCognomeProprietario());
        }
        System.out.println("──────────────────────────────────────────");
        System.out.println("Totale prenotazioni: " + agenda.size());
    }
}