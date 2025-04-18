package FrontEnd;

import BackEnd.WeeklyNutritionStats;
import BackEnd.NutritionData;

import java.util.*;

public class WeeklyNutritionStatsUI {

    public void showWeeklyStats() {
        try {
            WeeklyNutritionStats stats = new WeeklyNutritionStats();
            Map<String, List<NutritionData>> weeklyBreakdown = stats.getWeekNutritionBreakdown();
            Map<String, NutritionData> weeklyTotals = stats.getTotalStatsPerDay();

            System.out.println("\n📅 WEEKLY NUTRITION STATS");

            for (String day : weeklyBreakdown.keySet()) {
                List<NutritionData> dayMeals = weeklyBreakdown.get(day);
                NutritionData total = weeklyTotals.get(day);

                System.out.printf("\n===== %s =====\n", day.toUpperCase());
                printMealStats(dayMeals, total);
            }

        } catch (Exception e) {
            System.err.println("⚠️ Failed to load weekly nutrition stats: " + e.getMessage());
        }
    }

    private static final int CALORIE_UNIT = 50;

    private void printMealStats(List<NutritionData> meals, NutritionData total) {
        // Prepare map: MealType -> Calories
        Map<String, Integer> caloriesMap = new LinkedHashMap<>();
        List<String> mealOrder = Arrays.asList("breakfast", "lunch", "snack", "dinner");
        for (String meal : mealOrder) caloriesMap.put(meal, 0);

        for (NutritionData data : meals) {
            caloriesMap.put(data.getMealType(), data.getCalories());
        }

        // Determine max bar length
        int maxCalories = Collections.max(caloriesMap.values());
        int maxUnits = Math.max(1, maxCalories / CALORIE_UNIT + 1);

        // Print horizontal bar chart
        System.out.println("\n📊 CALORIE VS MEAL BAR CHART (Horizontal)");
        System.out.println("=".repeat(50));

        for (String meal : mealOrder) {
            int value = caloriesMap.get(meal);
            int units = value / CALORIE_UNIT;
            System.out.printf("%-10s | %-4d kcal | %s\n", capitalize(meal), value, "█".repeat(units));
        }

        // X-axis scale
        System.out.println("=".repeat(50));
        System.out.println("Scale: 1 unit = " + CALORIE_UNIT + " kcal");

        // Daily total
        System.out.println("\n🧾 Daily Total:");
        System.out.printf("Calories: %d kcal\n", total.getCalories());
        System.out.printf("Fats: %.1f g\n", total.getFats());
        System.out.printf("Proteins: %.1f g\n", total.getProteins());
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

}
