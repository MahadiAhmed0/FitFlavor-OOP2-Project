package FrontEnd;
import java.util.Scanner;

public class HomePageUI {
    private final Scanner scanner;

    public HomePageUI(Scanner scanner) {
        this.scanner = new Scanner(System.in);
    }

    public void showDashboard() {
        while (true) {
            System.out.println("\n=== 🍽️ Welcome to the Meal System Dashboard ===");
            System.out.println("1. 🍲 Meal Recommendation System");
            System.out.println("2. 🧺 Profile");
            System.out.println("3. 🥗 Nutrition Tracker");
            System.out.println("4. 📅 Weekly Meal Planner");
            System.out.println("5. 🧠 Weight Loss Prediction");
            System.out.println("6. 🌿 Daily Health Tips & Fun Facts");
            System.out.println("7. 🔒 Logout");
            System.out.print("Choose an option: ");

            int choice = getChoice();



            switch (choice) {
                case 1:
                    System.out.println("Launching Meal Recommendation System...");
                    MealSystemUI meal = new MealSystemUI(scanner);
                    try {
                        meal.start(); // Updated to call appropriate method
                    } catch (Exception e) {
                        System.err.println("An error occurred while interacting with the Meal Recommendation System: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("Launching Profile..");
                    // ingredientFilter();
                    break;
                case 3:
                    System.out.println("Opening Nutrition Tracker...");
                    NutritionStatsUI tracker = new NutritionStatsUI();
                    tracker.showStatsMenu();
                    break;
                case 4:
                    System.out.println("Generating Weekly Meal Planner...");
                    // weeklyPlanner();
                    break;
                case 5:
                    System.out.println("Launching AI Weight Prediction Module...");
                    // weightPrediction();
                    break;
                case 6:
                    System.out.println("Here's your daily health tip...");
                    // dailyTip();
                    break;
                case 7:
                    System.out.println("Logging out...");
                    return; // returns control back to AuthenticationUI
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
        scanner.nextLine(); // clear buffer
        return choice;
    }
}
