package com.flipfit.utils;

/**
 * Utility class for sanitizing user inputs.
 * Removes potentially harmful characters and normalizes inputs.
 *
 * @author Zeta
 */
public class InputSanitizer {
    
    /**
     * Sanitizes general text input by trimming and removing control characters.
     *
     * @param input the input string to sanitize
     * @return sanitized string
     */
    public static String sanitizeText(String input) {
        if (input == null) {
            return "";
        }
        // Trim whitespace and remove control characters
        return input.trim().replaceAll("\\p{Cntrl}", "");
    }
    
    /**
     * Sanitizes alphanumeric input by removing non-alphanumeric characters.
     *
     * @param input the input string to sanitize
     * @return sanitized string with only alphanumeric characters
     */
    public static String sanitizeAlphanumeric(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("[^A-Za-z0-9]", "");
    }
    
    /**
     * Sanitizes numeric input by removing non-numeric characters.
     *
     * @param input the input string to sanitize
     * @return sanitized string with only digits
     */
    public static String sanitizeNumeric(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("[^0-9]", "");
    }
    
    /**
     * Sanitizes email by trimming and converting to lowercase.
     *
     * @param email the email to sanitize
     * @return sanitized email
     */
    public static String sanitizeEmail(String email) {
        if (email == null) {
            return "";
        }
        return email.trim().toLowerCase();
    }
    
    /**
     * Sanitizes username by trimming and removing special characters except underscore.
     *
     * @param username the username to sanitize
     * @return sanitized username
     */
    public static String sanitizeUsername(String username) {
        if (username == null) {
            return "";
        }
        return username.trim().replaceAll("[^A-Za-z0-9_]", "");
    }
    
    /**
     * Sanitizes PAN number by converting to uppercase and removing non-alphanumeric.
     *
     * @param pan the PAN number to sanitize
     * @return sanitized PAN
     */
    public static String sanitizePAN(String pan) {
        if (pan == null) {
            return "";
        }
        return pan.trim().toUpperCase().replaceAll("[^A-Z0-9]", "");
    }
    
    /**
     * Sanitizes GST number by converting to uppercase and removing non-alphanumeric.
     *
     * @param gst the GST number to sanitize
     * @return sanitized GST
     */
    public static String sanitizeGST(String gst) {
        if (gst == null) {
            return "";
        }
        return gst.trim().toUpperCase().replaceAll("[^A-Z0-9]", "");
    }
    
    /**
     * Sanitizes Aadhar number by removing non-numeric characters.
     *
     * @param aadhar the Aadhar number to sanitize
     * @return sanitized Aadhar
     */
    public static String sanitizeAadhar(String aadhar) {
        if (aadhar == null) {
            return "";
        }
        return aadhar.trim().replaceAll("[^0-9]", "");
    }
    
    /**
     * Prevents SQL injection by escaping single quotes.
     *
     * @param input the input string to sanitize
     * @return sanitized string
     */
    public static String preventSQLInjection(String input) {
        if (input == null) {
            return "";
        }
        // Replace single quotes with two single quotes (SQL escape)
        return input.replace("'", "''");
    }
    
    /**
     * Removes potentially dangerous characters that could be used in attacks.
     *
     * @param input the input string to sanitize
     * @return sanitized string
     */
    public static String removeDangerousCharacters(String input) {
        if (input == null) {
            return "";
        }
        // Remove common dangerous characters
        return input.replaceAll("[<>\"'`;(){}\\[\\]\\\\]", "");
    }
}
