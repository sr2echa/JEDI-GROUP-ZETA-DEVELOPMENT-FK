package com.flipfit.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification.
 * Uses SHA-256 with salt for password hashing.
 * 
 * Note: For production use, consider using BCrypt or Argon2 for better security.
 * This implementation provides basic password hashing with salt.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class PasswordUtil {
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    private static final SecureRandom random = new SecureRandom();

    /**
     * Hashes a password with a randomly generated salt.
     * The returned string contains both the salt and the hash, separated by a colon.
     * Format: "salt:hash"
     * 
     * @param password the plain text password to hash
     * @return a string containing salt and hash separated by colon
     * @throws RuntimeException if hashing algorithm is not available
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        try {
            // Generate random salt
            byte[] salt = generateSalt();
            
            // Hash password with salt
            String hash = hashWithSalt(password, salt);
            
            // Encode salt and hash to Base64 for storage
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            
            // Return format: "salt:hash"
            return saltBase64 + ":" + hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing algorithm not available", e);
        }
    }

    /**
     * Verifies a password against a stored hash.
     * 
     * @param password the plain text password to verify
     * @param storedHash the stored hash in format "salt:hash"
     * @return true if password matches, false otherwise
     * @throws IllegalArgumentException if storedHash format is invalid
     */
    public static boolean verifyPassword(String password, String storedHash) {
        if (password == null || storedHash == null) {
            return false;
        }

        try {
            // Split salt and hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }

            // Decode salt from Base64
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            
            // Hash the provided password with the stored salt
            String hashToVerify = hashWithSalt(password, salt);
            
            // Use constant-time comparison to prevent timing attacks
            return constantTimeEquals(hashToVerify, parts[1]);
        } catch (Exception e) {
            // Log error in production, return false for security
            return false;
        }
    }

    /**
     * Generates a random salt for password hashing.
     * 
     * @return a byte array containing random salt
     */
    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hashes a password with the provided salt.
     * 
     * @param password the plain text password
     * @param salt the salt to use for hashing
     * @return the Base64 encoded hash
     * @throws NoSuchAlgorithmException if hashing algorithm is not available
     */
    private static String hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Performs constant-time string comparison to prevent timing attacks.
     * 
     * @param a first string
     * @param b second string
     * @return true if strings are equal, false otherwise
     */
    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
