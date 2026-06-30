package dao;

import models.Person;
import models.Visitor;
import models.Leader;
import models.FollowUpStatus;
import models.LeaderTier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {

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

    public Person getPersonById(int id) throws SQLException {
        String sql = "SELECT * FROM people WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapRowToPerson(resultSet) : null;
            }
        }
    }

    public List<Person> getAllPeople() throws SQLException {
        String sql = "SELECT * FROM people";
        List<Person> people = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                people.add(mapRowToPerson(resultSet));
            }
        }
        return people;
    }

    public int addPerson(Person person) throws SQLException {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }

        // Expanded SQL statement to handle all specialized role tracking columns
        String sql = "INSERT INTO people (first_name, last_name, email, phone_number, residential_area, person_role, "
                + "first_time_visit_date, invited_by_id, follow_up_status, leader_tier) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, person.getFirstName());
            statement.setString(2, person.getLastName());
            statement.setString(3, person.getEmail());
            statement.setString(4, person.getPhoneNumber());
            statement.setString(5, person.getResidentialArea());
            statement.setString(6, person.getPersonRole());

            // Polymorphic handling based on what class type is passed into the DAO
            if (person instanceof Visitor) {
                Visitor visitor = (Visitor) person;

                if (visitor.getFirstTimeVisitDate() != null) {
                    statement.setDate(7, java.sql.Date.valueOf(visitor.getFirstTimeVisitDate()));
                } else {
                    statement.setNull(7, Types.DATE);
                }

                if (visitor.getInvitedById() > 0) {
                    statement.setInt(8, visitor.getInvitedById());
                } else {
                    statement.setNull(8, Types.INTEGER);
                }

                statement.setString(9, visitor.getFollowUpStatus() != null ? visitor.getFollowUpStatus().name() : null);
                statement.setNull(10, Types.VARCHAR); // No leader tier for a visitor

            } else if (person instanceof Leader) {
                Leader leader = (Leader) person;

                statement.setNull(7, Types.DATE);
                statement.setNull(8, Types.INTEGER);
                statement.setNull(9, Types.VARCHAR);
                statement.setString(10, leader.getLeaderTier() != null ? leader.getLeaderTier().name() : null);

            } else {
                // If it's a plain base Person object, set all specialized columns to null
                statement.setNull(7, Types.DATE);
                statement.setNull(8, Types.INTEGER);
                statement.setNull(9, Types.VARCHAR);
                statement.setNull(10, Types.VARCHAR);
            }

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating person failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    person.setId(generatedId);
                    return generatedId;
                } else {
                    throw new SQLException("Creating person failed, no ID obtained.");
                }
            }
        }
    }

    public boolean updatePerson(Person person) throws SQLException {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }

        String sql = "UPDATE people SET first_name = ?, last_name = ?, email = ?, phone_number = ?, residential_area = ?, person_role = ?, "
                + "first_time_visit_date = ?, invited_by_id = ?, follow_up_status = ?, leader_tier = ? "
                + "WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, person.getFirstName());
            statement.setString(2, person.getLastName());
            statement.setString(3, person.getEmail());
            statement.setString(4, person.getPhoneNumber());
            statement.setString(5, person.getResidentialArea());
            statement.setString(6, person.getPersonRole());

            if (person instanceof Visitor) {
                Visitor visitor = (Visitor) person;
                statement.setDate(7, visitor.getFirstTimeVisitDate() != null ? java.sql.Date.valueOf(visitor.getFirstTimeVisitDate()) : null);
                statement.setInt(8, visitor.getInvitedById());
                statement.setString(9, visitor.getFollowUpStatus() != null ? visitor.getFollowUpStatus().name() : null);
                statement.setNull(10, Types.VARCHAR);
            } else if (person instanceof Leader) {
                Leader leader = (Leader) person;
                statement.setNull(7, Types.DATE);
                statement.setNull(8, Types.INTEGER);
                statement.setNull(9, Types.VARCHAR);
                statement.setString(10, leader.getLeaderTier() != null ? leader.getLeaderTier().name() : null);
            } else {
                statement.setNull(7, Types.DATE);
                statement.setNull(8, Types.INTEGER);
                statement.setNull(9, Types.VARCHAR);
                statement.setNull(10, Types.VARCHAR);
            }

            statement.setInt(11, person.getId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean deletePerson(int id) throws SQLException {
        String sql = "DELETE FROM people WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    // Explicitly reconstructs specific subclasses based on the role column value
    private Person mapRowToPerson(ResultSet resultSet) throws SQLException {
        String role = resultSet.getString("person_role");
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String phoneNumber = resultSet.getString("phone_number");
        String residentialArea = resultSet.getString("residential_area");

        if ("VISITOR".equalsIgnoreCase(role)) {
            java.sql.Date sqlDate = resultSet.getDate("first_time_visit_date");
            LocalDate visitDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;
            int invitedBy = resultSet.getInt("invited_by_id");

            String statusStr = resultSet.getString("follow_up_status");
            FollowUpStatus status = (statusStr != null) ? FollowUpStatus.valueOf(statusStr) : FollowUpStatus.PENDING;

            return new Visitor(id, firstName, lastName, email, phoneNumber, residentialArea, role, visitDate, invitedBy, status);

        } else if ("LEADER".equalsIgnoreCase(role)) {
            String tierStr = resultSet.getString("leader_tier");
            LeaderTier tier = (tierStr != null) ? LeaderTier.valueOf(tierStr) : LeaderTier.TRAINEE;

            return new Leader(id, firstName, lastName, email, phoneNumber, residentialArea, role, tier);
        }

        // Default structural fallback to a base Person object
        return new Person(id, firstName, lastName, email, phoneNumber, residentialArea, role);
    }
}