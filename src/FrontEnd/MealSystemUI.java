package FrontEnd;

import BackEnd.MealRecommendationSystem;

import java.util.Scanner;

public class MealSystemUI {
    private final Scanner scanner;

    public MealSystemUI() {
        scanner = new Scanner(System.in);
    }

    public void suggestDailyMeals() {
        System.out.println("\n📥 Loading your personalized meal suggestions...");
        MealRecommendationSystem recommender = new MealRecommendationSystem();
        recommender.suggestDailyMeals();
    }
    public void showDashboard() {
        while (true) {
            System.out.println("\n=== 🍽️ Meal System Dashboard ===");
            System.out.println("1. 🍲 Meal Recommendation System");
            System.out.println("2. 🧺 Ingredient-Based Filtering");
            System.out.println("3. 🥗 Nutrition Tracker");
            System.out.println("4. 📅 Weekly Meal Planner");
            System.out.println("5. 🧠 AI-Inspired Weight Prediction Module");
            System.out.println("6. 🌿 Daily Health Tips & Fun Facts");
            System.out.println("7. 🔒 Logout");
            System.out.print("Choose an option: ");

            int choice = getChoice();

            switch (choice) {
                case 1:
                    handleMealRecommendation();
                    break;
                case 2:
                    System.out.println("Feature coming soon: Ingredient-Based Filtering...");
                    break;
                case 3:
                    System.out.println("Feature coming soon: Nutrition Tracker...");
                    break;
                case 4:
                    System.out.println("Feature coming soon: Weekly Meal Planner...");
                    break;
                case 5:
                    System.out.println("Feature coming soon: AI Weight Prediction...");
                    break;
                case 6:
                    System.out.println("Feature coming soon: Daily Health Tips...");
                    break;
                case 7:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    private void handleMealRecommendation() {
        System.out.println("\n📥 Loading your personalized meal suggestions...");
        MealRecommendationSystem recommender = new MealRecommendationSystem();
        recommender.suggestDailyMeals();
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
