package BackEnd;


import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class MealRecommendationSystem {
    private final MealLoader mealLoader;
    private final MealStorageManager storageManager;
    private final List<Meal> allMeals;

    public MealRecommendationSystem() {
        mealLoader = new MealLoader();
        allMeals = mealLoader.loadMeals();
        storageManager = new MealStorageManager();
    }

    public void suggestDailyMeals() {
        if (isNewDateOrEmpty()) {
            System.out.println("📅 New day detected or no meals found. Generating meals for today...");
            clearOldDailyMeals();
            generateAndSaveDailyMeals();
        } else {
            System.out.println("🍽️ Daily meals already exist for today. Fetching meals...");
            fetchAndDisplayDailyMeals();
        }
    }

    private boolean isNewDateOrEmpty() {
        String query = "SELECT COUNT(*) AS meal_count FROM DailyMeals WHERE date = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("meal_count") == 0;
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error checking DailyMeals for today's date: " + e.getMessage());
        }
        return true; // Assume empty if error occurs
    }

    private void clearOldDailyMeals() {
        String query = "DELETE FROM DailyMeals";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
            System.out.println("🧹 Old daily meals cleared.");
        } catch (SQLException e) {
            System.out.println("⚠️ Error clearing DailyMeals: " + e.getMessage());
        }
    }

    private void generateAndSaveDailyMeals() {
        String[] types = {"breakfast", "lunch", "dinner", "snack"};
        Map<String, List<Meal>> dailyMeals = new LinkedHashMap<>();
        Random random = new Random();

        for (String type : types) {
            List<Meal> selected = new ArrayList<>();
            while (selected.size() < 1) {
                Meal meal = allMeals.get(random.nextInt(allMeals.size()));
                if (!selected.contains(meal)) selected.add(meal);
            }
            dailyMeals.put(type, selected);
            storageManager.saveMealsToDatabase(type, selected); // Include date in insertion
        }

        showMeals(dailyMeals);
    }

    private void fetchAndDisplayDailyMeals() {
        String query = "SELECT dm.meal_type, m.* FROM DailyMeals dm " +
                "JOIN Meals m ON dm.meal_id = m.meal_id " +
                "WHERE dm.date = ? ORDER BY dm.meal_type";
        Map<String, List<Meal>> dailyMeals = new LinkedHashMap<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String mealType = rs.getString("meal_type");
                Meal meal = Meal.fromResultSet(rs);
                dailyMeals.putIfAbsent(mealType, new ArrayList<>());
                dailyMeals.get(mealType).add(meal);
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching daily meals: " + e.getMessage());
        }

        showMeals(dailyMeals);
    }

    private void showMeals(Map<String, List<Meal>> dailyMeals) {
        dailyMeals.forEach((type, meals) -> {
            System.out.println("\n🍴 " + type.toUpperCase());
            meals.forEach(System.out::println);
        });
    }
}
