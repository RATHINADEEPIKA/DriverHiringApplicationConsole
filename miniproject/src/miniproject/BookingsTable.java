package miniproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingsTable {
    private Connection connection;// Association: BookingsTable has a connection to the database


    public BookingsTable(Connection connection) {
        this.connection = connection;// Encapsulation: Initializing private member 'connection'
    }
    

    public int createBooking(int userId, int driverId, String pickupLocation, String destination,
                             String dates, String days, String bookingStatus, double amount) {
        int bookingId = -1;// Encapsulation: Variable 'bookingId' is limited to this method
        try {
            String sql = "INSERT INTO Bookings (user_id, driver_id, pickup_location, destination, dates, days, booking_status, amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setInt(2, driverId);
            statement.setString(3, pickupLocation);
            statement.setString(4, destination);
            statement.setString(5, dates);
            statement.setString(6, days);
            statement.setString(7, bookingStatus);
            statement.setDouble(8, amount); // Set the booking amount// Encapsulation: Setting the booking amount
            statement.executeUpdate();

            // Get the auto-generated booking ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                bookingId = generatedKeys.getInt(1);// Encapsulation: Updating 'bookingId' within the method
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingId;
    }

    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>(); //Encapsulation: Limited to this method
        try {
        
            String sql = "SELECT * FROM Bookings WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int bookingId = resultSet.getInt("booking_id");
                int driverId = resultSet.getInt("driver_id");
                String pickupLocation = resultSet.getString("pickup_location");
                String destination = resultSet.getString("destination");
                String dates = resultSet.getString("dates");
                String days = resultSet.getString("days");
                String bookingStatus = resultSet.getString("booking_status");
                double amount = resultSet.getDouble("amount");

                Booking booking = new Booking(bookingId, userId, driverId, pickupLocation, destination, dates, days, bookingStatus, amount);
                bookings.add(booking);// Abstraction: Adding booking details to the list
             
            
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Booking getBookingById(int bookingId) {
        Booking booking = null;// Encapsulation: Limited to this method
        try {
            String sql = "SELECT * FROM Bookings WHERE booking_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, bookingId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                int driverId = resultSet.getInt("driver_id");
                String pickupLocation = resultSet.getString("pickup_location");
                String destination = resultSet.getString("destination");
                String dates = resultSet.getString("dates");
                String days = resultSet.getString("days");
                String bookingStatus = resultSet.getString("booking_status");
                double amount = resultSet.getDouble("amount");

                booking = new Booking(bookingId, userId, driverId, pickupLocation, destination, dates, days, bookingStatus, amount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

    public int cancelBooking(int bookingId) {
        try {
            String sql = "UPDATE Bookings SET booking_status = 'cancelled', refund_amount = amount * 0.8, cancellation_timestamp = NOW() WHERE booking_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, bookingId);
            return statement.executeUpdate(); // Encapsulation: Return the update count
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Booking> getUpcomingBookingsByUserId(int userId) {
        List<Booking> upcomingBookings = new ArrayList<>();// Encapsulation: Limited to this method
        
        try {
//         String sql = "SELECT * FROM Bookings WHERE booking_status = 'confirmed' AND dates >= CURRENT_DATE() AND user_id = ?";
        	String sql = "SELECT * FROM Bookings WHERE booking_status = 'confirmed' AND dates <= CURRENT_DATE() AND user_id = ?";
            
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int bookingId = resultSet.getInt("booking_id");
                int driverId = resultSet.getInt("driver_id");
                String pickupLocation = resultSet.getString("pickup_location");
                String destination = resultSet.getString("destination");
                String dates = resultSet.getString("dates");
                String days = resultSet.getString("days");
                String bookingStatus = resultSet.getString("booking_status");
                double amount = resultSet.getDouble("amount");

                Booking booking = new Booking(bookingId, userId, driverId, pickupLocation, destination, dates, days, bookingStatus, amount);
                upcomingBookings.add(booking);// Abstraction: Adding booking details to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upcomingBookings;
    }


    // Add other methods for booking-related operations as needed

    // Sample Booking class for booking details
    public static class Booking {
        private int bookingId;
        private int userId;
        private int driverId;
        private String pickupLocation;
        private String destination;
        private String dates;
        private String days;
        private String bookingStatus;
        private double amount;
        private double refundAmount;

        public Booking(int bookingId, int userId, int driverId, String pickupLocation, String destination,
                       String dates, String days, String bookingStatus, double amount) {
            this.bookingId = bookingId;
            this.userId = userId;
            this.driverId = driverId;
            this.pickupLocation = pickupLocation;
            this.destination = destination;
            this.dates = dates;
            this.days = days;
            this.bookingStatus = bookingStatus;
            this.amount = amount;
            this.refundAmount = 0.0; // Initially set refund amount to 0
        }

		public int getBookingId() {
			return bookingId;
		}

		public void setBookingId(int bookingId) {
			this.bookingId = bookingId;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public int getDriverId() {
			return driverId;
		}

		public void setDriverId(int driverId) {
			this.driverId = driverId;
		}

		public String getPickupLocation() {
			return pickupLocation;
		}

		public void setPickupLocation(String pickupLocation) {
			this.pickupLocation = pickupLocation;
		}

		public String getDestination() {
			return destination;
		}

		public void setDestination(String destination) {
			this.destination = destination;
		}

		public String getDates() {
			return dates;
		}

		public void setDates(String dates) {
			this.dates = dates;
		}

		public String getDays() {
			return days;
		}

		public void setDays(String days) {
			this.days = days;
		}

		public String getBookingStatus() {
			return bookingStatus;
		}

		public void setBookingStatus(String bookingStatus) {
			this.bookingStatus = bookingStatus;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}

		public double getRefundAmount() {
			return refundAmount;
		}

		public void setRefundAmount(double refundAmount) {
			this.refundAmount = refundAmount;
		}

		public Booking(int bookingId, int userId, int driverId, String pickupLocation, String destination, String dates,
				String days, String bookingStatus, double amount, double refundAmount) {
			super();
			this.bookingId = bookingId;
			this.userId = userId;
			this.driverId = driverId;
			this.pickupLocation = pickupLocation;
			this.destination = destination;
			this.dates = dates;
			this.days = days;
			this.bookingStatus = bookingStatus;
			this.amount = amount;
			this.refundAmount = refundAmount;
		}

        // Getters and Setters
        // (Omitted for brevity)
    }
}
