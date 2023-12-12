package miniproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriversTable {
    private Connection connection;// Association: DriversTable has a connection to the database

    public DriversTable() {
   	
   }//default constructor
  public DriversTable(Connection connection) {//parameterized constructor
    	//polymorphism used
        this.connection = connection;// Encapsulation: Initializing private member 'connection'
    }

    public int createDriver(String name, int experience, String vehicleType, double ratings) {
        int driverId = -1;// Encapsulation: Variable 'driverId' is limited to this method
        try {
            String sql = "INSERT INTO Drivers (name, experience, vehicle_type, ratings) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setInt(2, experience);
            statement.setString(3, vehicleType);
            statement.setDouble(4, ratings);
            statement.executeUpdate();

            // Get the auto-generated driver ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                driverId = generatedKeys.getInt(1);// Encapsulation: Updating 'driverId' within the method
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driverId;
    }

    public DriversTable.Driver getDriverById(int driverId) {
        DriversTable.Driver driver = null;// Encapsulation: Limited to this method
        try {
            String sql = "SELECT * FROM Drivers WHERE driver_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, driverId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int experience = resultSet.getInt("experience");
                String vehicleType = resultSet.getString("vehicle_type");
                double ratings = resultSet.getDouble("ratings");
                driver = new DriversTable.Driver(driverId, name, experience, vehicleType, ratings);// Abstraction: Creating a Driver object with provided details
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driver;
    }

    public List<DriversTable.Driver> getAllDrivers() {
        List<DriversTable.Driver> drivers = new ArrayList<>();// Encapsulation: Limited to this method
        try {
            String sql = "SELECT * FROM Drivers";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int driverId = resultSet.getInt("driver_id");
                String name = resultSet.getString("name");
                int experience = resultSet.getInt("experience");
                String vehicleType = resultSet.getString("vehicle_type");
                double ratings = resultSet.getDouble("ratings");
                drivers.add(new DriversTable.Driver(driverId, name, experience, vehicleType, ratings));// Abstraction: Creating a Driver object with provided details
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    public void updateDriver(DriversTable.Driver driver) {
        try {
            String sql = "UPDATE Drivers SET name=?, experience=?, vehicle_type=?, ratings=? WHERE driver_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, driver.getName());
            statement.setInt(2, driver.getExperience());
            statement.setString(3, driver.getVehicleType());
            statement.setDouble(4, driver.getRatings());
            statement.setInt(5, driver.getDriverId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDriver(int driverId) {
        try {
            String sql = "DELETE FROM Drivers WHERE driver_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, driverId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // You can add other methods for additional driver-related operations as needed.

    // Sample Driver class for driver details
    public static class Driver {
        private int driverId;// Encapsulation: Private members of the Driver class
        private String name;
        private int experience;
        private String vehicleType;
        private double ratings;

        public Driver(int driverId, String name, int experience, String vehicleType, double ratings) {
            this.driverId = driverId;
            this.name = name;
            this.experience = experience;
            this.vehicleType = vehicleType;
            this.ratings = ratings;
        }

        // Getters and Setters
        public int getDriverId() {
            return driverId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public double getRatings() {
            return ratings;
        }

        public void setRatings(double ratings) {
            this.ratings = ratings;
        }

        // Override toString() for easy printing
        @Override
        public String toString() {
            return "Driver{" +
                    "driverId=" + driverId +
                    ", name='" + name + '\'' +
                    ", experience=" + experience +
                    ", vehicleType='" + vehicleType + '\'' +
                    ", ratings=" + ratings +
                    '}';
        }
    }
}
