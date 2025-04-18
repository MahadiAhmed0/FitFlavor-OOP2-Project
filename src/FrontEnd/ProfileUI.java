package FrontEnd;

import BackEnd.User;
import BackEnd.UserProfileManager;

import java.util.Scanner;

public class ProfileUI {
    private final Scanner scanner;
    private final String loggedInUsername;
    private final UserProfileManager profileManager;

    public ProfileUI(Scanner scanner, String loggedInUsername) {
        this.scanner = scanner;
        this.loggedInUsername = loggedInUsername;
        this.profileManager = new UserProfileManager();
    }

    public void showProfileMenu() {
        while (true) {
            System.out.println("\n=== 👤 Profile Management ===");
            System.out.println("1. View Profile");
            System.out.println("2. Update Password");
            System.out.println("3. Update Age");
            System.out.println("4. Update Weight");
            System.out.println("5. Back to Dashboard");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    updatePassword();
                    break;
                case 3:
                    updateAge();
                    break;
                case 4:
                    updateWeight();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewProfile() {
        User user = profileManager.getUserDetails(loggedInUsername);
        if (user != null) {
            System.out.println("\n--- Your Profile ---");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Age: " + user.getAge());
            System.out.println("Weight: " + user.getWeight());
        } else {
            System.out.println("Error retrieving user profile.");
        }
    }

    private void updatePassword() {
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        if (newPassword.length() < 6) {
            System.out.println("Password must be at least 6 characters.");
            return;
        }
        if (profileManager.updateUser(loggedInUsername, "password", newPassword)) {
            System.out.println("Password updated.");
        } else {
            System.out.println("Failed to update password.");
        }
    }

    private void updateAge() {
        System.out.print("Enter new age: ");
        int newAge = scanner.nextInt();
        scanner.nextLine();
        if (profileManager.updateUser(loggedInUsername, "age", newAge)) {
            System.out.println("Age updated.");
        } else {
            System.out.println("Failed to update age.");
        }
    }

    private void updateWeight() {
        System.out.print("Enter new weight: ");
        double newWeight = scanner.nextDouble();
        scanner.nextLine();
        if (profileManager.updateUser(loggedInUsername, "weight", newWeight)) {
            System.out.println("Weight updated.");
        } else {
            System.out.println("Failed to update weight.");
        }
    }
}
