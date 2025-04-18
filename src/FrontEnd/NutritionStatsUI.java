package FrontEnd;

import java.util.Scanner;

public class NutritionStatsUI {

    private final Scanner scanner = new Scanner(System.in);
    private final DailyNutritionStatsUI dailyUI = new DailyNutritionStatsUI();
    private final WeeklyNutritionStatsUI weeklyUI = new WeeklyNutritionStatsUI();

    public void showStatsMenu() {
        while (true) {
            System.out.println("\n📈 Nutrition Stats Menu");
            System.out.println("1. 📅 View Daily Nutrition Stats");
            System.out.println("2. 📆 View Weekly Nutrition Stats");
            System.out.println("3. 🔙 Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    dailyUI.showTodayStats();
                    CalorieBarChartTerminal.display();
                }
                case "2" -> {
                    weeklyUI.showWeeklyStats();
                    CalorieBarChartTerminal.displayWeeklyCaloriesFromWeeklyMeals();
                }
                case "3" -> {
                    System.out.println("🔙 Returning to previous menu...");
                    return;
                }
                default -> System.out.println("❌ Invalid input. Try again.");
            }
        }
    }
}
