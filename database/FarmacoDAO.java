package database;

import entity.Agenda;
import entity.Farmaco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FarmacoDAO {

    public int create(Farmaco f) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO farmaco (nome, produttore) VALUES (?, ?)";

        if (f.getNome() == null || f.getNome().isEmpty() ||
                f.getProduttore() == null || f.getProduttore().isEmpty()) {
            throw new SQLException("Tutti i campi sono obbligatori.");
        }

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getProduttore());

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

    public List<Farmaco> read(){
        String query = "SELECT id, nome, produttore FROM farmaco";
        List<Farmaco> farmaci = new ArrayList<>();

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Farmaco result = new Farmaco(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("produttore")
                    );
                    farmaci.add(result);
                }
            }catch (SQLException e) {
                throw new SQLException("Lettura fallita farmaci fallita: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return farmaci;
    }

    public void associaFarmacoVisita(int idFarmaco, int idVisita){
        String query = "INSERT INTO impiega (idFarmaco, idVisita) VALUES (?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idFarmaco);
            stmt.setInt(2, idVisita);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Associazione fallita, nessuna riga modificata.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Associazione farmaco-visita fallita: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC non trovato: " + e.getMessage());
        }
    }

}
