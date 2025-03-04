import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws InterruptedException, SQLException {
        // Creating an instance of the CarRentalSystem
        CarRentalSystem rentalSystem = new CarRentalSystem();

        // Calling the method to display the main menu of the car rental system
        rentalSystem.displayMenu();
    }
}
