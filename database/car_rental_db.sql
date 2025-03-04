-- Create the database  car_rental_db if it doesn't already exist
CREATE DATABASE IF NOT EXISTS car_rental_db;

-- Select the created database for use
USE car_rental_db;

-- Create the 'cars' table to store car details
CREATE TABLE IF NOT EXISTS cars (
    car_id VARCHAR(10) PRIMARY KEY, 
    brand VARCHAR(50), 
    model VARCHAR(50), 
    price DOUBLE, 
    is_available BOOLEAN DEFAULT TRUE 
);

-- Create the 'customers' table to store customer details
CREATE TABLE IF NOT EXISTS customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY, 
    name VARCHAR(100) NOT NULL 
);

-- Create the 'rentals' table to store rental transactions
CREATE TABLE IF NOT EXISTS rentals (
    rental_id INT AUTO_INCREMENT PRIMARY KEY, 
    customer_id INT, 
    car_id VARCHAR(10), 
    days INT,
    total_price DOUBLE,
    rental_status ENUM('Active', 'Returned') DEFAULT 'Active', 
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE, 
    FOREIGN KEY (car_id) REFERENCES cars(car_id) ON DELETE CASCADE 
);

-- Insert the cars data into the 'cars' table
INSERT INTO cars (car_id, brand, model, price) VALUES
('C001', 'Toyota', 'Corolla', 50.0),
('C002', 'Honda', 'Civic', 55.0),
('C003', 'BMW', 'X5', 120.0),
('C004', 'Audi', 'A4', 100.0),
('C005', 'Kia', 'Sportage', 60.0),
('C006', 'Volkswagen', 'Jetta', 52.0),
('C007', 'Lexus', 'RX 350', 125.0),
('C008', 'Jaguar', 'XF', 140.0),
('C009', 'Porsche', '911', 200.0),
('C010', 'Lamborghini', 'Huracan', 350.0),
('C011', 'Ferrari', '488', 400.0),
('C012', 'Bugatti', 'Chiron', 1000.0),
('C013', 'Ford', 'Mustang', 90.0),
('C014', 'Dodge', 'Challenger', 95.0),
('C015', 'Land Rover', 'Range Rover', 160.0);
