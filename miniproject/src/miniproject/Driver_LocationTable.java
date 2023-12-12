package miniproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Driver_LocationTable {
    private Connection connection;

    public Driver_LocationTable(Connection connection) {
        this.connection = connection;
    }

    public DriverLocation getDriverLocationById(int driverId) {//Abstraction
        DriverLocation driverLocation = null;
        try {
            String sql = "SELECT * FROM Driver_Location WHERE driver_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, driverId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double latitude = resultSet.getDouble("latitude");
                double longitude = resultSet.getDouble("longitude");
                driverLocation = new DriverLocation(driverId, latitude, longitude);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driverLocation;
    }

    public List<DriverLocation> getDriverLocations(int driverId) {//Abstraction
        List<DriverLocation> driverLocations = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Driver_Location WHERE driver_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, driverId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                double latitude = resultSet.getDouble("latitude");
                double longitude = resultSet.getDouble("longitude");
                DriverLocation location = new DriverLocation(driverId, latitude, longitude);
                driverLocations.add(location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driverLocations;
    }

    // Add other methods related to driver location as needed

    public boolean addDriverLocation(DriverLocation driverLocation) {//Abstraction
        try {
            String sql = "INSERT INTO Driver_Location (driver_id, latitude, longitude) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, driverLocation.getDriverId());
            statement.setDouble(2, driverLocation.getLatitude());
            statement.setDouble(3, driverLocation.getLongitude());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class DriverLocation {
        private int driverId;
        private double latitude;
        private double longitude;

        public DriverLocation(int driverId, double latitude, double longitude) {
            this.driverId = driverId;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public int getDriverId() {
            return driverId;
        }

        public void setDriverId(int driverId) {
            this.driverId = driverId;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        // Getters and Setters
        // (Omitted for brevity)
    }
}
