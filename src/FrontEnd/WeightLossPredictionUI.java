package FrontEnd;

import BackEnd.User;
import BackEnd.WeightLossPredictor;

import java.sql.SQLException;
import java.util.Scanner;

public class WeightLossPredictionUI {
    private final Scanner scanner;
    private final String username;

    public WeightLossPredictionUI(Scanner scanner, String username) {
        this.scanner = scanner;
        this.username = username;
    }

    public void showWeightLossPredictionPage() {
        try {
            WeightLossPredictor predictor = new WeightLossPredictor(username);
            User user = predictor.getUserData();
            if (user == null) {
                System.out.println("User not found!");
                return;
            }

            
            System.out.println("\n=== 🔍 Weekly Weight Loss Estimator ===");
            double bmr = predictor.calculateBMR(user);
            double bmi = predictor.calculateBMI(user);
            double idealWeight = predictor.calculateIdealWeight(user);

            System.out.printf("💪 BMR (Basal Metabolic Rate): %.2f kcal/day\n", bmr);
            System.out.printf("📏 BMI (Body Mass Index): %.2f\n", bmi);
            System.out.printf("⚖️ Ideal Weight for your height: %.2f kg\n", idealWeight);
            System.out.println("\nNow, let's calculate your weekly weight loss based on exercise...");

            
            System.out.print("Enter average exercise time per week (in hours): ");
            double hours = scanner.nextDouble();

            
            double weightLoss = predictor.estimateWeightLoss(user, hours);
            System.out.printf("📉 Estimated weight loss for the week: %.2f kg\n", weightLoss);

            double currentWeight = user.getWeight();
            double targetWeight = currentWeight - weightLoss;
            System.out.printf("🏃‍♂️ Your target weight after a week: %.2f kg\n", targetWeight);
            if (currentWeight > idealWeight) {
                double weightToLose = currentWeight - idealWeight;
                System.out.printf("💡 You need to reduce %.2f kg to reach your ideal weight.\n", weightToLose);
            } else {
                System.out.println("✅ You're already at or below your ideal weight!");
            }
        } catch (SQLException e) {
            System.out.println("Error accessing the database: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
