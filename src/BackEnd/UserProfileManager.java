package BackEnd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileManager {

    public User getUserDetails(String username) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
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
        } catch (SQLException e) {
            System.err.println("Error retrieving user details: " + e.getMessage());
        }
        return null;
    }

    public boolean updateUser(String username, String field, Object newValue) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "UPDATE users SET " + field + " = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            if (newValue instanceof String)
                stmt.setString(1, (String) newValue);
            else if (newValue instanceof Integer)
                stmt.setInt(1, (Integer) newValue);
            else if (newValue instanceof Double)
                stmt.setDouble(1, (Double) newValue);

            stmt.setString(2, username);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
}
