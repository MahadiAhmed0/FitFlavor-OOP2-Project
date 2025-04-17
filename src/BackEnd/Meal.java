package BackEnd;

import java.sql.*;
import java.util.*;

public class Meal {
    private final int mealId;
    private String name;
    private int calories;
    private double fats;
    private double proteins;

    public Meal(int mealId, String name, int calories, double fats, double proteins) {
        this.mealId = mealId;
        this.name = name;
        this.calories = calories;
        this.fats = fats;
        this.proteins = proteins;
    }

    public int getMealId() {
        return mealId;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public double getFats() {
        return fats;
    }

    public double getProteins() {
        return proteins;
    }

    // Setter methods for updating meal properties
    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    // Fetch the ingredients from the database based on meal_id
    public List<String> getIngredients(Connection conn) {
        List<String> ingredients = new ArrayList<>();
        String query = "SELECT i.name FROM Ingredients i " +
                "JOIN MealIngredients mi ON i.ingredient_id = mi.ingredient_id " +
                "WHERE mi.meal_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.mealId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ingredients.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching ingredients for meal: " + e.getMessage());
        }

        return ingredients;
    }

    @Override
    public String toString() {
        // Fetch ingredients from the database when displaying meal
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            List<String> ingredients = getIngredients(conn);
            return "\n🍽️ " + name + " (" + calories + " kcal)\n" +
                    "🥩 Proteins: " + proteins + "g | 🧈 Fats: " + fats + "g\n" +
                    "🧺 Ingredients: " + String.join(", ", ingredients);
        } catch (SQLException e) {
            return "⚠️ Error fetching ingredients.";
        }
    }

    public void updateIngredients(Connection conn, List<String> newIngredients) throws SQLException {
        // First, remove all existing ingredients from MealIngredients table
        String deleteQuery = "DELETE FROM MealIngredients WHERE meal_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, this.mealId);
            deleteStmt.executeUpdate();
        }

        // Then, insert the new ingredients into the MealIngredients table
        String insertQuery = "INSERT INTO MealIngredients (meal_id, ingredient_id) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            for (String ingredientName : newIngredients) {
                // Get the ingredient ID from the Ingredients table
                String ingredientQuery = "SELECT ingredient_id FROM Ingredients WHERE name = ?";
                try (PreparedStatement ingredientStmt = conn.prepareStatement(ingredientQuery)) {
                    ingredientStmt.setString(1, ingredientName);
                    ResultSet rs = ingredientStmt.executeQuery();

                    if (rs.next()) {
                        int ingredientId = rs.getInt("ingredient_id");
                        // Insert the ingredient into the MealIngredients table
                        insertStmt.setInt(1, this.mealId);
                        insertStmt.setInt(2, ingredientId);
                        insertStmt.executeUpdate();
                    } else {
                        // If the ingredient doesn't exist, you can either add it or skip
                        System.out.println("⚠️ Ingredient " + ingredientName + " not found in database.");
                    }
                }
            }
        }
    }

    // Static method to parse meal from database row data
    public static Meal fromResultSet(ResultSet rs) throws SQLException {
        int mealId = rs.getInt("meal_id");
        String name = rs.getString("name");
        int calories = rs.getInt("calories");
        double fats = rs.getDouble("fats");
        double proteins = rs.getDouble("proteins");

        return new Meal(mealId, name, calories, fats, proteins);
    }

    // If you still want to export meals to file, you could use a simplified version without ingredients
    public String toFileString() {
        return name + ";" + calories + ";" + fats + ";" + proteins;
    }
}
