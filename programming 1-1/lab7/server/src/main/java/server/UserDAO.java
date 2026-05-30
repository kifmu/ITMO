package server;

import java.sql.*;

public class UserDAO {
    private final DatabaseManager dbManager;

    public UserDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean register(String username, String passwordHash) throws SQLException {
        if (userExists(username)) {
            return false;
        }
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            stmt.executeUpdate();
            return true;
        }
    }

    public int authenticate(String username, String passwordHash) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ? AND password_hash = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1;
            }
        }
    }

    public boolean userExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public int getUserId(String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
            return -1;
        }
    }
}