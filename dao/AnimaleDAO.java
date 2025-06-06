package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Animale;
import config.DBConnectionManager;

public class AnimaleDAO {

    public List<Animale> readAnimale(String usernameProprietario) throws SQLException, ClassNotFoundException {
        List<Animale> animali = new ArrayList<>();

        String query = "SELECT * FROM animale WHERE 1=1";
        boolean hasUser = usernameProprietario != null;

        if (hasUser) {
            query += " AND usernameUtente = ?";
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionManager.getConnection();
            stmt = conn.prepareStatement(query);

            if (hasUser) {
                stmt.setString(1, usernameProprietario);
            }

            rs = stmt.executeQuery();

            while (rs.next()) {
                Animale a = new Animale(
                        rs.getInt("chip"),
                        rs.getString("nome"),
                        rs.getString("razza"),
                        rs.getString("colore"),
                        rs.getString("dataNascita"),
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
}
