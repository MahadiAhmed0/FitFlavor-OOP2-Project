package BackEnd;

import java.sql.*;
import java.util.HashMap;

public class Authenticator {
    private static final String INSERT_USER = "INSERT INTO users (username, password, age, weight) VALUES (?, ?, ?, ?)";
    private static final String SELECT_USER = "SELECT * FROM users WHERE username = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";

    public boolean isUsernameTaken(String username) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error checking username: " + e.getMessage(), e);
        }
    }

    public boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    public void addUser(User user) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getAge());
            stmt.setDouble(4, user.getWeight());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding user: " + e.getMessage(), e);
        }
    }

    public User getUser(String username) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("age"),
                        rs.getDouble("weight")
                );
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user: " + e.getMessage(), e);
        }
    }

    public boolean authenticate(String username, String password) {
        User user = getUser(username);
        return user != null && user.checkPassword(password);
    }
}
