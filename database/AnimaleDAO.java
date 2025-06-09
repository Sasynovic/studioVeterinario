package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entity.Animale;

public class AnimaleDAO {

    public List<Animale> visualizzaAnimali(String nome) throws SQLException, ClassNotFoundException {
        List<Animale> animali = new ArrayList<>();

        String query = "SELECT a.chip, a.nome, a.razza, a.colore, a.dataNascita, a.usernameUtente,u.nome , u.cognome " +
                "FROM animale a " +
                "LEFT JOIN utente u ON u.username = a.usernameUtente " +
                "WHERE 1=1";

        boolean hasUser = nome != null && !nome.trim().isEmpty();

        if (hasUser) {
//            query += " AND LOWER(CONCAT(TRIM(u.nome), TRIM(u.cognome))) LIKE ?";
              query += " AND LOWER(a.usernameUtente) = ?"; // Modifica per cercare per username
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionManager.getConnection();
            stmt = conn.prepareStatement(query);

            if (hasUser) {
                stmt.setString(1, nome);
            }

            rs = stmt.executeQuery();

            while (rs.next()) {
                Animale a = new Animale(
                        rs.getInt("chip"),
                        rs.getString("nome"),
                        rs.getString("razza"),
                        rs.getString("colore"),
                        rs.getDate("dataNascita"),
                        rs.getString("usernameUtente")
                );
                animali.add(a);
            }

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBConnectionManager.closeConnection(conn);
        }

        return animali;
    }

    public Animale inserisciAnimale(Animale animale) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO animale (chip, nome, razza, colore, dataNascita, usernameUtente) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, animale.getChip());
            stmt.setString(2, animale.getNome());
            stmt.setString(3, animale.getRazza());
            stmt.setString(4, animale.getColore());
            stmt.setDate(5, new java.sql.Date(animale.getDataNascita().getTime()));
            stmt.setString(6, animale.getUsernameUtente());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return animale; // Ritorna l'animale inserito
            } else {
                throw new SQLException("Inserimento fallito per l'animale: " + animale.getNome());
            }
        }
    }

    public void cancellaAnimale(int chip) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM animale WHERE chip = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, chip);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Nessun animale trovato con il chip: " + chip);
            }
        }
    }


}
