package BackEnd;

import FrontEnd.CalorieBarChartTerminal;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class NutritionStatsService {
    public static class NutritionData {
        public String mealType;
        public int calories;
        public double fats;
        public double proteins;

        public NutritionData(String mealType, int calories, double fats, double proteins) {
            this.mealType = mealType;
            this.calories = calories;
            this.fats = fats;
            this.proteins = proteins;
        }
    }

    public List<NutritionData> getDailyNutritionStats(Date date) {
        List<NutritionData> list = new ArrayList<>();
        String query = """
            SELECT dm.meal_type, m.calories, m.fats, m.proteins
            FROM DailyMeals dm
            JOIN Meals m ON dm.meal_id = m.meal_id
           WHERE dm.date = ?
        """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, date);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("meal_type");
                int cal = rs.getInt("calories");
                double fats = rs.getDouble("fats");
                double proteins = rs.getDouble("proteins");
                list.add(new NutritionData(type, cal, fats, proteins));
            }



        } catch (Exception e) {
            System.err.println("⚠️ Failed to fetch nutrition stats: " + e.getMessage());
        }

        return list;
    }
}
