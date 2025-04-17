package FrontEnd;

import java.util.Scanner;

public class AuthenticationUI {
    private final UserAccountManager manager;
    private final Scanner scanner;

    public AuthenticationUI() {
        manager = new UserAccountManager();
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            showMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    manager.createAccount();
                    break;
                case 2:
                    manager.loginAccount();
                    HomePageUI home = new HomePageUI(scanner);
                    home.showDashboard();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n=== User Menu ===");
        System.out.println("1. Create Account");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    private int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // discard invalid input
            showMenu();
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // clear buffer
        return choice;
    }
}
