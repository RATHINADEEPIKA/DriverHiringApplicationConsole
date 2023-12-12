package miniproject;

import java.sql.Connection;
import java.util.Scanner;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        AppDatabase appDatabase = new AppDatabase();
        Connection connection = appDatabase.getConnection();

        UsersTable usersTable = new UsersTable(connection);
        DriversTable driversTable = new DriversTable(connection);
        BookingsTable bookingsTable = new BookingsTable(connection);
        Driver_LocationTable driver_LocationTable = new Driver_LocationTable(connection);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice (1/2/3/4/5/6): ");

            switch (choice) {
                case 1:
                    hireDriver(usersTable, driversTable, bookingsTable, scanner);
                    break;
                case 2:
                    viewUpcomingBookings(bookingsTable, scanner);
                    break;
                case 3:
                    cancelBooking(bookingsTable, scanner);
                    break;
                case 4:
                    viewDriverLocation(driver_LocationTable, scanner);
                    break;
                case 5:
                    addDriverLocation(driver_LocationTable, scanner); // New option for adding driver location
                    break;
                case 6:
                    System.out.println("Exiting the application.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        scanner.close();
        appDatabase.close();
    }

    private static void displayMenu() {
        System.out.println("\n===== Console-Based Application =====");
        System.out.println("1. Hire a Driver");
        System.out.println("2. View Upcoming Bookings");
        System.out.println("3. Cancel Booking");
        System.out.println("4. View Driver Locations"); // Combined with the previous "View Driver Locations" option
        System.out.println("5. Add Driver Location"); // New option for adding driver location
        System.out.println("6. Exit");
        System.out.println("=====================================");
    }

    private static int getIntInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid integer: ");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return input;
    }

    private static String getStringInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextLine();
    }

    private static double getDoubleInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. Please enter a valid number: ");
            scanner.next();
        }
        double input = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character
        return input;
    }
    private static void addDriverLocation(Driver_LocationTable driver_LocationTable, Scanner scanner) {
        // Implement the logic for adding a driver's location
        // Example code:
        int driverId = getIntInput("Enter the driver ID whose location you want to add: ");
        double latitude = getDoubleInput("Enter the latitude: ");
        double longitude = getDoubleInput("Enter the longitude: ");
        Driver_LocationTable.DriverLocation driverLocation = new Driver_LocationTable.DriverLocation(driverId, latitude, longitude);

        // Save the driver location to the database
        boolean success = driver_LocationTable.addDriverLocation(driverLocation);

        if (success) {
            System.out.println("Driver location added successfully!");
        } else {
            System.out.println("Failed to add driver location.");
        }
    }
    private static void hireDriver(UsersTable usersTable, DriversTable driversTable, BookingsTable bookingsTable, Scanner scanner) {
        // Implement the logic for hiring a driver
        // Example code:
        String username = getStringInput("Enter your username: ");
        String email = getStringInput("Enter your email: ");
        String password = getStringInput("Enter your password: ");
        // ... other inputs
        int userId = usersTable.createUser(username, email, password);

        String driverName = getStringInput("Enter driver's name: ");
        int experience = getIntInput("Enter driver's experience (in years): ");
        String vehicleType = getStringInput("Enter driver's vehicle type: ");
        double ratings = getDoubleInput("Enter driver's ratings: ");
        // ... other inputs
        int driverId = driversTable.createDriver(driverName, experience, vehicleType, ratings);

        // Get pickup and destination locations from the user
        String pickupLocation = getStringInput("Enter pickup location: ");
        String destination = getStringInput("Enter destination: ");

        // Assuming the driver selection logic here based on preferences
        // We will just assign a driver ID for simplicity in this example
        int bookingId = bookingsTable.createBooking(userId, driverId, pickupLocation, destination, "2023-10-17", "Tuesday", "confirmed", 100.0);

        System.out.println("Booking confirmed! Driver is hired for your ride on 2023-10-17 (Tuesday) from " + pickupLocation + " to " + destination + ".");
    }

    private static void viewUpcomingBookings(BookingsTable bookingsTable, Scanner scanner) {
        // Implement the logic for viewing upcoming bookings
        // Example code:
        int userId = getIntInput("Enter your user ID: ");

        List<BookingsTable.Booking> bookings = bookingsTable.getUpcomingBookingsByUserId(userId);
        boolean foundBooking = false;
        for (BookingsTable.Booking booking : bookings) {
            System.out.println("Booking ID: " + booking.getBookingId() + " | Driver ID: " + booking.getDriverId()
                    + " | Date: " + booking.getDates() + " | Status: " + booking.getBookingStatus());
            System.out.println("Pickup: " + booking.getPickupLocation() + " | Destination: " + booking.getDestination());
            foundBooking = true;
        }

        if (!foundBooking) {
            System.out.println("No upcoming bookings found for the user.");
        }
    }

    private static void getDriverLocationById(Driver_LocationTable driver_LocationTable, Scanner scanner) {
        // Implement the logic for getting a driver's location by ID
        // Example code:
        int driverId = getIntInput("Enter the driver ID whose location you want to get: ");
        Driver_LocationTable.DriverLocation driverLocation = driver_LocationTable.getDriverLocationById(driverId);
        if (driverLocation != null) {
            System.out.println("Driver ID: " + driverLocation.getDriverId());
            System.out.println("Latitude: " + driverLocation.getLatitude());
            System.out.println("Longitude: " + driverLocation.getLongitude());
        } else {
            System.out.println("Driver location not found for the provided driver ID.");
        }
    }

    private static void viewDriverLocation(Driver_LocationTable driver_LocationTable, Scanner scanner) {
        int driverId = getIntInput("Enter the driver ID to view locations: ");

        List<Driver_LocationTable.DriverLocation> driverLocations = driver_LocationTable.getDriverLocations(driverId);

        if (!driverLocations.isEmpty()) {
            System.out.println("Driver locations for driver ID " + driverId + ":");
            for (Driver_LocationTable.DriverLocation location : driverLocations) {
                System.out.println("Latitude: " + location.getLatitude() + " | Longitude: " + location.getLongitude());
            }
        } else {
            System.out.println("No driver locations found for the provided driver ID.");
        }
    }

    private static void cancelBooking(BookingsTable bookingsTable, Scanner scanner) {
        // Implement the logic for canceling a booking
        // Example code:
        int bookingId = getIntInput("Enter the booking ID you want to cancel: ");
        int rowsAffected = bookingsTable.cancelBooking(bookingId);

        if (rowsAffected > 0) {
            System.out.println("Booking with ID " + bookingId + " has been cancelled.");
        } else {
            System.out.println("No valid booking found with the provided booking ID.");
        }
    }
}
