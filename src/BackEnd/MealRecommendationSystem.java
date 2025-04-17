package BackEnd;

import java.util.*;

public class MealRecommendationSystem {
    private final List<Meal> allMeals;
    private final MealLoader mealLoader;
    private final MealStorageManager storageManager;

    public MealRecommendationSystem() {
        mealLoader = new MealLoader();
        allMeals = mealLoader.loadMeals();
        storageManager = new MealStorageManager();
    }

    public void suggestDailyMeals() {
        Map<String, List<Meal>> dailyMeals = new HashMap<>();
        Random random = new Random();

        String[] types = {"breakfast", "lunch", "dinner", "noon_snack", "evening_snack"};

        for (String type : types) {
            List<Meal> selected = new ArrayList<>();
            while (selected.size() < 1) {
                Meal m = allMeals.get(random.nextInt(allMeals.size()));
                if (!selected.contains(m)) selected.add(m);
            }
            dailyMeals.put(type, selected);
            storageManager.saveMealsToFile(type, selected);
        }

        showMeals(dailyMeals);
    }

    private void showMeals(Map<String, List<Meal>> dailyMeals) {
        dailyMeals.forEach((type, meals) -> {
            System.out.println("\n🍽️ " + type.toUpperCase().replace("_", " ") + ":");
            meals.forEach(meal -> {
                System.out.println(meal);
            });
        });
    }
}

