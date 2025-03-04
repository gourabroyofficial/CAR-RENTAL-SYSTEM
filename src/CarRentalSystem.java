import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class CarRentalSystem {
    private Scanner scanner;
    private Connection connection;

    public CarRentalSystem() throws InterruptedException {
        // Initializes scanner and establishes database connection
        scanner = new Scanner(System.in);
        connection = DatabaseConnection.connect();
    }

    // Displays the main menu and handles user input
    public void displayMenu() throws InterruptedException, SQLException {
        while (true) {
            System.out.println("\n=======================================");
            System.out.println("|        CAR RENTAL SYSTEM            |");
            System.out.println("=======================================");
            System.out.println("| 1. View Available Cars              |");
            System.out.println("| 2. Rent a Car                       |");
            System.out.println("| 3. View Rental Status               |");
            System.out.println("| 4. Return a Car                     |");
            System.out.println("| 5. Exit                             |");
            System.out.println("=======================================\n");
            System.out.print("Enter your choice: ");

            int choice = scanner.hasNextInt() ? scanner.nextInt() : -1;
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showAvailableCars();
                    break;
                case 2:
                    rentCar();
                    break;
                case 3:
                    showRentalStatus();
                    break;
                case 4:
                    returnCar();
                    break;
                case 5:
                    exit();
                    return;
                default:
                    System.out.println("Invalid choice! Please enter a number between 1-5.");
            }
        }
    }

    // Displays the list of available cars from the database
    private void showAvailableCars() throws SQLException {
        // SQL query to fetch all available cars from the database
        String query = "SELECT * FROM cars WHERE is_available = TRUE";

        try (Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(query)) {

            // Check if there are available cars
            if (!rs.isBeforeFirst()) {
                System.out.println("\nNo cars available for rent at the moment.");
                return;
            }

            // Initialize column width variables for formatting table output
            int maxCarId = "Car ID".length();
            int maxBrand = "Brand".length();
            int maxModel = "Model".length();
            int maxPrice = "Price per Day".length();

            // First pass: Determine the maximum column width for each column
            while (rs.next()) {
                maxCarId = Math.max(maxCarId, rs.getString("car_id").length());
                maxBrand = Math.max(maxBrand, rs.getString("brand").length());
                maxModel = Math.max(maxModel, rs.getString("model").length());
                maxPrice = Math.max(maxPrice, String.format("$%.2f", rs.getDouble("price")).length());
            }

            // Create a formatted string for table display
            String format = "| %-" + maxCarId + "s | %-" + maxBrand + "s | %-" + maxModel + "s | %-" + maxPrice
                    + "s |%n";
            String rowBorder = "+-" + "-".repeat(maxCarId) + "-+-" + "-".repeat(maxBrand) + "-+-" +
                    "-".repeat(maxModel) + "-+-" + "-".repeat(maxPrice) + "-+";

            // Print table header
            System.out.println("\nAvailable Cars:");
            System.out.println(rowBorder);
            System.out.printf(format, "Car ID", "Brand", "Model", "Price per Day");
            System.out.println(rowBorder);

            // Reset cursor to the beginning of the result set
            rs.beforeFirst();

            // Second pass: Print each row of the table
            while (rs.next()) {
                String carId = rs.getString("car_id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String price = String.format("$%.2f", rs.getDouble("price"));

                System.out.printf(format, carId, brand, model, price);
                System.out.println(rowBorder);
            }
        }
    }

    // Allows customers to return a rented car and updates the database
    private void rentCar() throws SQLException {
        // Request the user to enter their name
        System.out.print("\nEnter your name: ");
        String customerName = scanner.nextLine().trim();

        // Validate the customer name (only letters and spaces allowed)
        if (!customerName.matches("[a-zA-Z ]+")) {
            System.out.println("Invalid name! Please enter a valid name.");
            return;
        }

        // Retrieve the customer ID, if they exist
        int customerId = getCustomerId(customerName);

        // Start a transaction
        connection.setAutoCommit(false);
        boolean rentalSuccess = false;

        try {
            // If the customer is new, insert them into the database
            if (customerId == -1) {
                String insertQuery = "INSERT INTO customers (name) VALUES (?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertQuery,
                        Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, customerName);
                    pstmt.executeUpdate();
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        customerId = rs.getInt(1);
                    }
                }
            }

            // Display available cars
            showAvailableCars();

            // Request the user to choose car(s) to rent
            System.out.print("\nEnter Car IDs to rent (Use a comma to separate multiple car IDs): ");
            String carIdsInput = scanner.nextLine().trim();
            String[] carIds = carIdsInput.split("\\s*,\\s*"); // Split by commas and trim spaces

            double totalCost = 0;

            for (String carId : carIds) {
                // Fetch the price of the selected car
                String priceQuery = "SELECT price FROM cars WHERE car_id = ? AND is_available = TRUE";
                try (PreparedStatement pstmt = connection.prepareStatement(priceQuery)) {
                    pstmt.setString(1, carId);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        double pricePerDay = rs.getDouble("price");
                        System.out.print("\nEnter number of days to rent Car ID " + carId + ": ");
                        int days = scanner.hasNextInt() ? scanner.nextInt() : 0;
                        scanner.nextLine();

                        if (days <= 0) {
                            System.out.println("\nInvalid number of days for Car ID " + carId);
                            connection.rollback();
                            return;
                        }

                        double totalPrice = days * pricePerDay;
                        totalCost += totalPrice;

                        // Insert rental information into the database
                        String rentQuery = "INSERT INTO rentals (customer_id, car_id, days, total_price) VALUES (?, ?, ?, ?)";
                        String updateCarQuery = "UPDATE cars SET is_available = FALSE WHERE car_id = ?";

                        try (PreparedStatement rentStmt = connection.prepareStatement(rentQuery);
                                PreparedStatement updateStmt = connection.prepareStatement(updateCarQuery)) {

                            rentStmt.setInt(1, customerId);
                            rentStmt.setString(2, carId);
                            rentStmt.setInt(3, days);
                            rentStmt.setDouble(4, totalPrice);
                            rentStmt.executeUpdate();

                            updateStmt.setString(1, carId);
                            updateStmt.executeUpdate();

                            rentalSuccess = true;
                            System.out.println("\nCar ID " + carId + " rented successfully! Cost: $" + totalPrice);
                        }
                    } else {
                        System.out.println("\nCar ID " + carId + " is not available.");
                        connection.rollback();
                        return;
                    }
                }
            }

            // Commit the transaction if successful
            if (rentalSuccess) {
                connection.commit();
                System.out.println("\nTotal rental cost: $" + totalCost);
            } else {
                connection.rollback();
            }

        } catch (SQLException e) {
            connection.rollback(); // Rollback transaction in case of an error
            System.err.println("Error processing rental: " + e.getMessage());
        } finally {
            connection.setAutoCommit(true); // Reset auto-commit mode
        }
    }

    // Retrieves the customer ID from the database if the customer exists, otherwise
    // returns -1
    private int getCustomerId(String customerName) throws SQLException {
        // Query to check if the customer exists in the database
        String checkQuery = "SELECT customer_id FROM customers WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(checkQuery)) {
            pstmt.setString(1, customerName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("customer_id"); // Return existing customer ID
            }
        }

        return -1; // Indicate that the customer does not exist
    }

    // Allows customers to return a rented car and updates the database
    private void returnCar() throws SQLException {
        // Query to fetch currently rented cars
        String rentedCarsQuery = "SELECT r.car_id, cars.brand, cars.model " +
                "FROM rentals r " +
                "JOIN cars ON r.car_id = cars.car_id " +
                "WHERE r.rental_status = 'Active'";

        try (Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(rentedCarsQuery)) {

            // Check if there are any active rentals
            if (!rs.isBeforeFirst()) {
                System.out.println("\nNo rented cars available for return.");
                return;
            }

            // Display table header
            System.out.println("\nCurrently Rented Cars:");
            String rowBorder = "+----------+---------------+--------------+";

            System.out.println(rowBorder);
            System.out.printf("| %-8s | %-13s | %-12s |\n", "Car ID", "Brand", "Model");
            System.out.println(rowBorder);

            // Flag to check if entered Car ID is valid
            boolean carExists = false;

            // Print the table data
            while (rs.next()) {
                String carId = rs.getString("car_id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");

                // Print car details in tabular format
                System.out.printf("| %-8s | %-13s | %-12s |\n", carId, brand, model);
                System.out.println(rowBorder);
            }

            // Move cursor back to the beginning to validate user input
            rs.beforeFirst();

            // Request user for Car ID to return
            System.out.print("\nEnter Car ID to return: ");
            String carIdToReturn = scanner.nextLine().trim();

            // Check if entered Car ID is valid
            while (rs.next()) {
                if (rs.getString("car_id").equalsIgnoreCase(carIdToReturn)) {
                    carExists = true;
                    break;
                }
            }

            // If entered Car ID is not found in active rentals
            if (!carExists) {
                System.out.println("\nInvalid Car ID or the car is not currently rented.");
                return;
            }

            // Queries to update car availability and rental status
            String updateCarQuery = "UPDATE cars SET is_available = TRUE WHERE car_id = ?";
            String updateRentalQuery = "UPDATE rentals SET rental_status = 'Returned' WHERE car_id = ? AND rental_status = 'Active'";

            // Execute update queries
            try (PreparedStatement updateCarStmt = connection.prepareStatement(updateCarQuery);
                    PreparedStatement updateRentalStmt = connection.prepareStatement(updateRentalQuery)) {

                updateCarStmt.setString(1, carIdToReturn);
                updateCarStmt.executeUpdate();

                updateRentalStmt.setString(1, carIdToReturn);
                updateRentalStmt.executeUpdate();

                System.out.println("\nCar returned successfully.");
            }
        }
    }

    // Displays the list of active car rentals along with customer details
    private void showRentalStatus() throws SQLException {
        // Query to check if there are active rentals
        String checkQuery = "SELECT COUNT(*) AS count FROM rentals WHERE rental_status = 'Active'";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(checkQuery)) {
            // If no active rentals exist
            if (rs.next() && rs.getInt("count") == 0) {
                System.out.println("\nNo active rentals available.");
                return;
            }
        }

        // Query to fetch rental details including customer name
        String query = "SELECT r.car_id, cars.brand, cars.model, c.name, r.days " +
                "FROM rentals r " +
                "JOIN cars ON r.car_id = cars.car_id " +
                "JOIN customers c ON r.customer_id = c.customer_id " +
                "WHERE r.rental_status = 'Active'"; // Only show active rentals

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\nRental Status:");

            // Display table header with proper formatting
            String rowBorder = "+----------+-----------------+-----------------+----------------------+----------------------+";

            System.out.println(rowBorder);
            System.out.printf("| %-8s | %-15s | %-15s | %-20s | %-20s |\n",
                    "Car ID", "Brand", "Model", "Customer Name", "No. of Days Rented");
            System.out.println(rowBorder);

            // Iterate through the result set and display rental details
            while (rs.next()) {
                System.out.printf("| %-8s | %-15s | %-15s | %-20s | %-20d |\n",
                        rs.getString("car_id"), rs.getString("brand"), rs.getString("model"),
                        rs.getString("name"), rs.getInt("days"));
                System.out.println(rowBorder);
            }
        }
    }

    // Exits the system with a countdown animation effect
    public void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        // Countdown animation effect
        while (i != 0) {
            System.out.print(".");

            // Delay for smooth effect
            Thread.sleep(230);
            i--;
        }
        System.out.println();
        System.out.println("Thank You For Using Car Rental System!!!");
    }
}
