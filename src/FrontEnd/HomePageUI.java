package FrontEnd;
import BackEnd.HealthTipManager;

import java.util.Scanner;

public class HomePageUI {
    private final Scanner scanner;
    private final String username;
    public HomePageUI(Scanner scanner, String username) {
        this.scanner = new Scanner(System.in);
        this.username = username;
    }

    public void showDashboard() {
        while (true) {
            System.out.println("\n=== 🍽️ Welcome to the Meal System Dashboard ===");
            System.out.println("1. 🍲 Meal Recommendation System");
            System.out.println("2. 🧺 Profile");
            System.out.println("3. 🥗 Nutrition Tracker");
            System.out.println("4. 🧠 Weight Loss Prediction");
            System.out.println("5. 🌿 Daily Health Tips & Fun Facts");
            System.out.println("6. 🔒 Logout");
            System.out.print("Choose an option: ");

            int choice = getChoice();



            switch (choice) {
                case 1:
                    System.out.println("Launching Meal Recommendation System...");
                    MealSystemUI meal = new MealSystemUI(scanner);
                    try {
                        meal.start(); 
                    } catch (Exception e) {
                        System.err.println("An error occurred while interacting with the Meal Recommendation System: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("Launching Profile..");
                    ProfileUI profileUI = new ProfileUI(scanner, username);
                    profileUI.showProfileMenu();

                    break;
                case 3:
                    System.out.println("Opening Nutrition Tracker...");
                    NutritionStatsUI tracker = new NutritionStatsUI();
                    tracker.showStatsMenu();
                    break;
                case 4:
                    System.out.println("Launching Weight Prediction Module...");
                    WeightLossPredictionUI predictionUI = new WeightLossPredictionUI(scanner, username);
                    predictionUI.showWeightLossPredictionPage();
                    break;
                case 5:
                    System.out.println("Here's your daily health tip...");
                    HealthTipManager healthTipManager = new HealthTipManager();
                    String healthTip = healthTipManager.getRandomHealthTip();
                    System.out.println("💡 " + healthTip);
                    
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return; 
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    private int getChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        return choice;
    }
}
