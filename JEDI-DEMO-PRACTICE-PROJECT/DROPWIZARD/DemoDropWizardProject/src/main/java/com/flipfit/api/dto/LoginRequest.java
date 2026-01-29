package com.flipfit.api.dto;

/**
 * Login request DTO.
 * 
 * IMPORTANT: The password field should contain PLAIN TEXT password,
 * NOT a hashed value. Server-side password hashing is handled by
 * the UserService layer.
 * 
 * Example JSON:
 * {
 *   "username": "admin",
 *   "password": "admin123"
 * }
 */
public class LoginRequest {
    private String username;
    private String password;  // PLAIN TEXT password
    
    public LoginRequest() {}
    
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Gets the password.
     * 
     * @return the plain text password (NOT hashed)
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password.
     * 
     * @param password the plain text password (NOT hashed)
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
