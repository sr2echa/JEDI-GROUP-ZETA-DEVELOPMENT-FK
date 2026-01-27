-- FlipFit Database Schema

CREATE DATABASE IF NOT EXISTS Flipfit_schema;
USE Flipfit_schema;

-- Table for Users
CREATE TABLE IF NOT EXISTS User (
    userId VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL -- ADMIN, GYM_OWNER, CUSTOMER
);

-- Table for Customers
CREATE TABLE IF NOT EXISTS Customer (
    userId VARCHAR(50) PRIMARY KEY,
    phoneNumber VARCHAR(15),
    address VARCHAR(255),
    FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- Table for Gym Owners
CREATE TABLE IF NOT EXISTS GymOwner (
    userId VARCHAR(50) PRIMARY KEY,
    panNumber VARCHAR(20),
    isApproved BOOLEAN DEFAULT FALSE,
    gstNumber VARCHAR(20),
    aadharNumber VARCHAR(20),
    location VARCHAR(100),
    FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- Table for Gym Centers
CREATE TABLE IF NOT EXISTS GymCenter (
    centerId VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(50),
    address VARCHAR(255),
    ownerId VARCHAR(50),
    isApproved BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (ownerId) REFERENCES User(userId) ON DELETE SET NULL
);

-- Table for Slots
CREATE TABLE IF NOT EXISTS Slot (
    slotId VARCHAR(50) PRIMARY KEY,
    centerId VARCHAR(50),
    startTime TIME NOT NULL,
    endTime TIME NOT NULL,
    capacity INT DEFAULT 10,
    availableSeats INT DEFAULT 10,
    price DOUBLE DEFAULT 500.0,
    isApproved BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (centerId) REFERENCES GymCenter(centerId) ON DELETE CASCADE
);

-- Table for Bookings
CREATE TABLE IF NOT EXISTS Booking (
    bookingId VARCHAR(50) PRIMARY KEY,
    slotId VARCHAR(50),
    userId VARCHAR(50),
    status VARCHAR(20), -- CONFIRMED, CANCELLED, WAITLISTED, PENDING_PAYMENT
    bookingDate DATE,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (slotId) REFERENCES Slot(slotId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- Table for Payments
CREATE TABLE IF NOT EXISTS Payment (
    transactionId VARCHAR(50) PRIMARY KEY,
    bookingId VARCHAR(50),
    userId VARCHAR(50),
    centerId VARCHAR(50),
    amount DOUBLE NOT NULL,
    method VARCHAR(50),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20), -- COMPLETED, FAILED, REFUNDED
    FOREIGN KEY (bookingId) REFERENCES Booking(bookingId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE,
    FOREIGN KEY (centerId) REFERENCES GymCenter(centerId) ON DELETE CASCADE
);

-- Table for Notifications
CREATE TABLE IF NOT EXISTS Notification (
    notificationId VARCHAR(50) PRIMARY KEY,
    userId VARCHAR(50),
    message TEXT NOT NULL,
    type VARCHAR(50), -- BOOKING_CONFIRMATION, REFUND_INITIATED, etc.
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- Insert Default Admin
INSERT IGNORE INTO User (userId, name, email, password, role) 
VALUES ('admin', 'Admin User', 'admin@flipfit.com', 'admin123', 'ADMIN');
