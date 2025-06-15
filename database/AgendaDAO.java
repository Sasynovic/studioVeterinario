//package database;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import entity.*;
//
//public class AgendaDAO {
//
//    public List<Agenda> VisualizzaAgenda(String username) throws SQLException, ClassNotFoundException {
//        List<Agenda> agendaItems = new ArrayList<>();
//
//        String query = "SELECT p.tipoVisita, p.descrizione, p.costo, p.chipAnimale, " +
//                "u.nome AS nomeVeterinario, u.cognome AS cognomeVeterinario, " +
//                "a.nome AS nomeAnimale, " +
//                "u2.nome AS nomeProprietario, u2.cognome AS cognomeProprietario, " +
//                "s.idslot, s.data, s.orario, s.disponibile, " +
//                "st.nomeStato "+
//
//                "FROM Prenotazione p " +
//
//                "LEFT JOIN Utente u ON p.usernameVeterinario = u.username " +
//                "LEFT JOIN Animale a ON p.chipAnimale = a.chip " +
//                "LEFT JOIN Utente u2 ON a.usernameUtente = u2.username " +
//                "LEFT JOIN Slot s ON p.idSlot = s.idslot " +
//                "LEFT JOIN Stato st ON st.idStato = p.idStato ";
//
//
//                if(username.trim().isEmpty() || username == null) {
//                    query += "ORDER BY s.data, s.orario";
//                }else{
//                     query += "WHERE a.usernameUtente = ?" +
//                                "ORDER BY s.data, s.orario";
//                }
//
//
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            conn = DBConnectionManager.getConnection();
//            stmt = conn.prepareStatement(query);
//            if(!username.trim().isEmpty()) {
//                // Non impostare il parametro se username è vuoto o nullo
//                stmt.setString(1, username);
//            }
//            rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                // ECCO COME POPOLARI TUTTI I DATI
//                Agenda agendaItem = new Agenda(
//                        rs.getString("tipoVisita"),
//                        rs.getString("descrizione"),
//                        rs.getDouble("costo"),
//                        rs.getString("chipAnimale"),
//                        rs.getString("nomeVeterinario"),
//                        rs.getString("cognomeVeterinario"),
//                        rs.getString("nomeAnimale"),
//                        rs.getString("nomeProprietario"),
//
//                );
//
//                agendaItems.add(agendaItem);
//            }
//
//        } finally {
//            if (rs != null) rs.close();
//            if (stmt != null) stmt.close();
//            DBConnectionManager.closeConnection(conn);
//        }
//
//        return agendaItems;
//    }
//
//}