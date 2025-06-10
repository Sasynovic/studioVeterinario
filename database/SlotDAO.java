package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entity.*;

public class SlotDAO {

    public List<Slot> VisualizzaSlot(Boolean disponibile) throws SQLException, ClassNotFoundException {
        List<Slot> slotList = new ArrayList<>();

        String query = "SELECT s.idslot, s.data, s.orario, s.disponibile " +
                       "FROM Slot s ";

        if (disponibile) {
            query += "WHERE s.disponibile = ? ";
        }

        query += "ORDER BY s.data, s.orario";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionManager.getConnection();
            stmt = conn.prepareStatement(query);
            if (disponibile) {
                stmt.setBoolean(1, true);
            }
            rs = stmt.executeQuery();

            while (rs.next()) {
                Slot slot = new Slot(
                    rs.getInt("idslot"),
                    rs.getDate("data"),
                    rs.getInt("orario"),
                    rs.getBoolean("disponibile")
                );
                slotList.add(slot);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return slotList;
    }

    public Slot inserisciSlot(Slot slot) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO Slot (data, orario, disponibile) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnectionManager.getConnection();
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setDate(1, new java.sql.Date(slot.getData().getTime()));
            stmt.setInt(2, slot.getOrario());
            stmt.setBoolean(3, slot.isDisponibile());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        slot.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return slot;
    }
}
