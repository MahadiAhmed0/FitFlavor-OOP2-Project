package BackEnd;

import java.util.List;

public class Snack extends Meal {
    public Snack(String name, int calories, List<String> ingredients, double fats, double proteins) {
        super(name, calories, ingredients, fats, proteins);
    }
}