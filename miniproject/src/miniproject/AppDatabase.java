package miniproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AppDatabase {
	// Private constants holding database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/miniproject";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private Connection connection;

    public AppDatabase() {
        try {
        	// Using the concept of Encapsulation, the implementation details of database connection
            // are hidden from external users.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
