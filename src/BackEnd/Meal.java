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
}
