package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entity.Animale;

public class AnimaleDAO {

    public void create(Animale animale) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO animale (chip, nome, tipo, razza, colore, dataNascita, usernameUtente) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, animale.getChip());
            stmt.setString(2, animale.getNome());
            stmt.setString(3, animale.getTipo());
            stmt.setString(4, animale.getRazza());
            stmt.setString(5, animale.getColore());
            stmt.setDate(6, new java.sql.Date(animale.getDataNascita().getTime()));
            stmt.setString(7, animale.getUsernameUtente());
            stmt.executeUpdate();
        }catch (SQLException e) {
            throw new SQLException("Inserimento fallito: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Driver JDBC non trovato: " + e.getMessage());
        }
    }

    public List<Animale> read(String nome) throws SQLException, ClassNotFoundException {
        List<Animale> animali = new ArrayList<>();

        String query = "SELECT a.chip, a.nome, a.tipo, a.colore, a.razza,  a.dataNascita, a.usernameUtente " +
                "FROM animale a " +
                "LEFT JOIN utente u ON u.username = a.usernameUtente " +
                "WHERE 1=1";

        boolean hasUser = nome != null && !nome.trim().isEmpty();

        if (hasUser) {
              query += " AND a.usernameUtente = ?"; // Modifica per cercare per username
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
                        rs.getString("tipo"),
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

    public void update(Animale animale) throws SQLException, ClassNotFoundException {
        String query = "UPDATE animale SET nome = ?, tipo = ?, razza = ?, colore = ?, dataNascita = ? WHERE chip = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, animale.getNome());
            stmt.setString(2, animale.getTipo());
            stmt.setString(3, animale.getRazza());
            stmt.setString(4, animale.getColore());
            stmt.setDate(5, new java.sql.Date(animale.getDataNascita().getTime()));
            stmt.setInt(6, animale.getChip());

        }catch (SQLException e) {
            throw new SQLException("Aggiornamento fallito : " + e.getMessage());
        }
    }

    public void delete(int chip) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM animale WHERE chip = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, chip);

        }catch (SQLException e) {
            throw new SQLException("Cancellazione fallita : " + e.getMessage());
        }
    }
}