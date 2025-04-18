package BackEnd;


import java.sql.*;
import java.sql.Date;
import java.time.DayOfWeek;
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

    public void suggestWeeklyMeals() {
        if (isNewWeekOrEmpty()) {
            System.out.println("📅 New week detected or no weekly meals found. Generating weekly plan...");
            clearOldWeeklyMeals();
            generateAndSaveWeeklyMeals();
        } else {
            System.out.println("🍽️ Weekly meals already exist for this week. Fetching...");
            fetchAndDisplayWeeklyMeals();
        }
    }

    private void fetchAndDisplayWeeklyMeals() {
        String query = "SELECT wm.day_of_week, wm.meal_type, m.* FROM WeeklyMeals wm " +
                "JOIN Meals m ON wm.meal_id = m.meal_id " +
                "WHERE wm.week_start_date = ? ORDER BY wm.day_of_week, wm.meal_type";

        Map<String, Map<String, Meal>> weeklyMeals = new LinkedHashMap<>();
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(startOfWeek));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String day = rs.getString("day_of_week");
                String type = rs.getString("meal_type");
                Meal meal = Meal.fromResultSet(rs);

                weeklyMeals.putIfAbsent(day, new LinkedHashMap<>());
                weeklyMeals.get(day).put(type, meal);
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching weekly meals: " + e.getMessage());
        }

        
        for (String day : weeklyMeals.keySet()) {
            System.out.println("\n📅 " + day);
            weeklyMeals.get(day).forEach((type, meal) -> {
                System.out.println("🍴 " + type + ": " + meal);
            });
        }
    }


    private boolean isNewWeekOrEmpty() {
        String query = "SELECT COUNT(*) AS meal_count FROM WeeklyMeals WHERE week_start_date = ?";
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(startOfWeek));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("meal_count") == 0;
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error checking WeeklyMeals for current week: " + e.getMessage());
        }
        return true;
    }

    private void clearOldWeeklyMeals() {
        String query = "DELETE FROM WeeklyMeals";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
            System.out.println("🧹 Old weekly meals cleared.");
        } catch (SQLException e) {
            System.out.println("⚠️ Error clearing WeeklyMeals: " + e.getMessage());
        }
    }

    private void generateAndSaveWeeklyMeals() {
        String[] types = {"breakfast", "lunch", "dinner", "snack"};
        DayOfWeek[] days = DayOfWeek.values();
        Random random = new Random();
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);

        for (DayOfWeek day : days) {
            for (String type : types) {
                Meal meal = allMeals.get(random.nextInt(allMeals.size()));
                storageManager.saveWeeklyMealToDatabase(type, meal, startOfWeek, day.name());
            }
        }

        System.out.println("✅ Weekly meals generated and saved.");
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
        return true; 
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
            storageManager.saveMealsToDatabase(type, selected); 
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
