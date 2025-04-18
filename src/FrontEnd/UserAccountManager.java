package FrontEnd;

import BackEnd.Authenticator;
import BackEnd.User;

import java.util.Scanner;

public class UserAccountManager {
    private Authenticator authenticator;
    private Scanner scanner;

    public UserAccountManager() {
        authenticator = new Authenticator();
        scanner = new Scanner(System.in);
    }

    public void createAccount() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (authenticator.isUsernameTaken(username)) {
            System.out.println("Username already taken. Try another one.");
            return;
        }

        System.out.print("Enter password (min 6 characters): ");
        String password = scanner.nextLine();

        if (!authenticator.isPasswordValid(password)) {
            System.out.println("Password too weak.");
            return;
        }

        int age = promptInt("Enter age: ");
        double weight = promptDouble("Enter weight: ");

        User newUser = new User(username, password, age, weight);
        authenticator.addUser(newUser);
        System.out.println("Account created successfully.");
    }


    public String loginAccount() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (authenticator.authenticate(username, password)) {
            System.out.println("Login successful. Welcome, " + username + "!");
        } else {
            System.out.println("Invalid credentials.");
        }
        return username;
    }

    private int promptInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }
    }

    private double promptDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid number.");
            }
        }
    }


}
