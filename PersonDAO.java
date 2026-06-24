import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        String sql = "INSERT INTO people (first_name, last_name, email, phone_number, residential_area, person_role) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, person.getFirstName());
            statement.setString(2, person.getLastName());
            statement.setString(3, person.getEmail());
            statement.setString(4, person.getPhoneNumber());
            statement.setString(5, person.getResidentialArea());
            statement.setString(6, person.getPersonRole());

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

        String sql = "UPDATE people SET first_name = ?, last_name = ?, email = ?, phone_number = ?, residential_area = ?, person_role = ? "
                   + "WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, person.getFirstName());
            statement.setString(2, person.getLastName());
            statement.setString(3, person.getEmail());
            statement.setString(4, person.getPhoneNumber());
            statement.setString(5, person.getResidentialArea());
            statement.setString(6, person.getPersonRole());
            statement.setInt(7, person.getId());

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

    private Person mapRowToPerson(ResultSet resultSet) throws SQLException {
        return new Person(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("phone_number"),
                resultSet.getString("residential_area"),
                resultSet.getString("person_role")
        );
    }
}
