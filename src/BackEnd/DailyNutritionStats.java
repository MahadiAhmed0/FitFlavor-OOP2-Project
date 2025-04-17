package BackEnd;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class DailyNutritionStats {

    private final Connection conn;

    public DailyNutritionStats() throws Exception {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public List<NutritionData> getTodayNutritionBreakdown() {
        List<NutritionData> list = new ArrayList<>();
        String sql = """
            SELECT dm.meal_type, m.calories, m.fats, m.proteins
            FROM DailyMeals dm
            JOIN Meals m ON dm.meal_id = m.meal_id
            WHERE dm.date = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = ps.executeQuery();

            Map<String, NutritionData> map = new HashMap<>();
            while (rs.next()) {
                String type = rs.getString("meal_type");
                int cal = rs.getInt("calories");
                float fats = rs.getFloat("fats");
                float prot = rs.getFloat("proteins");

                map.merge(type,
                        new NutritionData(type, cal, fats, prot),
                        (oldData, newData) -> new NutritionData(
                                type,
                                oldData.getCalories() + newData.getCalories(),
                                oldData.getFats() + newData.getFats(),
                                oldData.getProteins() + newData.getProteins()
                        )
                );
            }
            list.addAll(map.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public NutritionData getTotalDailyStats() {
        List<NutritionData> breakdown = getTodayNutritionBreakdown();
        int totalCal = 0;
        float totalFat = 0;
        float totalProt = 0;

        for (NutritionData n : breakdown) {
            totalCal += n.getCalories();
            totalFat += n.getFats();
            totalProt += n.getProteins();
        }
        return new NutritionData("Total", totalCal, totalFat, totalProt);
    }
}
