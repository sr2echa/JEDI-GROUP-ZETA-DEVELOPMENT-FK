package com.flipfit.dao;

import com.flipfit.bean.User;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Interface GymUserDAO.
 *
 * @author Zeta
 * @ClassName  "GymUserDAO"
 */
public interface GymUserDAO {
    
    /**
     * Register user.
     *
     * @param user the user
     * @return true, if successful
     */
    public boolean registerUser(User user);

    /**
     * Login user.
     *
     * @param username the username
     * @param password the password
     * @return the user
     */
    public User loginUser(String username, String password);

    /**
     * Change password.
     *
     * @param username the username
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return true, if successful
     */
    public boolean changePassword(String username, String oldPassword, String newPassword);

    /**
     * Gets the user.
     *
     * @param userId the user id
     * @return the user
     */
    public User getUser(String userId);

    /**
     * Gets the all users.
     *
     * @return the all users
     */
    public java.util.List<User> getAllUsers();
}