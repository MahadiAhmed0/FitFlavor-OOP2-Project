package BackEnd;

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

    public boolean isPasswordValid(String password){
        return password.length() >= 6;
    }
    
}
