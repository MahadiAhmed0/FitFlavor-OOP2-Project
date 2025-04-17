package BackEnd;

import java.util.List;

public class Meal {
    private final String name;
    private final int calories;
    private final List<String> ingredients;
    private final double fats;
    private final double proteins;

    public Meal(String name, int calories, List<String> ingredients, double fats, double proteins) {
        this.name = name;
        this.calories = calories;
        this.ingredients = ingredients;
        this.fats = fats;
        this.proteins = proteins;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public double getFats() {
        return fats;
    }

    public double getProteins() {
        return proteins;
    }

    @Override
    public String toString() {
        return "\n🍽️ " + name + " (" + calories + " kcal)\n" +
                "🥩 Proteins: " + proteins + "g | 🧈 Fats: " + fats + "g\n" +
                "🧺 Ingredients: " + String.join(", ", ingredients);
    }

    public static Meal parseFromFileString(String line) {
        try {
            // Split line by semicolon: name;calories;ingredient1,ingredient2,...;fats;proteins
            String[] parts = line.split(";");
            if (parts.length != 5) throw new IllegalArgumentException("Invalid meal format");

            String name = parts[0].trim();
            int calories = Integer.parseInt(parts[1].trim());

            List<String> ingredients = List.of(parts[2].trim().split(","));

            double fats = Double.parseDouble(parts[3].trim());
            double proteins = Double.parseDouble(parts[4].trim());

            return new Meal(name, calories, ingredients, fats, proteins);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse meal from line: " + line, e);
        }
    }
    public String toFileString() {
        return name + ";" + calories + ";" +
                String.join(",", ingredients) + ";" +
                fats + ";" + proteins;
    }

}
