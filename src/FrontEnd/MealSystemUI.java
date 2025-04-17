package FrontEnd;

import BackEnd.MealRecommendationSystem;
import BackEnd.MealStorageManager;

import java.util.Scanner;

public class MealSystemUI {
    private final Scanner scanner;

    public MealSystemUI() {
        scanner = new Scanner(System.in);
    }
    private void mealFileActions() {
        MealStorageManager storage = new MealStorageManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nManage Meal Files:");
        System.out.println("1. Modify Breakfast");
        System.out.println("2. Modify Lunch");
        System.out.println("3. Modify Dinner");
        System.out.println("4. Modify Noon Snack");
        System.out.println("5. Modify Evening Snack");
        System.out.println("6. Regenerate All Meals");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> storage.modifyMeal("breakfast");
            case 2 -> storage.modifyMeal("lunch");
            case 3 -> storage.modifyMeal("dinner");
            case 4 -> storage.modifyMeal("noon_snack");
            case 5 -> storage.modifyMeal("evening_snack");
            case 6 -> new MealRecommendationSystem().suggestDailyMeals();
            default -> System.out.println("Invalid option.");
        }
    }

    public void suggestDailyMeals() {
        System.out.println("\n📥 Loading your personalized meal suggestions...");
        MealRecommendationSystem recommender = new MealRecommendationSystem();
        recommender.suggestDailyMeals();
        mealFileActions();
    }

    private int getChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // clear buffer
        return choice;
    }
}
