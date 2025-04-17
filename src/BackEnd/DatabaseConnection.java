package BackEnd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final Connection connection;

    private DatabaseConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/food";
        String user = "root";
        String password = "Msdef@S3";

        this.connection = DriverManager.getConnection(url, user, password);
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}