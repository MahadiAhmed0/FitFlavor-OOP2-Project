package BackEnd;

import java.util.HashMap;

public class Authenticator {
    private HashMap<String, User> userDatabase;
    private static final String File_Name = "user_database.txt";

    public Authenticator() {
        userDatabase = new HashMap<>();
        loadUsersFromFile();
    }
    
}
