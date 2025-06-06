package config;

import java.sql.*;

public class DBConnectionManager {

    public static final String DB_URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "studiovet";
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String USERNAME = "salvatore";
    public static final String PASSWORD = "admin";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {

        Connection conn = null;
        Class.forName(DRIVER);

        conn = DriverManager.getConnection(DB_URL + DB_NAME, USERNAME, PASSWORD);

        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

        public static ResultSet selectQuery(String query) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    public static int updateQuery(String query) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        int rowsAffected = stmt.executeUpdate(query);
        closeConnection(conn);
        return rowsAffected;
    }
}