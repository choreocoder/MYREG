package dao;

import models.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

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

    // RECORD SINGLE: Inserts a clean row linking a single person to a single event
    public void recordAttendance(int eventId, int personId, String status) throws SQLException {
        String sql = "INSERT INTO attendance (event_id, person_id, status, marked_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, eventId);
            statement.setInt(2, personId);
            statement.setString(3, status);

            statement.executeUpdate();
        }
    }

    // READ: Returns a list of all person_ids who checked into a specific meeting
    public List<Integer> getPresentAttendeesForEvent(int eventId) throws SQLException {
        String sql = "SELECT person_id FROM attendance WHERE event_id = ? AND status = 'PRESENT'";
        List<Integer> presentIds = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    presentIds.add(resultSet.getInt("person_id"));
                }
            }
        }
        return presentIds;
    }

    // BATCH RECORD: Performance optimized via JDBC batching for checking off rows simultaneously
    public void batchRecordAttendance(List<AttendanceRecord> records) throws SQLException {
        if (records == null || records.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO attendance (event_id, person_id, status, marked_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Turn off auto-commit to run everything as a single transaction block
            connection.setAutoCommit(false);

            for (AttendanceRecord record : records) {
                statement.setInt(1, record.getEventId());
                statement.setInt(2, record.getPersonId());
                statement.setString(3, record.getStatus());
                statement.addBatch(); // Stages the query in memory
            }

            statement.executeBatch(); // Pushes all records across the network at once
            connection.commit();      // Saves changes securely to disk

        } catch (SQLException e) {
            throw e;
        }
    }
}