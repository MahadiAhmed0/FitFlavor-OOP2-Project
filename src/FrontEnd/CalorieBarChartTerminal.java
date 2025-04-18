package FrontEnd;

import BackEnd.NutritionStatsService;
import BackEnd.WeeklyNutritionStats;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class CalorieBarChartTerminal {
    
    private static final int CALORIE_UNIT = 50; 

    public static void display() {

        NutritionStatsService service = new NutritionStatsService();
        List<NutritionStatsService.NutritionData> dataList = service.getDailyNutritionStats(Date.valueOf(LocalDate.now()));

        if (dataList.isEmpty()) {
            System.out.println("No meals found for today.");
            return;
        }

        
        Map<String, Integer> caloriesMap = new LinkedHashMap<>();
        List<String> mealOrder = Arrays.asList("breakfast", "lunch", "snack", "dinner");

        for (String meal : mealOrder) caloriesMap.put(meal, 0);

        for (NutritionStatsService.NutritionData data : dataList) {
            caloriesMap.put(data.mealType, data.calories);
        }

        
        int maxCalories = Collections.max(caloriesMap.values());
        int maxUnits = Math.max(1, maxCalories / CALORIE_UNIT + 1);

        System.out.println("\n📊 CALORIE VS MEAL BAR CHART");
        System.out.println("=".repeat(40));

        
        for (int level = maxUnits; level > 0; level--) {
            int calorieLevel = level * CALORIE_UNIT;
            System.out.printf("%4d | ", calorieLevel);
            for (String meal : mealOrder) {
                int value = caloriesMap.get(meal);
                System.out.print(value >= calorieLevel ? " █ " : "   ");
            }
            System.out.println();
        }

        
        System.out.println("      " + "---".repeat(mealOrder.size()));
        System.out.print("       ");
        for (String meal : mealOrder) {
            System.out.print(meal.substring(0, 1) + "  "); 
        }
        System.out.println();

        
        System.out.println("\nLegend:");
        for (String meal : mealOrder) {
            System.out.printf("  %s (%s)\n", meal.substring(0, 1), meal);
        }

        System.out.println("=".repeat(40));
    }

    public static void displayTodayFromWeeklyMeals() {
        WeeklyNutritionStats stats;
        try {
            stats = new WeeklyNutritionStats();
        } catch (Exception e) {
            System.err.println("Failed to initialize WeeklyNutritionStats: " + e.getMessage());
            return;
        }
        String today = LocalDate.now().getDayOfWeek().name().toLowerCase(Locale.ROOT);
        today = today.substring(0, 1).toUpperCase() + today.substring(1); 

        Map<String, List<BackEnd.NutritionData>> breakdown = stats.getWeekNutritionBreakdown();
        List<BackEnd.NutritionData> todayMeals = breakdown.getOrDefault(today, new ArrayList<>());

        if (todayMeals.isEmpty()) {
            System.out.println("No meals recorded in WeeklyMeals for today.");
            return;
        }

        Map<String, Integer> caloriesMap = new LinkedHashMap<>();
        List<String> mealOrder = Arrays.asList("breakfast", "lunch", "snack", "dinner");

        for (String meal : mealOrder) caloriesMap.put(meal, 0);
        for (BackEnd.NutritionData data : todayMeals) {
            caloriesMap.put(data.getMealType(), data.getCalories());
        }

        int maxCalories = Collections.max(caloriesMap.values());
        int maxUnits = Math.max(1, maxCalories / CALORIE_UNIT + 1);

        System.out.println("\n📊 TODAY'S CALORIE CHART FROM WEEKLY MEALS");
        System.out.println("=".repeat(40));

        for (int level = maxUnits; level > 0; level--) {
            int threshold = level * CALORIE_UNIT;
            System.out.printf("%4d | ", threshold);
            for (String meal : mealOrder) {
                int value = caloriesMap.get(meal);
                System.out.print(value >= threshold ? " █ " : "   ");
            }
            System.out.println();
        }

        System.out.println("      " + "---".repeat(mealOrder.size()));
        System.out.print("       ");
        for (String meal : mealOrder) {
            System.out.print(meal.substring(0, 1) + "  ");
        }
        System.out.println();

        System.out.println("\nLegend:");
        for (String meal : mealOrder) {
            System.out.printf("  %s (%s)\n", meal.substring(0, 1), meal);
        }

        System.out.println("=".repeat(40));
    }

    public static void displayWeeklyCaloriesFromWeeklyMeals() {
        WeeklyNutritionStats stats = null;
        try {
            stats = new WeeklyNutritionStats();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, List<BackEnd.NutritionData>> breakdown = stats.getWeekNutritionBreakdown();

        Map<String, Integer> dayCalories = new LinkedHashMap<>();
        List<String> weekDays = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

        for (String day : weekDays) {
            int total = 0;
            for (BackEnd.NutritionData meal : breakdown.getOrDefault(day, Collections.emptyList())) {
                total += meal.getCalories();
            }
            dayCalories.put(day, total);
        }

        int maxCalories = Collections.max(dayCalories.values());
        int maxUnits = Math.max(1, maxCalories / CALORIE_UNIT + 1);

        System.out.println("\n📊 WEEKLY CALORIE CHART");
        System.out.println("=".repeat(50));

        for (int level = maxUnits; level > 0; level--) {
            int threshold = level * CALORIE_UNIT;
            System.out.printf("%4d | ", threshold);
            for (String day : weekDays) {
                int value = dayCalories.get(day);
                System.out.print(value >= threshold ? " █ " : "   ");
            }
            System.out.println();
        }

        System.out.println("      " + "---".repeat(weekDays.size()));
        System.out.print("       ");
        for (String day : weekDays) {
            System.out.print(day.substring(0, 2) + " ");
        }
        System.out.println();

        System.out.println("\nLegend:");
        for (String day : weekDays) {
            System.out.printf("  %s (%s)\n", day.substring(0, 2), day);
        }

        System.out.println("=".repeat(50));
    }
}
