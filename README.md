# Car Rental System

## Overview
The **Car Rental System** is a Java-based console application that allows users to rent and return cars. It uses **JDBC** for database interaction with MySQL and follows an intuitive menu-driven approach. The system enables users to view available cars, rent multiple cars, return rented cars, and check rental status.
## Features

- **View Available Cars:** Users can browse a list of cars that are currently available for rent.

- **Rent Multiple Cars at Once:** Allows users to rent a single car or multiple cars in a single transaction.

- **View Rental Status:** Displays a user's active rental status.

- **Return Rented Cars:** Users can return rented cars, and the system updates availability of cars accordingly.

- **Persistent Database Storage:** Uses MySQL for storing car, customer, and rental data.

- **Optimized Queries:** Uses efficient SQL queries to manage data retrieval and updates.

## Technologies Used
- **Java** (JDBC for database connectivity)
- **MySQL** (for storing car, customer, and rental data)

## Prerequisites
- Java (JDK 8+)
- MySQL Server
- MySQL Connect
- IDE (Eclipse, IntelliJ IDEA, or VS Code with Java extensions)



## Installation and Setup

### 1. Database Setup

1. Open MySQL Workbench or any SQL client.
2. Execute the SQL script `car_rental_db.sql` to create the necessary database and tables.
3. Ensure that MySQL is running and accessible.

### 2. Configure Database Connection

Edit the `DatabaseConnection.java` file and update the database credentials:

```java
private static final String URL = "jdbc:mysql://localhost:3306/car_rental_db";
private static final String USERNAME = "root";
private static final String PASSWORD = "Admin@123";
```

### 3. Compile and Run the Project

Navigate to the `src` folder and run the following commands:

1. Compile the Java files:
   ```sh
   javac *.java
   ```
2. Run the application:
   ```sh
   java Main
   ```

## Usage Guide

- **View Available Cars**: Lists all cars available for rent.
- **Rent a Car**: Allows users to rent multiple cars by specifying their IDs and rental duration.
- **View Rental Status**: Shows active rentals with details of the customer and rented cars.
- **Return a Car**: Displays currently rented cars and allows the user to return them.
- **Exit**: Closes the system.

## Outputs

### 1. Viewing Available Cars  
```plaintext
=======================================
|        CAR RENTAL SYSTEM            |
=======================================
| 1. View Available Cars              |
| 2. Rent a Car                       |
| 3. View Rental Status               |
| 4. Return a Car                     |
| 5. Exit                             |
=======================================

Enter your choice: 1

Available Cars:
+--------+-------------+-------------+---------------+
| Car ID | Brand       | Model       | Price per Day |
+--------+-------------+-------------+---------------+
| C001   | Toyota      | Corolla     | $50.00        |
+--------+-------------+-------------+---------------+
| C002   | Honda       | Civic       | $55.00        |
+--------+-------------+-------------+---------------+
| C003   | BMW         | X5          | $120.00       |
+--------+-------------+-------------+---------------+
| C004   | Audi        | A4          | $100.00       |
+--------+-------------+-------------+---------------+
| C005   | Kia         | Sportage    | $60.00        |
+--------+-------------+-------------+---------------+
| C006   | Volkswagen  | Jetta       | $52.00        |
+--------+-------------+-------------+---------------+
| C007   | Lexus       | RX 350      | $125.00       |
+--------+-------------+-------------+---------------+
| C008   | Jaguar      | XF          | $140.00       |
+--------+-------------+-------------+---------------+
| C009   | Porsche     | 911         | $200.00       |
+--------+-------------+-------------+---------------+
| C010   | Lamborghini | Huracan     | $350.00       |
+--------+-------------+-------------+---------------+
| C011   | Ferrari     | 488         | $400.00       |
+--------+-------------+-------------+---------------+
| C012   | Bugatti     | Chiron      | $1000.00      |
+--------+-------------+-------------+---------------+
| C013   | Ford        | Mustang     | $90.00        |
+--------+-------------+-------------+---------------+
| C014   | Dodge       | Challenger  | $95.00        |
+--------+-------------+-------------+---------------+
| C015   | Land Rover  | Range Rover | $160.00       |
+--------+-------------+-------------+---------------+
```


### 2. Renting a Car  
```plaintext
=======================================
|        CAR RENTAL SYSTEM            |
=======================================
| 1. View Available Cars              |
| 2. Rent a Car                       |
| 3. View Rental Status               |
| 4. Return a Car                     |
| 5. Exit                             |
=======================================

Enter your choice: 2
Enter your name: Rajesh Sharma

Available Cars:
+--------+-------------+-------------+---------------+
| Car ID | Brand       | Model       | Price per Day |
+--------+-------------+-------------+---------------+
| C001   | Toyota      | Corolla     | $50.00        |
+--------+-------------+-------------+---------------+
| C002   | Honda       | Civic       | $55.00        |
+--------+-------------+-------------+---------------+
| C003   | BMW         | X5          | $120.00       |
+--------+-------------+-------------+---------------+
| C004   | Audi        | A4          | $100.00       |
+--------+-------------+-------------+---------------+
| C005   | Kia         | Sportage    | $60.00        |
+--------+-------------+-------------+---------------+
| C006   | Volkswagen  | Jetta       | $52.00        |
+--------+-------------+-------------+---------------+
| C007   | Lexus       | RX 350      | $125.00       |
+--------+-------------+-------------+---------------+
| C008   | Jaguar      | XF          | $140.00       |
+--------+-------------+-------------+---------------+
| C009   | Porsche     | 911         | $200.00       |
+--------+-------------+-------------+---------------+
| C010   | Lamborghini | Huracan     | $350.00       |
+--------+-------------+-------------+---------------+
| C011   | Ferrari     | 488         | $400.00       |
+--------+-------------+-------------+---------------+
| C012   | Bugatti     | Chiron      | $1000.00      |
+--------+-------------+-------------+---------------+
| C013   | Ford        | Mustang     | $90.00        |
+--------+-------------+-------------+---------------+
| C014   | Dodge       | Challenger  | $95.00        |
+--------+-------------+-------------+---------------+
| C015   | Land Rover  | Range Rover | $160.00       |
+--------+-------------+-------------+---------------+

Enter Car IDs to rent (Use a comma to separate multiple car IDs): C003, C005

Enter number of days to rent Car ID C003: 5
Car ID C003 rented successfully! Cost: $600.0

Enter number of days to rent Car ID C005: 8
Car ID C005 rented successfully! Cost: $480.0

Total rental cost: $1080.0
```

### 3. Viewing Rental Status  
```plaintext

=======================================
|        CAR RENTAL SYSTEM            |
=======================================
| 1. View Available Cars              |
| 2. Rent a Car                       |
| 3. View Rental Status               |
| 4. Return a Car                     |
| 5. Exit                             |
=======================================

Enter your choice: 3

Rental Status:
+----------+-----------------+-----------------+----------------------+----------------------+
| Car ID   | Brand           | Model           | Customer Name        | No. of Days Rented   |
+----------+-----------------+-----------------+----------------------+----------------------+
| C003     | BMW             | X5              | Rajesh Sharma        | 5                    |
+----------+-----------------+-----------------+----------------------+----------------------+
| C005     | Kia             | Sportage        | Rajesh Sharma        | 8                    |
+----------+-----------------+-----------------+----------------------+----------------------+
```

### 4. Returning a Car  
```plaintext
=======================================
|        CAR RENTAL SYSTEM            |
=======================================
| 1. View Available Cars              |
| 2. Rent a Car                       |
| 3. View Rental Status               |
| 4. Return a Car                     |
| 5. Exit                             |
=======================================

Enter your choice: 4

Currently Rented Cars:
+----------+---------------+--------------+
| Car ID   | Brand         | Model        |
+----------+---------------+--------------+
| C003     | BMW           | X5           |
+----------+---------------+--------------+
| C005     | Kia           | Sportage     |
+----------+---------------+--------------+

Enter Car ID to return: C003

Car returned successfully.

=======================================
|        CAR RENTAL SYSTEM            |
=======================================
| 1. View Available Cars              |
| 2. Rent a Car                       |
| 3. View Rental Status               |
| 4. Return a Car                     |
| 5. Exit                             |
=======================================

Enter your choice: 4

Currently Rented Cars:
+----------+---------------+--------------+
| Car ID   | Brand         | Model        |
+----------+---------------+--------------+
| C005     | Kia           | Sportage     |
+----------+---------------+--------------+

Enter Car ID to return: C005

Car returned successfully.

=======================================
|        CAR RENTAL SYSTEM            |
=======================================
| 1. View Available Cars              |
| 2. Rent a Car                       |
| 3. View Rental Status               |
| 4. Return a Car                     |
| 5. Exit                             |
=======================================

Enter your choice: 3

No active rentals available.
```

### 5. Exiting the System
```plaintext
=======================================
|        CAR RENTAL SYSTEM            |
=======================================
| 1. View Available Cars              |
| 2. Rent a Car                       |
| 3. View Rental Status               |
| 4. Return a Car                     |
| 5. Exit                             |
=======================================

Enter your choice: 5
Exiting System.....
Thank You For Using Car Rental System!!!
```

## Database Schema
### Tables:
1. **cars** - Stores car details (ID, brand, model, price, availability).
2. **customers** - Stores customer information (ID, name).
3. **rentals** - Stores rental transactions (customer, car, rental days, status).

## Error Handling & Transactions
- Uses **transactions** and **rollback** to maintain data consistency.
- Prevents SQL injections by using **prepared statements**.
- Ensures only available cars can be rented.

## Author
- **Gourab Roy**


