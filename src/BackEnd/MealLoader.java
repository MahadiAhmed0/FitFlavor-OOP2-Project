package BackEnd;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MealLoader {
    private static final String MEAL_FILE = "meals.txt";

    public List<Meal> loadMeals() {
        List<Meal> meals = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(MEAL_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 5) continue;

                String name = parts[0].trim();
                int calories = Integer.parseInt(parts[1].trim());
                List<String> ingredients = Arrays.asList(parts[2].trim().split(","));
                double fats = Double.parseDouble(parts[3].trim());
                double proteins = Double.parseDouble(parts[4].trim());

                meals.add(new Meal(name, calories, ingredients, fats, proteins));
            }

        } catch (IOException e) {
            System.out.println("⚠️ Failed to load meals: " + e.getMessage());
        }

        return meals;
    }
}
