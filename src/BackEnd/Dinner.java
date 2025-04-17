package BackEnd;
import java.util.List;

public class Dinner extends Meal {
    public Dinner(String name, int calories, List<String> ingredients, double fats, double proteins) {
        super(name, calories, ingredients, fats, proteins);
    }
}