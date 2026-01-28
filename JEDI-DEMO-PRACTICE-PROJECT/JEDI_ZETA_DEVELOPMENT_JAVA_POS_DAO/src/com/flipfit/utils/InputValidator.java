package com.flipfit.utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Utility class for validating various input fields.
 * Provides validation for Aadhar, PAN, GST, email, phone, and other inputs.
 *
 * @author Zeta
 */
public class InputValidator {
    
    // Regular expression patterns
    private static final Pattern AADHAR_PATTERN = Pattern.compile("^[0-9]{12}$");
    private static final Pattern PAN_PATTERN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]{1}$");
    private static final Pattern GST_PATTERN = Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,20}$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");
    
    /**
     * Validates Aadhar number.
     * Must be exactly 12 digits.
     *
     * @param aadhar the aadhar number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAadhar(String aadhar) {
        if (aadhar == null || aadhar.trim().isEmpty()) {
            return false;
        }
        return AADHAR_PATTERN.matcher(aadhar.trim()).matches();
    }
    
    /**
     * Validates PAN number.
     * Format: 5 letters, 4 digits, 1 letter (e.g., ABCDE1234F)
     *
     * @param pan the PAN number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPAN(String pan) {
        if (pan == null || pan.trim().isEmpty()) {
            return false;
        }
        return PAN_PATTERN.matcher(pan.trim().toUpperCase()).matches();
    }
    
    /**
     * Validates GST number.
     * Format: 15 characters (2 digits state code + 10 char PAN + 1 digit entity number + Z + 1 alphanumeric)
     *
     * @param gst the GST number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidGST(String gst) {
        if (gst == null || gst.trim().isEmpty()) {
            return false;
        }
        return GST_PATTERN.matcher(gst.trim().toUpperCase()).matches();
    }
    
    /**
     * Validates email address.
     *
     * @param email the email address to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validates phone number.
     * Must be exactly 10 digits.
     *
     * @param phone the phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    /**
     * Validates username.
     * Must be 3-20 characters, alphanumeric with underscore allowed.
     *
     * @param username the username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }
    
    /**
     * Validates password strength.
     * Must be at least 6 characters.
     *
     * @param password the password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= 6;
    }
    
    /**
     * Validates if string is alphanumeric.
     *
     * @param input the input string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isAlphanumeric(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return ALPHANUMERIC_PATTERN.matcher(input.trim()).matches();
    }
    
    /**
     * Validates if number is positive.
     *
     * @param number the number to validate
     * @return true if positive, false otherwise
     */
    public static boolean isPositiveNumber(int number) {
        return number > 0;
    }
    
    /**
     * Validates if number is positive.
     *
     * @param number the number to validate
     * @return true if positive, false otherwise
     */
    public static boolean isPositiveNumber(double number) {
        return number > 0;
    }
    
    /**
     * Validates if string is not empty after trimming.
     *
     * @param input the input string to validate
     * @return true if not empty, false otherwise
     */
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }
    
    /**
     * Validates if string length is within range.
     *
     * @param input the input string to validate
     * @param minLength minimum length
     * @param maxLength maximum length
     * @return true if within range, false otherwise
     */
    public static boolean isLengthInRange(String input, int minLength, int maxLength) {
        if (input == null) {
            return false;
        }
        int length = input.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Gets validation error message for field.
     *
     * @param fieldName the field name
     * @param validationType the type of validation that failed
     * @return error message
     */
    public static String getValidationErrorMessage(String fieldName, String validationType) {
        switch (validationType) {
            case "AADHAR":
                return "[ERROR] Invalid " + fieldName + ". Must be exactly 12 digits.";
            case "PAN":
                return "[ERROR] Invalid " + fieldName + ". Format: 5 letters + 4 digits + 1 letter (e.g., ABCDE1234F).";
            case "GST":
                return "[ERROR] Invalid " + fieldName + ". Must be 15 characters in valid GST format.";
            case "EMAIL":
                return "[ERROR] Invalid " + fieldName + ". Please enter a valid email address.";
            case "PHONE":
                return "[ERROR] Invalid " + fieldName + ". Must be exactly 10 digits.";
            case "USERNAME":
                return "[ERROR] Invalid " + fieldName + ". Must be 3-20 characters, alphanumeric with underscore allowed.";
            case "PASSWORD":
                return "[ERROR] Invalid " + fieldName + ". Must be at least 6 characters.";
            case "EMPTY":
                return "[ERROR] " + fieldName + " cannot be empty.";
            case "POSITIVE":
                return "[ERROR] " + fieldName + " must be a positive number.";
            default:
                return "[ERROR] Invalid " + fieldName + ".";
        }
    }
}
