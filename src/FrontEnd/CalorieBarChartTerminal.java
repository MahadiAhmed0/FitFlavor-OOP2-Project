package FrontEnd;

import BackEnd.NutritionStatsService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class CalorieBarChartTerminal {

    private static final int MAX_HEIGHT = 10; // max rows for Y-axis
    private static final int CALORIE_UNIT = 50; // 1 unit = 50 calories

    public static void display() {

        NutritionStatsService service = new NutritionStatsService();
        List<NutritionStatsService.NutritionData> dataList = service.getDailyNutritionStats(Date.valueOf(LocalDate.now()));

        if (dataList.isEmpty()) {
            System.out.println("No meals found for today.");
            return;
        }

        // Prepare map: MealType -> Calories
        Map<String, Integer> caloriesMap = new LinkedHashMap<>();
        List<String> mealOrder = Arrays.asList("breakfast", "lunch", "snack", "dinner");

        for (String meal : mealOrder) caloriesMap.put(meal, 0);

        for (NutritionStatsService.NutritionData data : dataList) {
            caloriesMap.put(data.mealType, data.calories);
        }

        // Determine max calories
        int maxCalories = Collections.max(caloriesMap.values());
        int maxUnits = Math.max(1, maxCalories / CALORIE_UNIT + 1);

        System.out.println("\n📊 CALORIE VS MEAL BAR CHART");
        System.out.println("=".repeat(40));

        // Print chart from top row to bottom
        for (int level = maxUnits; level > 0; level--) {
            int calorieLevel = level * CALORIE_UNIT;
            System.out.printf("%4d | ", calorieLevel);
            for (String meal : mealOrder) {
                int value = caloriesMap.get(meal);
                System.out.print(value >= calorieLevel ? " █ " : "   ");
            }
            System.out.println();
        }

        // X-axis
        System.out.println("      " + "---".repeat(mealOrder.size()));
        System.out.print("       ");
        for (String meal : mealOrder) {
            System.out.print(meal.substring(0, 1) + "  "); // Initials
        }
        System.out.println();

        // Legend
        System.out.println("\nLegend:");
        for (String meal : mealOrder) {
            System.out.printf("  %s (%s)\n", meal.substring(0, 1), meal);
        }

        System.out.println("=".repeat(40));
    }
}
