package BackEnd;

import java.sql.*;
import java.util.*;

public class WeeklyNutritionStats {
    private final Connection conn;

    public WeeklyNutritionStats() throws Exception {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public Map<String, List<NutritionData>> getWeekNutritionBreakdown() {
        Map<String, List<NutritionData>> map = new LinkedHashMap<>();
        String sql = """
            SELECT day_of_week, meal_type, calories, fats, proteins
            FROM WeeklyMeals wm
            JOIN Meals m ON wm.meal_id = m.meal_id
            ORDER BY FIELD(day_of_week, 'Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday')
        """;


        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String day = rs.getString("day_of_week");
                String type = rs.getString("meal_type");
                int cal = rs.getInt("calories");
                float fats = rs.getFloat("fats");
                float prot = rs.getFloat("proteins");

                map.computeIfAbsent(day, k -> new ArrayList<>())
                        .add(new NutritionData(type, cal, fats, prot));
            }

        } catch (SQLException e) {
            System.err.println("⚠️ Error fetching weekly breakdown: " + e.getMessage());
        }

        return map;
    }

    public Map<String, NutritionData> getTotalStatsPerDay() {
        Map<String, NutritionData> totals = new HashMap<>();
        Map<String, List<NutritionData>> daily = getWeekNutritionBreakdown();

        for (String day : daily.keySet()) {
            int totalCal = 0;
            float totalFat = 0;
            float totalProt = 0;

            for (NutritionData n : daily.get(day)) {
                totalCal += n.getCalories();
                totalFat += n.getFats();
                totalProt += n.getProteins();
            }

            totals.put(day, new NutritionData("Total", totalCal, totalFat, totalProt));
        }

        return totals;
    }
}
