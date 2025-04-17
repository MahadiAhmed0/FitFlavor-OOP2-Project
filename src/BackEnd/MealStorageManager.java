package BackEnd;

import java.io.*;
import java.util.*;

public class MealStorageManager {

    private static final String MEAL_FOLDER = "saved_meals";

    public MealStorageManager() {
        File folder = new File(MEAL_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public void saveMealsToFile(String mealType, List<Meal> meals) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEAL_FOLDER + "/" + mealType + ".txt"))) {
            for (Meal meal : meals) {
                writer.write(meal.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving " + mealType + ": " + e.getMessage());
        }
    }

    public List<Meal> loadMealsFromFile(String mealType) {
        List<Meal> meals = new ArrayList<>();
        File file = new File(MEAL_FOLDER + "/" + mealType + ".txt");
        if (!file.exists()) return meals;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                meals.add(Meal.parseFromFileString(line));
            }
        } catch (IOException e) {
            System.err.println("Error loading " + mealType + ": " + e.getMessage());
        }

        return meals;
    }

    public void modifyMeal(String mealType) {
        Scanner scanner = new Scanner(System.in);
        List<Meal> meals = loadMealsFromFile(mealType);

        if (meals.isEmpty()) {
            System.out.println("No meals found for " + mealType);
            return;
        }

        System.out.println("\n📄 Current meals in " + mealType + ":");
        for (int i = 0; i < meals.size(); i++) {
            System.out.println((i + 1) + ". " + meals.get(i).getName());
        }

        System.out.println("\nChoose an action:");
        System.out.println("1. Remove a meal");
        System.out.println("2. Add a new meal");
        System.out.println("3. Regenerate meals");
        System.out.println("4. Cancel");
        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter meal number to remove: ");
                int index = scanner.nextInt() - 1;
                if (index >= 0 && index < meals.size()) {
                    meals.remove(index);
                    saveMealsToFile(mealType, meals);
                    System.out.println("Meal removed and file updated.");
                } else {
                    System.out.println("Invalid index.");
                }
                break;
            case 2:
                System.out.print("Enter new meal line (e.g. Name;Calories;Ingredients;Fats;Proteins): ");
                String newLine = scanner.nextLine();
                try {
                    meals.add(Meal.parseFromFileString(newLine));
                    saveMealsToFile(mealType, meals);
                    System.out.println("Meal added and file updated.");
                } catch (Exception e) {
                    System.out.println("Invalid format.");
                }
                break;
            case 3:
                MealLoader loader = new MealLoader();
                List<Meal> allMeals = loader.loadMeals();
                Collections.shuffle(allMeals);
                List<Meal> newMeals = allMeals.subList(0, Math.min(3, allMeals.size()));
                saveMealsToFile(mealType, newMeals);
                System.out.println("Meals regenerated and file updated.");
                break;
            default:
                System.out.println("Cancelled.");
        }
    }
}