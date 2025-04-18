package BackEnd;

import java.sql.*;
import java.util.*;

public class MealLoader {
    public List<Meal> loadMeals() {
        List<Meal> meals = new ArrayList<>();

        String mealQuery = "SELECT * FROM Meals";
        String ingredientQuery = "SELECT i.name FROM Ingredients i " +
                "JOIN MealIngredients mi ON i.ingredient_id = mi.ingredient_id " +
                "WHERE mi.meal_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement mealStmt = conn.createStatement();
             ResultSet mealRs = mealStmt.executeQuery(mealQuery)) {

            while (mealRs.next()) {
                int mealId = mealRs.getInt("meal_id");
                String name = mealRs.getString("name");
                int calories = mealRs.getInt("calories");
                float fats = mealRs.getFloat("fats");
                float proteins = mealRs.getFloat("proteins");

                
                List<String> ingredients = getIngredientsForMeal(conn, mealId, ingredientQuery);

                
                Meal meal = new Meal(mealId, name, calories, fats, proteins);
                meals.add(meal);
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Failed to load meals: " + e.getMessage());
        }

        return meals;
    }

    
    private List<String> getIngredientsForMeal(Connection conn, int mealId, String ingredientQuery) {
        List<String> ingredients = new ArrayList<>();
        try (PreparedStatement ingStmt = conn.prepareStatement(ingredientQuery)) {
            ingStmt.setInt(1, mealId);
            ResultSet ingRs = ingStmt.executeQuery();

            while (ingRs.next()) {
                ingredients.add(ingRs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error fetching ingredients for mealId " + mealId + ": " + e.getMessage());
        }
        return ingredients;
    }
}
