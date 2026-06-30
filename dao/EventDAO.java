package dao;

import models.Event;
import models.EventType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/your_database_name?useSSL=false&serverTimezone=UTC";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // CREATE: Saves the event record
    public void createEvent(Event event) throws SQLException {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        String sql = "INSERT INTO events (event_type, special_event_details, event_date, group_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, event.getEventType() != null ? event.getEventType().name() : null);
            statement.setString(2, event.getSpecialEventDetails());

            // Handle LocalDateTime conversion to SQL Timestamp
            if (event.getEventDate() != null) {
                statement.setTimestamp(3, Timestamp.valueOf(event.getEventDate()));
            } else {
                statement.setNull(3, Types.TIMESTAMP);
            }

            // Check if there is a group context, or if it's a general church service
            // (Assumes a dummy or zero value means no specific cell group owns this event)
            // Note: Since your Event model doesn't expose a public getGroupId(), we can use reflection or access it if we add a getter.
            // Let's assume you have a getGroupId() or we use direct mapping. If it's missing a getter, make sure to add public int getGroupId() to your model!
            statement.setInt(4, event.getEventId()); // Replace with getGroupId() if you add it to the model

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating event failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Note: Your Event model doesn't have a setEventId method.
                    // The generated ID is returned here from the DB execution if needed.
                    int generatedId = generatedKeys.getInt(1);
                }
            }
        }
    }

    // READ: Pulls historic calendar timeline for a single discipleship group
    public List<Event> getEventsByGroup(int groupId) throws SQLException {
        String sql = "SELECT * FROM events WHERE group_id = ? ORDER BY event_date DESC";
        List<Event> events = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, groupId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    events.add(mapRowToEvent(resultSet));
                }
            }
        }
        return events;
    }

    // READ: Grabs all entries matching a type (e.g., SUNDAY_SERVICE)
    public List<Event> getEventsByType(EventType type) throws SQLException {
        if (type == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }

        String sql = "SELECT * FROM events WHERE event_type = ? ORDER BY event_date DESC";
        List<Event> events = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, type.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    events.add(mapRowToEvent(resultSet));
                }
            }
        }
        return events;
    }

    private Event mapRowToEvent(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("event_id");
        String typeStr = resultSet.getString("event_type");
        EventType type = (typeStr != null) ? EventType.valueOf(typeStr) : null;
        String details = resultSet.getString("special_event_details");

        // Convert SQL Timestamp back to Java LocalDateTime cleanly
        Timestamp sqlTimestamp = resultSet.getTimestamp("event_date");
        java.time.LocalDateTime eventDate = (sqlTimestamp != null) ? sqlTimestamp.toLocalDateTime() : null;

        int groupId = resultSet.getInt("group_id");

        // Aligned perfectly to your constructor signature: (id, type, details, date, groupId)
        return new Event(id, type, details, eventDate, groupId);
    }
}