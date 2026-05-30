package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseManager() {
        this.url = "jdbc:postgresql://pg:5432/studs";
        this.username = "s501148";
        this.password = "mylab7password";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL драйвер не найден", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn.isValid(5);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}