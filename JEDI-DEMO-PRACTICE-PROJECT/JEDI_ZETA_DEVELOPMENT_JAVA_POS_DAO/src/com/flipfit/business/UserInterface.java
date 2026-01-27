package com.flipfit.business;

import com.flipfit.bean.User;
import com.flipfit.exception.*;

/**
 * Interface for user-related operations including authentication, registration, and password management.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public interface UserInterface {
    /**
     * Authenticates a user with username and password.
     * 
     * @param username the username to authenticate
     * @param password the plain text password to verify
     * @return the authenticated User object
     * @throws AuthenticationException if authentication fails
     * @throws ValidationException if input validation fails
     * @throws UserNotFoundException if user does not exist
     */
    User login(String username, String password) 
            throws AuthenticationException, ValidationException, UserNotFoundException;

    /**
     * Registers a new user in the system.
     * 
     * @param username the username for the new user
     * @param password the plain text password (will be hashed)
     * @param email the email address
     * @param roleChoice the role choice (1 for GYM_OWNER, 2 for CUSTOMER)
     * @return true if registration successful
     * @throws ValidationException if input validation fails
     * @throws InvalidInputException if role choice is invalid
     * @throws DatabaseException if database operation fails
     */
    boolean register(String username, String password, String email, int roleChoice) 
            throws ValidationException, InvalidInputException, DatabaseException;

    /**
     * Changes a user's password.
     * 
     * @param username the username of the user
     * @param oldPassword the current plain text password
     * @param newPassword the new plain text password (will be hashed)
     * @return true if password change successful
     * @throws AuthenticationException if old password is incorrect
     * @throws ValidationException if input validation fails
     * @throws UserNotFoundException if user does not exist
     * @throws DatabaseException if database operation fails
     */
    boolean changePassword(String username, String oldPassword, String newPassword) 
            throws AuthenticationException, ValidationException, UserNotFoundException, DatabaseException;

    /**
     * Retrieves a user by their user ID.
     * 
     * @param userId the user ID to look up
     * @return the User object if found
     * @throws ValidationException if userId is invalid
     * @throws UserNotFoundException if user does not exist
     * @throws DatabaseException if database operation fails
     */
    User getUser(String userId) throws ValidationException, UserNotFoundException, DatabaseException;

    /**
     * Retrieves all users from the system.
     * 
     * @return a list of all users
     * @throws DatabaseException if database operation fails
     */
    java.util.List<User> getAllUsers() throws DatabaseException;
}
