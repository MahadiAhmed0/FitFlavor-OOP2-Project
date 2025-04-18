package BackEnd;

import java.sql.*;

public class WeightLossPredictor {
    private final String username;
    private final Connection conn;

    public WeightLossPredictor(String username) throws SQLException {
        this.username = username;
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public User getUserData() throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        }
        return null;
    }

    public double calculateBMR(User user) {
        
        return 10 * user.getWeight() + 6.25 * 170 - 5 * user.getAge() + 5; 
    }

    public double calculateBMI(User user) {
        double heightInMeters = 1.70; 
        return user.getWeight() / (heightInMeters * heightInMeters);
    }

    public double calculateIdealWeight(User user) {
        double heightInMeters = 1.70; 
        double idealBMI = 22; 
        return idealBMI * (heightInMeters * heightInMeters);
    }

    public int getWeeklyCaloriesFromMeals() throws SQLException {
        String sql = "SELECT SUM(m.calories) AS total_calories " +
                "FROM WeeklyMeals w JOIN Meals m ON w.meal_id = m.meal_id " +
                "WHERE w.week_start_date = (SELECT MAX(week_start_date) FROM WeeklyMeals)";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total_calories");
            }
        }
        return 0;
    }

    public double calculateWeeklyCaloricBurn(User user, double exerciseHours) {
        double exerciseCaloriesBurned = exerciseHours * 500; 
        double weeklyBMR = calculateBMR(user) * 7;
        return weeklyBMR + exerciseCaloriesBurned;
    }

    public double estimateWeightLoss(User user, double exerciseHours) throws SQLException {
        int intake = getWeeklyCaloriesFromMeals();
        double burned = calculateWeeklyCaloricBurn(user, exerciseHours);
        double deficit = burned - intake;
        return deficit / 7700.0; 
    }
}
