package database;

import entity.Visita;

import java.sql.*;

public class VisitaDAO {

    public int create(Visita v) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO visita (tipoVisita, descrizione, costo, usernameVeterinario) VALUES (?, ?, ?, ?)";

        if( v.getTipoVisita() == null || v.getTipoVisita().isEmpty() ||
                v.getDescrizione() == null || v.getDescrizione().isEmpty() ||
                v.getCosto() <= 0 ||
                v.getUsernameVet() == null || v.getUsernameVet().isEmpty()) {
            throw new SQLException("Tutti i campi sono obbligatori.");
        }

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, v.getTipoVisita());
            stmt.setString(2, v.getDescrizione());
            stmt.setDouble(3, v.getCosto());
            stmt.setString(4, v.getUsernameVet());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserimento fallito, nessuna riga modificata.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Restituisce l'ID del farmaco inserito
                } else {
                    throw new SQLException("Inserimento fallito: nessun ID ottenuto.");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Inserimento fallito: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Driver JDBC non trovato: " + e.getMessage());
        }
    }
}
