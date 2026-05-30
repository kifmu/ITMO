package server;

import common.models.*;
import java.sql.*;
import java.util.*;

public class PersonDAO {
    private final DatabaseManager dbManager;

    public PersonDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public int insert(Person person, int ownerId) throws SQLException {
        String sql = """
            INSERT INTO persons (
                name, coordinates_x, coordinates_y, creation_date,
                height, passport_id, eye_color, nationality,
                location_x, location_y, location_name, owner_id
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, person.getName());
            stmt.setFloat(2, person.getCoordinates().getX());
            stmt.setDouble(3, person.getCoordinates().getY());
            stmt.setTimestamp(4, new Timestamp(person.getCreationDate().getTime()));
            stmt.setInt(5, person.getHeight());
            stmt.setString(6, person.getPassportID());
            stmt.setString(7, person.getEyeColor() != null ? person.getEyeColor().name() : null);
            stmt.setString(8, person.getNationality().name());
            stmt.setFloat(9, person.getLocation().getX());
            stmt.setLong(10, person.getLocation().getY());
            stmt.setString(11, person.getLocation().getName());
            stmt.setInt(12, ownerId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("Не удалось получить ID");
            }
        }
    }

    public void update(Person person, int personId) throws SQLException {
        String sql = """
            UPDATE persons SET
                name = ?, coordinates_x = ?, coordinates_y = ?,
                height = ?, passport_id = ?, eye_color = ?,
                nationality = ?, location_x = ?, location_y = ?,
                location_name = ?
            WHERE id = ?
        """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, person.getName());
            stmt.setFloat(2, person.getCoordinates().getX());
            stmt.setDouble(3, person.getCoordinates().getY());
            stmt.setInt(4, person.getHeight());
            stmt.setString(5, person.getPassportID());
            stmt.setString(6, person.getEyeColor() != null ? person.getEyeColor().name() : null);
            stmt.setString(7, person.getNationality().name());
            stmt.setFloat(8, person.getLocation().getX());
            stmt.setLong(9, person.getLocation().getY());
            stmt.setString(10, person.getLocation().getName());
            stmt.setInt(11, personId);

            stmt.executeUpdate();
        }
    }

    public void delete(int personId) throws SQLException {
        String sql = "DELETE FROM persons WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personId);
            stmt.executeUpdate();
        }
    }

    public boolean checkOwnership(int personId, int userId) throws SQLException {
        String sql = "SELECT owner_id FROM persons WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("owner_id") == userId;
            }
            return false;
        }
    }
    public List<Person> loadAll() throws SQLException {
        String sql = "SELECT * FROM persons ORDER BY id";
        List<Person> persons = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Person person = new Person();

                person.setId(rs.getInt("id"));
                person.setName(rs.getString("name"));

                Coordinates coords = new Coordinates(
                        rs.getFloat("coordinates_x"),
                        rs.getDouble("coordinates_y")
                );
                person.setCoordinates(coords);
                person.setCreationDate(new java.util.Date(rs.getTimestamp("creation_date").getTime()));

                person.setHeight(rs.getInt("height"));
                person.setPassportID(rs.getString("passport_id"));

                String eyeColorStr = rs.getString("eye_color");
                if (eyeColorStr != null) {
                    person.setEyeColor(Color.valueOf(eyeColorStr));
                }

                person.setNationality(Country.valueOf(rs.getString("nationality")));

                Location location = new Location(
                        rs.getFloat("location_x"),
                        rs.getLong("location_y"),
                        rs.getString("location_name")
                );
                person.setLocation(location);

                person.setOwnerId(rs.getInt("owner_id"));

                persons.add(person);
            }
        }

        return persons;
    }
    public int deleteAllByOwner(int ownerId) throws SQLException {
        String sql = "DELETE FROM persons WHERE owner_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ownerId);
            return stmt.executeUpdate();
        }
    }
}