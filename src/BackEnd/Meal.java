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
        
        String deleteQuery = "DELETE FROM MealIngredients WHERE meal_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, this.mealId);
            deleteStmt.executeUpdate();
        }

        
        String insertQuery = "INSERT INTO MealIngredients (meal_id, ingredient_id) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            for (String ingredientName : newIngredients) {
                
                String ingredientQuery = "SELECT ingredient_id FROM Ingredients WHERE name = ?";
                try (PreparedStatement ingredientStmt = conn.prepareStatement(ingredientQuery)) {
                    ingredientStmt.setString(1, ingredientName);
                    ResultSet rs = ingredientStmt.executeQuery();

                    if (rs.next()) {
                        int ingredientId = rs.getInt("ingredient_id");
                        
                        insertStmt.setInt(1, this.mealId);
                        insertStmt.setInt(2, ingredientId);
                        insertStmt.executeUpdate();
                    } else {
                        
                        System.out.println("⚠️ Ingredient " + ingredientName + " not found in database.");
                    }
                }
            }
        }
    }

    
    public static Meal fromResultSet(ResultSet rs) throws SQLException {
        int mealId = rs.getInt("meal_id");
        String name = rs.getString("name");
        int calories = rs.getInt("calories");
        double fats = rs.getDouble("fats");
        double proteins = rs.getDouble("proteins");

        return new Meal(mealId, name, calories, fats, proteins);
    }

    
    public String toFileString() {
        return name + ";" + calories + ";" + fats + ";" + proteins;
    }
}
