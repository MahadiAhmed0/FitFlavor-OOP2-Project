package BackEnd;

import java.sql.*;
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
        if (areDailyMealsEmpty()) { // Check if the DailyMeals table is empty
            System.out.println("🍽️ Daily meals table is empty. Generating new meals...");
            generateAndSaveDailyMeals();
        } else {
            System.out.println("🍽️ Daily meals already exist in the database. Fetching meals...");
            fetchAndDisplayDailyMeals();
        }
    }

    private boolean areDailyMealsEmpty() {
        String query = "SELECT COUNT(*) AS meal_count FROM DailyMeals";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt("meal_count") == 0; // Check if there are no rows
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error checking DailyMeals table: " + e.getMessage());
        }
        return true; // Assume empty if error occurs
    }

    private void generateAndSaveDailyMeals() {
        String[] types = {"breakfast", "lunch", "dinner", "snack"};
        Map<String, List<Meal>> dailyMeals = new LinkedHashMap<>();
        Random random = new Random();

        for (String type : types) {
            List<Meal> selected = new ArrayList<>();
            while (selected.size() < 1) {
                Meal meal = allMeals.get(random.nextInt(allMeals.size())); // Random meal selection
                if (!selected.contains(meal)) selected.add(meal); // Ensure no duplicates
            }
            dailyMeals.put(type, selected);
            storageManager.saveMealsToDatabase(type, selected); // Save meals to the database
        }

        showMeals(dailyMeals);
    }

    private void fetchAndDisplayDailyMeals() {
        // Retrieve daily meals from the database grouped by meal type
        String query = "SELECT dm.meal_type, m.* FROM DailyMeals dm " +
                "JOIN Meals m ON dm.meal_id = m.meal_id ORDER BY dm.meal_type";
        Map<String, List<Meal>> dailyMeals = new LinkedHashMap<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String mealType = rs.getString("meal_type");
                Meal meal = Meal.fromResultSet(rs); // Create Meal object
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