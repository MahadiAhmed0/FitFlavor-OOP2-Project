package BackEnd;

import java.sql.*;
import java.util.*;

public class MealStorageManager {

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
        List<Meal> meals = new ArrayList<>(); // Initialize the list, so it is never null

        String query = "SELECT m.* FROM DailyMeals dm " +
                "JOIN Meals m ON dm.meal_id = m.meal_id " +
                "WHERE dm.meal_type = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, mealType); // Bind the meal type to the query
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Parse the database row into a Meal object
                    Meal meal = Meal.fromResultSet(rs);
                    meals.add(meal); // Add each meal to the list
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to retrieve meals for type '" + mealType + "': " + e.getMessage());
            // Return an empty list in case of an error
        }

        return meals; // Return the list (either filled with meals or empty)
    }

    public void updateMealInDatabase(Meal meal) {
        String updateMealQuery = "UPDATE Meals SET name = ?, calories = ?, fats = ?, proteins = ? WHERE meal_id = ?";
        String deleteIngredientsQuery = "DELETE FROM MealIngredients WHERE meal_id = ?";
        String insertIngredientsQuery = "INSERT INTO MealIngredients (meal_id, ingredient_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Update the meal's main attributes
            try (PreparedStatement updateMealStmt = conn.prepareStatement(updateMealQuery)) {
                updateMealStmt.setString(1, meal.getName());
                updateMealStmt.setInt(2, meal.getCalories());
                updateMealStmt.setDouble(3, meal.getFats());
                updateMealStmt.setDouble(4, meal.getProteins());
                updateMealStmt.setInt(5, meal.getMealId());
                updateMealStmt.executeUpdate();
            }

            // Delete existing ingredients
            try (PreparedStatement deleteIngredientsStmt = conn.prepareStatement(deleteIngredientsQuery)) {
                deleteIngredientsStmt.setInt(1, meal.getMealId());
                deleteIngredientsStmt.executeUpdate();
            }

            // Insert updated ingredients
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

    // Helper method to get the ingredient ID from the database
    private int getIngredientId(Connection conn, String ingredientName) throws SQLException {
        String query = "SELECT ingredient_id FROM Ingredients WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, ingredientName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ingredient_id");
            }
        }
        return -1; // If ingredient not found
    }

}