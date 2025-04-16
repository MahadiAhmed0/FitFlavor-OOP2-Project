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

        System.out.print("Enter age: ");
        int age = scanner.nextInt();

        System.out.print("Enter weight: ");
        double weight = scanner.nextDouble();
        scanner.nextLine(); // clear buffer

        User newUser = new User(username, password, age, weight);
        authenticator.addUser(newUser);
        System.out.println("Account created successfully.");
    }

    public void loginAccount() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (authenticator.authenticate(username, password)) {
            System.out.println("Login successful. Welcome, " + username + "!");
        } else {
            System.out.println("Invalid credentials.");
        }
    }
    
}
