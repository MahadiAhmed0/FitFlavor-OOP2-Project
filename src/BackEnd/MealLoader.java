package BackEnd;

import java.io.*;
import java.util.*;

public class MealLoader {
    private static final String MEAL_FILE = "meals.txt";

    public List<Meal> loadMeals() {
        List<Meal> meals = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(MEAL_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 6) continue; // name;calories;ingredients;fats;proteins;type

                String name = parts[0].trim();
                int calories = Integer.parseInt(parts[1].trim());
                List<String> ingredients = Arrays.asList(parts[2].trim().split(","));
                double fats = Double.parseDouble(parts[3].trim());
                double proteins = Double.parseDouble(parts[4].trim());
                String type = parts[5].trim().toLowerCase();

                Meal meal;
                switch (type) {
                    case "breakfast":
                        meal = new Breakfast(name, calories, ingredients, fats, proteins);
                        break;
                    case "lunch":
                        meal = new Lunch(name, calories, ingredients, fats, proteins);
                        break;
                    case "dinner":
                        meal = new Dinner(name, calories, ingredients, fats, proteins);
                        break;
                    case "snack":
                        meal = new Snack(name, calories, ingredients, fats, proteins);
                        break;
                    default:
                        meal = new Meal(name, calories, ingredients, fats, proteins);
                        break;
                }

                meals.add(meal);
            }

        } catch (IOException e) {
            System.out.println("⚠️ Failed to load meals: " + e.getMessage());
        }

        return meals;
    }
}
