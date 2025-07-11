package database;

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
}