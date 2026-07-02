package dao;

import models.DiscipleshipGroup;
import models.GroupType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DiscipleshipGroupDAO {

    // Mirroring your PersonDAO database configurations
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

    // 1. GET GROUP BY ID
    public DiscipleshipGroup getGroupById(int id) throws SQLException {
        String sql = "SELECT * FROM discipleship_groups WHERE group_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapRowToGroup(resultSet) : null;
            }
        }
    }

    // 2. GET ALL GROUPS
    public List<DiscipleshipGroup> getAllGroups() throws SQLException {
        String sql = "SELECT * FROM discipleship_groups";
        List<DiscipleshipGroup> groups = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                groups.add(mapRowToGroup(resultSet));
            }
        }
        return groups;
    }

    // 3. ADD NEW GROUP
    public int addGroup(DiscipleshipGroup group) throws SQLException {
        if (group == null) {
            throw new IllegalArgumentException("Discipleship group cannot be null");
        }

        String sql = "INSERT INTO discipleship_groups (group_name, residential_area, leader_id, group_type) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, group.getGroupName());
            statement.setString(2, group.getResidentialArea());
            statement.setInt(3, group.getLeaderId()); // Relates to a Leader's Person ID
            statement.setString(4, group.getGroupType() != null ? group.getGroupType().name() : GroupType.MIXED.name());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating group failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    // Since your model constructor sets the ID and doesn't have a direct setter,
                    // you can optionally add a setGroupId() to your model if you want to update it in memory here.
                    return generatedId;
                } else {
                    throw new SQLException("Creating group failed, no ID obtained.");
                }
            }
        }
    }

    // 4. UPDATE GROUP
    public boolean updateGroup(DiscipleshipGroup group) throws SQLException {
        if (group == null) {
            throw new IllegalArgumentException("Discipleship group cannot be null");
        }

        String sql = "UPDATE discipleship_groups SET group_name = ?, residential_area = ?, leader_id = ?, group_type = ? WHERE group_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, group.getGroupName());
            statement.setString(2, group.getResidentialArea());
            statement.setInt(3, group.getLeaderId());
            statement.setString(4, group.getGroupType() != null ? group.getGroupType().name() : GroupType.MIXED.name());
            statement.setInt(5, group.getGroupId());

            return statement.executeUpdate() > 0;
        }
    }

    // 5. DELETE GROUP
    public boolean deleteGroup(int id) throws SQLException {
        String sql = "DELETE FROM discipleship_groups WHERE group_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    // Explicitly reconstructs the DiscipleshipGroup object from the DB row
    private DiscipleshipGroup mapRowToGroup(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("group_id");
        String name = resultSet.getString("group_name");
        String area = resultSet.getString("residential_area");
        int leaderId = resultSet.getInt("leader_id");

        DiscipleshipGroup group = new DiscipleshipGroup(id, name, area, leaderId);

        String typeStr = resultSet.getString("group_type");
        if (typeStr != null) {
            group.setGroupType(GroupType.valueOf(typeStr.toUpperCase()));
        }

        return group;
    }
}