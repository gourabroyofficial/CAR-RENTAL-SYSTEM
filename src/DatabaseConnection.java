import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DatabaseConnection {
    // Database URL
    private static final String URL = "jdbc:mysql://localhost:3306/car_rental_db";

    // Database credentials
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Admin@123";

    public static Connection connect() {
        try {
            // Attempting to establish a connection to the database
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            return connection; // Return the connection object if the connection is successfully established
        } catch (SQLException e) {
            // Handling database connection failure
            System.out.println("Database connection failed. Please contact support.");
            System.exit(1); // Exit the program if connection fails
        }
        return null; // Return null if connection is not established
    }
}
