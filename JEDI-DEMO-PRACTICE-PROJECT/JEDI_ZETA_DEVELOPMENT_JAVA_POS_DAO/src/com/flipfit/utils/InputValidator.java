package com.flipfit.utils;

import com.flipfit.exception.InvalidInputException;
import com.flipfit.exception.ValidationException;
import java.util.regex.Pattern;

/**
 * Utility class for input validation.
 * Provides methods to validate various types of user inputs according to business rules.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class InputValidator {
    
    // Regex patterns for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );
    
    private static final Pattern PAN_PATTERN = Pattern.compile(
        "^[A-Z]{5}[0-9]{4}[A-Z]{1}$"
    );
    
    private static final Pattern GST_PATTERN = Pattern.compile(
        "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$"
    );
    
    private static final Pattern AADHAR_PATTERN = Pattern.compile(
        "^[0-9]{12}$"
    );
    
    // Constants for validation
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 50;
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_EMAIL_LENGTH = 100;
    private static final int MIN_LOCATION_LENGTH = 3;
    private static final int MAX_LOCATION_LENGTH = 100;
    private static final double MIN_AMOUNT = 0.01;
    private static final double MAX_AMOUNT = 1000000.0;
    private static final int MIN_CAPACITY = 1;
    private static final int MAX_CAPACITY = 1000;

    /**
     * Validates a username.
     * Username must be 3-20 characters, alphanumeric with underscores.
     * 
     * @param username the username to validate
     * @throws ValidationException if username is invalid
     */
    public static void validateUsername(String username) throws ValidationException {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username cannot be null or empty");
        }
        
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new ValidationException(
                "Username must be 3-20 characters long and contain only letters, numbers, and underscores"
            );
        }
    }

    /**
     * Validates a password.
     * Password must be 8-50 characters long.
     * 
     * @param password the password to validate
     * @throws ValidationException if password is invalid
     */
    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password cannot be null or empty");
        }
        
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new ValidationException(
                "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long"
            );
        }
        
        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new ValidationException(
                "Password must not exceed " + MAX_PASSWORD_LENGTH + " characters"
            );
        }
    }

    /**
     * Validates an email address.
     * 
     * @param email the email to validate
     * @throws ValidationException if email is invalid
     */
    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be null or empty");
        }
        
        if (email.length() > MAX_EMAIL_LENGTH) {
            throw new ValidationException("Email must not exceed " + MAX_EMAIL_LENGTH + " characters");
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }
    }

    /**
     * Validates a name (user name, center name, etc.).
     * 
     * @param name the name to validate
     * @param fieldName the name of the field (for error messages)
     * @throws ValidationException if name is invalid
     */
    public static void validateName(String name, String fieldName) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be null or empty");
        }
        
        String trimmed = name.trim();
        if (trimmed.length() < MIN_NAME_LENGTH) {
            throw new ValidationException(
                fieldName + " must be at least " + MIN_NAME_LENGTH + " characters long"
            );
        }
        
        if (trimmed.length() > MAX_NAME_LENGTH) {
            throw new ValidationException(
                fieldName + " must not exceed " + MAX_NAME_LENGTH + " characters"
            );
        }
    }

    /**
     * Validates a PAN number.
     * 
     * @param panNumber the PAN number to validate
     * @throws ValidationException if PAN number is invalid
     */
    public static void validatePAN(String panNumber) throws ValidationException {
        if (panNumber == null || panNumber.trim().isEmpty()) {
            throw new ValidationException("PAN number cannot be null or empty");
        }
        
        if (!PAN_PATTERN.matcher(panNumber.toUpperCase()).matches()) {
            throw new ValidationException("Invalid PAN number format. Expected format: ABCDE1234F");
        }
    }

    /**
     * Validates a GST number.
     * 
     * @param gstNumber the GST number to validate
     * @throws ValidationException if GST number is invalid
     */
    public static void validateGST(String gstNumber) throws ValidationException {
        if (gstNumber == null || gstNumber.trim().isEmpty()) {
            throw new ValidationException("GST number cannot be null or empty");
        }
        
        if (!GST_PATTERN.matcher(gstNumber.toUpperCase()).matches()) {
            throw new ValidationException("Invalid GST number format");
        }
    }

    /**
     * Validates an Aadhar number.
     * 
     * @param aadharNumber the Aadhar number to validate
     * @throws ValidationException if Aadhar number is invalid
     */
    public static void validateAadhar(String aadharNumber) throws ValidationException {
        if (aadharNumber == null || aadharNumber.trim().isEmpty()) {
            throw new ValidationException("Aadhar number cannot be null or empty");
        }
        
        if (!AADHAR_PATTERN.matcher(aadharNumber).matches()) {
            throw new ValidationException("Invalid Aadhar number. Must be exactly 12 digits");
        }
    }

    /**
     * Validates a location string.
     * 
     * @param location the location to validate
     * @throws ValidationException if location is invalid
     */
    public static void validateLocation(String location) throws ValidationException {
        if (location == null || location.trim().isEmpty()) {
            throw new ValidationException("Location cannot be null or empty");
        }
        
        String trimmed = location.trim();
        if (trimmed.length() < MIN_LOCATION_LENGTH) {
            throw new ValidationException(
                "Location must be at least " + MIN_LOCATION_LENGTH + " characters long"
            );
        }
        
        if (trimmed.length() > MAX_LOCATION_LENGTH) {
            throw new ValidationException(
                "Location must not exceed " + MAX_LOCATION_LENGTH + " characters"
            );
        }
    }

    /**
     * Validates a payment amount.
     * 
     * @param amount the amount to validate
     * @throws ValidationException if amount is invalid
     */
    public static void validateAmount(double amount) throws ValidationException {
        if (amount < MIN_AMOUNT) {
            throw new ValidationException("Amount must be at least " + MIN_AMOUNT);
        }
        
        if (amount > MAX_AMOUNT) {
            throw new ValidationException("Amount must not exceed " + MAX_AMOUNT);
        }
    }

    /**
     * Validates a slot capacity.
     * 
     * @param capacity the capacity to validate
     * @throws ValidationException if capacity is invalid
     */
    public static void validateCapacity(int capacity) throws ValidationException {
        if (capacity < MIN_CAPACITY) {
            throw new ValidationException("Capacity must be at least " + MIN_CAPACITY);
        }
        
        if (capacity > MAX_CAPACITY) {
            throw new ValidationException("Capacity must not exceed " + MAX_CAPACITY);
        }
    }

    /**
     * Validates that a string is not null or empty.
     * 
     * @param value the value to validate
     * @param fieldName the name of the field (for error messages)
     * @throws ValidationException if value is null or empty
     */
    public static void validateNotNullOrEmpty(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be null or empty");
        }
    }

    /**
     * Validates that an ID is not null or empty.
     * 
     * @param id the ID to validate
     * @param idType the type of ID (for error messages)
     * @throws ValidationException if ID is invalid
     */
    public static void validateId(String id, String idType) throws ValidationException {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException(idType + " cannot be null or empty");
        }
    }

    /**
     * Validates time range for slots.
     * 
     * @param startTime the start time
     * @param endTime the end time
     * @throws ValidationException if time range is invalid
     */
    public static void validateTimeRange(java.time.LocalTime startTime, java.time.LocalTime endTime) 
            throws ValidationException {
        if (startTime == null || endTime == null) {
            throw new ValidationException("Start time and end time cannot be null");
        }
        
        if (!endTime.isAfter(startTime)) {
            throw new ValidationException("End time must be after start time");
        }
        
        // Validate minimum slot duration (e.g., 30 minutes)
        long minutes = java.time.Duration.between(startTime, endTime).toMinutes();
        if (minutes < 30) {
            throw new ValidationException("Slot duration must be at least 30 minutes");
        }
    }
}
