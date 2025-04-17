package FrontEnd;

import BackEnd.MealRecommendationSystem;
import BackEnd.MealStorageManager;
import BackEnd.Meal;
import BackEnd.DatabaseConnection;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class MealSystemUI {
    private final Scanner scanner;
    private final MealRecommendationSystem recommendationSystem;
    private final MealStorageManager storageManager;

    public MealSystemUI(Scanner scanner) {
        this.scanner = new Scanner(System.in);
        this.recommendationSystem = new MealRecommendationSystem();
        this.storageManager = new MealStorageManager();
    }

    public void start() {
        System.out.println("🍴 Welcome to the Meal System! 🍴");
        while (true) {
            System.out.println("\n---- Main Menu ----");
            System.out.println("1. Suggest Daily Meals");
            System.out.println("2. View and Edit Meals");
            System.out.println("3. Exit");

            System.out.print("Enter your choice: ");
            int choice = getChoice();

            try {
                switch (choice) {
                    case 1 -> suggestionActions();
                    case 2 -> viewAndEditMealsActions();
                    case 3 -> {
                        System.out.println("👋 Exiting Meal System. Goodbye!");
                        return;
                    }
                    default -> System.out.println("⚠️ Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.err.println("⚠️ An error occurred while processing your request: " + e.getMessage());
            }
        }
    }

    private void suggestionActions() {
        try {
            System.out.println("\n---- Suggest Daily Meals ----");
            recommendationSystem.suggestDailyMeals();
        } catch (Exception e) {
            System.err.println("⚠️ Failed to suggest daily meals: " + e.getMessage());
        }
    }

    private void viewAndEditMealsActions() {
        try {
            boolean isRunning = true;
            while (isRunning) {
                System.out.println("\n---- View and Edit Meals ----");
                System.out.println("1. Edit Daily Meals");
                System.out.println("2. Back to Main Menu");
                System.out.print("Enter your choice: ");
                int choice = getChoice();

                switch (choice) {
                    case 1:
                        editDailyMeals();
                        break;
                    case 2:
                        System.out.println("\nReturning to the Main Menu...");
                        isRunning = false;
                        break;
                    default:
                        System.out.println("⚠️ Invalid choice! Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to view or edit meals: " + e.getMessage());
        }
    }

    private void editDailyMeals() {
        try {
            System.out.println("\n---- Edit Meals ----");
            String[] mealTypes = {"breakfast", "lunch", "dinner", "snack"};
            for (int i = 0; i < mealTypes.length; i++) {
                System.out.println((i + 1) + ". " + mealTypes[i]);
            }

            System.out.print("Choose a meal type to edit (1-4): ");
            int choice = getChoice();
            if (choice < 1 || choice > mealTypes.length) {
                System.out.println("⚠️ Invalid choice! Returning to the main menu.");
                return;
            }

            String selectedType = mealTypes[choice - 1];
            List<Meal> meals = storageManager.viewMealsByType(selectedType);

            if (meals == null || meals.isEmpty()) {
                System.out.println("⚠️ No meals found for " + selectedType + "!");
                return;
            }

            System.out.println("\nMeals for " + selectedType + ":");
            for (int i = 0; i < meals.size(); i++) {
                System.out.println((i + 1) + ". " + meals.get(i));
            }

            System.out.print("\nSelect a meal to edit (1-" + meals.size() + "): ");
            int mealChoice = getChoice();

            if (mealChoice < 1 || mealChoice > meals.size()) {
                System.out.println("⚠️ Invalid meal choice! Returning to the main menu.");
                return;
            }

            Meal mealToEdit = meals.get(mealChoice - 1);
            System.out.println("\nEditing: " + mealToEdit.getName());
            editMeal(mealToEdit);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to edit meals: " + e.getMessage());
        }
    }

    private void editMeal(Meal meal) {
        try {
            while (true) {
                System.out.println("\n---- Edit Meal ----");
                System.out.println("1. Edit Name");
                System.out.println("2. Edit Calories");
                System.out.println("3. Edit Fats");
                System.out.println("4. Edit Proteins");
                System.out.println("5. Edit Ingredients");
                System.out.println("6. Save and Go Back");

                System.out.print("Choose an option: ");
                int choice = getChoice();

                switch (choice) {
                    case 1 -> editName(meal);
                    case 2 -> editCalories(meal);
                    case 3 -> editFats(meal);
                    case 4 -> editProteins(meal);
                    case 5 -> editIngredients(meal);
                    case 6 -> {
                        storageManager.updateMealInDatabase(meal);
                        System.out.println("✅ Meal updated successfully!");
                        return;
                    }
                    default -> System.out.println("⚠️ Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to edit the meal: " + e.getMessage());
        }
    }

    private void editName(Meal meal) {
        try {
            System.out.print("Enter new name: ");
            scanner.nextLine(); // Consume leftover newline
            String newName = scanner.nextLine();
            meal.setName(newName);
            System.out.println("✅ Name updated to " + newName);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to edit meal name: " + e.getMessage());
        }
    }

    private void editCalories(Meal meal) {
        try {
            System.out.print("Enter new calories: ");
            int newCalories = getChoice();
            meal.setCalories(newCalories);
            System.out.println("✅ Calories updated to " + newCalories);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to edit calories: " + e.getMessage());
        }
    }

    private void editFats(Meal meal) {
        try {
            System.out.print("Enter new fats: ");
            double newFats = scanner.nextDouble();
            meal.setFats(newFats);
            System.out.println("✅ Fats updated to " + newFats);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to edit fats: " + e.getMessage());
        }
    }

    private void editProteins(Meal meal) {
        try {
            System.out.print("Enter new proteins: ");
            double newProteins = scanner.nextDouble();
            meal.setProteins(newProteins);
            System.out.println("✅ Proteins updated to " + newProteins);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to edit proteins: " + e.getMessage());
        }
    }

    private void editIngredients(Meal meal) {
        try {
            System.out.println("\nEditing ingredients for: " + meal.getName());
            List<String> currentIngredients = meal.getIngredients(getConnection());
            System.out.println("Current Ingredients: " + currentIngredients);

            System.out.print("\nEnter new ingredients (comma-separated): ");
            scanner.nextLine(); // Consume leftover newline
            String input = scanner.nextLine();
            List<String> newIngredients = List.of(input.split("\\s*,\\s*"));

            meal.updateIngredients(getConnection(), newIngredients);
            System.out.println("✅ Ingredients updated!");
        } catch (Exception e) {
            System.err.println("⚠️ Failed to edit ingredients: " + e.getMessage());
        }
    }

    private int getChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("⚠️ Invalid input. Please enter a number: ");
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // clear buffer
        return choice;
    }

    private Connection getConnection() {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {
            System.err.println("⚠️ Database connection failure: " + e.getMessage());
            return null;
        }
    }
}