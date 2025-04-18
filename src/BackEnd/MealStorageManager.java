package BackEnd;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class MealStorageManager {

    public void saveWeeklyMealToDatabase(String type, Meal meal, LocalDate weekStart, String dayOfWeek) {
        String insert = "INSERT INTO WeeklyMeals (meal_type, meal_id, week_start_date, day_of_week) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insert)) {

            pstmt.setString(1, type);
            pstmt.setInt(2, meal.getMealId());
            pstmt.setDate(3, java.sql.Date.valueOf(weekStart));
            pstmt.setString(4, dayOfWeek);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("⚠️ Error saving weekly meal: " + e.getMessage());
        }
    }

    public void saveMealsToDatabase(String mealType, List<Meal> meals) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            String insertQuery = "INSERT INTO DailyMeals (meal_type, meal_id, date) VALUES (?, ?, CURDATE())";
            try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
                for (Meal meal : meals) {
                    int mealId = getMealIdByName(conn, meal.getName());
                    if (mealId != -1) {
                        ps.setString(1, mealType);
                        ps.setInt(2, mealId);
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            }
        } catch (SQLException e) {
            System.err.println("Error saving meals to database: " + e.getMessage());
        }
    }

    private int getMealIdByName(Connection conn, String name) throws SQLException {
        String query = "SELECT meal_id FROM Meals WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("meal_id");
                }
            }
        }
        return -1;
    }

    public List<Meal> viewMealsByType(String mealType) {
        List<Meal> meals = new ArrayList<>(); 

        String query = "SELECT m.* FROM DailyMeals dm " +
                "JOIN Meals m ON dm.meal_id = m.meal_id " +
                "WHERE dm.meal_type = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, mealType); 
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    
                    Meal meal = Meal.fromResultSet(rs);
                    meals.add(meal); 
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to retrieve meals for type '" + mealType + "': " + e.getMessage());
            
        }

        return meals; 
    }

    public void updateMealInDatabase(Meal meal) {
        String updateMealQuery = "UPDATE Meals SET name = ?, calories = ?, fats = ?, proteins = ? WHERE meal_id = ?";
        String deleteIngredientsQuery = "DELETE FROM MealIngredients WHERE meal_id = ?";
        String insertIngredientsQuery = "INSERT INTO MealIngredients (meal_id, ingredient_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            
            try (PreparedStatement updateMealStmt = conn.prepareStatement(updateMealQuery)) {
                updateMealStmt.setString(1, meal.getName());
                updateMealStmt.setInt(2, meal.getCalories());
                updateMealStmt.setDouble(3, meal.getFats());
                updateMealStmt.setDouble(4, meal.getProteins());
                updateMealStmt.setInt(5, meal.getMealId());
                updateMealStmt.executeUpdate();
            }

            
            try (PreparedStatement deleteIngredientsStmt = conn.prepareStatement(deleteIngredientsQuery)) {
                deleteIngredientsStmt.setInt(1, meal.getMealId());
                deleteIngredientsStmt.executeUpdate();
            }

            
            try (PreparedStatement insertIngredientsStmt = conn.prepareStatement(insertIngredientsQuery)) {
                for (String ingredient : meal.getIngredients(conn)) {
                    int ingredientId = getIngredientId(conn, ingredient);
                    if (ingredientId != -1) {
                        insertIngredientsStmt.setInt(1, meal.getMealId());
                        insertIngredientsStmt.setInt(2, ingredientId);
                        insertIngredientsStmt.addBatch();
                    }
                }
                insertIngredientsStmt.executeBatch();
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error updating meal in database: " + e.getMessage());
        }
    }

    
    private int getIngredientId(Connection conn, String ingredientName) throws SQLException {
        String query = "SELECT ingredient_id FROM Ingredients WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, ingredientName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ingredient_id");
            }
        }
        return -1; 
    }

    public Map<String, Map<String, List<Meal>>> getWeeklyMeals() {
        Map<String, Map<String, List<Meal>>> weeklyMeals = new LinkedHashMap<>();

        String query = "SELECT wm.day_of_week, wm.meal_type, m.* " +
                "FROM WeeklyMeals wm " +
                "JOIN Meals m ON wm.meal_id = m.meal_id " +
                "WHERE wm.week_start_date = ? " +
                "ORDER BY wm.day_of_week, wm.meal_type";

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1); 

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(weekStart));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String day = rs.getString("day_of_week");
                    String type = rs.getString("meal_type");
                    Meal meal = Meal.fromResultSet(rs);

                    weeklyMeals
                            .computeIfAbsent(day, d -> new LinkedHashMap<>())
                            .computeIfAbsent(type, t -> new ArrayList<>())
                            .add(meal);
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to fetch weekly meals: " + e.getMessage());
        }

        return weeklyMeals;
    }

}