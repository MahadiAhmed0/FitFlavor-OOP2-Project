package BackEnd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HealthTipManager {

    public String getRandomHealthTip() {
        List<HealthTip> healthTips = loadHealthTipsFromDatabase();
        if (healthTips.isEmpty()) {
            return "No health tips available at the moment.";
        }

        Random random = new Random();
        HealthTip randomTip = healthTips.get(random.nextInt(healthTips.size()));
        return randomTip.toString();
    }

    private List<HealthTip> loadHealthTipsFromDatabase() {
        List<HealthTip> healthTips = new ArrayList<>();
        String query = "SELECT * FROM HealthTips";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int tipId = rs.getInt("tip_id");
                String tipText = rs.getString("tip_text");
                healthTips.add(new HealthTip(tipId, tipText));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching health tips: " + e.getMessage());
        }

        return healthTips;
    }
}
