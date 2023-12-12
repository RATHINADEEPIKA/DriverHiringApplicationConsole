package miniproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersTable {
    private Connection connection;// Association: UsersTable has a connection to the database


    public UsersTable(Connection connection) {
        this.connection = connection;// Encapsulation: Initializing private member 'connection'
    }

    public int createUser(String username, String email, String password) {
        int userId = -1;// Encapsulation: Variable 'userId' is limited to this method
        try {
            String sql = "INSERT INTO Users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.executeUpdate();

            // Get the auto-generated user ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);// Encapsulation: Updating 'userId' within the method
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public void updateUser(int userId, String username, String email, String password) {
        try {
            String sql = "UPDATE Users SET username = ?, email = ?, password = ? WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setInt(4, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        try {
            String sql = "DELETE FROM Users WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(int userId) {
        User user = null;// Encapsulation: Limited to this method
        try {
            String sql = "SELECT * FROM Users WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                user = new User(userId, username, email, password);
             // Abstraction: Creating a User object with provided details
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Add other methods for user-related operations as needed

    // Sample User class for user details
    public static class User {
        private int userId;// Encapsulation: Private members of the User class
        private String username;
        private String email;
        private String password;

        public User(int userId, String username, String email, String password) {
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.password = password;
        }

        // Getters and Setters
        public int getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        // Override toString() for easy printing
        @Override
        public String toString() {
            return "User{" +
                    "userId=" + userId +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
