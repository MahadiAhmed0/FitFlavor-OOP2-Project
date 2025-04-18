package FrontEnd;

import BackEnd.WeeklyNutritionStats;
import BackEnd.NutritionData;

import java.util.List;
import java.util.Map;

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

    private void printMealStats(List<NutritionData> meals, NutritionData total) {
        System.out.println("\n📊 Nutrition vs Meal Time Graph:");
        for (NutritionData data : meals) {
            System.out.printf("\n🍽️ %s:\n", data.getMealType());
            printBar("Calories", data.getCalories(), 100);
            printBar("Fats", (int) data.getFats(), 10);
            printBar("Proteins", (int) data.getProteins(), 10);
        }

        System.out.println("\n🧾 Daily Total:");
        System.out.printf("Calories: %d kcal\n", total.getCalories());
        System.out.printf("Fats: %.1f g\n", total.getFats());
        System.out.printf("Proteins: %.1f g\n", total.getProteins());
    }

    private void printBar(String label, int value, int scale) {
        int length = value / scale;
        System.out.printf("%-10s: %-3d | %s\n", label, value, "█".repeat(Math.max(0, length)));
    }
}
