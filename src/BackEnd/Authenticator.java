package BackEnd;

import java.io.*;
import java.util.HashMap;

public class Authenticator {
    private HashMap<String, User> userDatabase;
    private static final String File_Name = "user_database.txt";

    public Authenticator() {
        userDatabase = new HashMap<>();
        loadUsersFromFile();
    }

    public boolean isUsernameTaken(String username) {
        return userDatabase.containsKey(username);
    }

    public boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    public void addUser(User user) {
        userDatabase.put(user.getUsername(), user);
        saveUserToFile(user);
    }

    public User getUser(String username) {
        return userDatabase.get(username);
    }

    public boolean authenticate(String username, String password) {
        User user = getUser(username);
        return user != null && user.checkPassword(password);
    }

    public void saveUserToFile(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(File_Name, true))) {
            writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getAge() + "," + user.getWeight());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error saving user to file: " + e.getMessage(), e);
        }
    }

    private void loadUsersFromFile() {
        File file = new File(File_Name);
        if (!file.exists()) {
            System.out.println("File not found. Creating a new file.");
            try {
                file.createNewFile(); // Creates an empty file if it doesn't exist
            } catch (IOException e) {
                throw new RuntimeException("Failed to create the file: " + e.getMessage(), e);
            }
            return; // No need to load anything if the file is newly created
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(File_Name))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                User user = new User(data[0], data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3]));
                userDatabase.put(user.getUsername(), user);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading users from file: " + e.getMessage(), e);
        }
    }
}