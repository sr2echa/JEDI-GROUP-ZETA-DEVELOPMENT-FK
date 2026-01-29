package com.flipfit.api.dto;

/**
 * Change password request DTO.
 * 
 * IMPORTANT: Both oldPassword and newPassword fields should contain
 * PLAIN TEXT passwords, NOT hashed values. Server-side password hashing
 * is handled by the UserService layer.
 * 
 * Example JSON:
 * {
 *   "username": "admin",
 *   "oldPassword": "admin123",
 *   "newPassword": "newpassword456"
 * }
 */
public class ChangePasswordRequest {
    private String username;
    private String oldPassword;  // PLAIN TEXT password
    private String newPassword;  // PLAIN TEXT password
    
    public ChangePasswordRequest() {}
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Gets the old password.
     * 
     * @return the old plain text password (NOT hashed)
     */
    public String getOldPassword() {
        return oldPassword;
    }
    
    /**
     * Sets the old password.
     * 
     * @param oldPassword the old plain text password (NOT hashed)
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    /**
     * Gets the new password.
     * 
     * @return the new plain text password (NOT hashed)
     */
    public String getNewPassword() {
        return newPassword;
    }
    
    /**
     * Sets the new password.
     * 
     * @param newPassword the new plain text password (NOT hashed)
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
