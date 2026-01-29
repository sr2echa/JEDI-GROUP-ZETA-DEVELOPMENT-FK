package com.flipfit.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for password hashing operations.
 * Uses SHA-256 with username as salt.
 *
 * @author Zeta
 */
public class PasswordHashUtil {
    
    /**
     * Hash password using SHA-256 with username as salt.
     *
     * @param password the plain text password
     * @param username the username (used as salt)
     * @return the hashed password as hex string
     */
    public static String hashPassword(String password, String username) {
        try {
            String saltedPassword = password + username;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(saltedPassword.getBytes());
            
            // Convert bytes to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
