package BackEnd;

import java.util.Collections;
import java.util.List;

public class MealRecommendationSystem {
    private final List<Meal> mealList;

    public MealRecommendationSystem() {
        MealLoader loader = new MealLoader();
        mealList = loader.loadMeals();
    }

    public void suggestDailyMeals() {
        if (mealList.size() < 5) {
            System.out.println("Not enough meals in the database to generate full suggestions.");
            return;
        }

        Collections.shuffle(mealList); // Random suggestions
        System.out.println("\n🥣 Breakfast: " + mealList.get(0));
        System.out.println("\n🍱 Lunch: " + mealList.get(1));
        System.out.println("\n🍽️ Dinner: " + mealList.get(2));
        System.out.println("\n🍌 Noon Snack: " + mealList.get(3));
        System.out.println("\n🍎 Evening Snack: " + mealList.get(4));
    }

    public List<Meal> getAllMeals() {
        return mealList;
    }
}
