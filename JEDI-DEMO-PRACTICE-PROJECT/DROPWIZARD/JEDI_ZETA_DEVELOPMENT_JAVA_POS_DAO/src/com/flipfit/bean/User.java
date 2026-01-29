package com.flipfit.bean;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 *
 * @author Zeta
 * @ClassName  "User"
 */
public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private Role role;

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public String getUserId() { return userId; }
    
    /**
     * Sets the user id.
     *
     * @param userId the new user id
     */
    public void setUserId(String userId) { this.userId = userId; }
    
    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() { return name; }
    
    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() { return email; }
    
    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(String email) { this.email = email; }
    
    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() { return password; }
    
    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) { this.password = password; }
    
    /**
     * Gets the role.
     *
     * @return the role
     */
    public Role getRole() { return role; }
    
    /**
     * Sets the role.
     *
     * @param role the new role
     */
    public void setRole(Role role) { this.role = role; }
}