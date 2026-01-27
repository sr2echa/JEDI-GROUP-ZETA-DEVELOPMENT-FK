package com.flipfit.business;

import com.flipfit.bean.*;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.dao.impl.GymUserDAOImpl;
import com.flipfit.exception.*;
import com.flipfit.utils.InputValidator;
import com.flipfit.utils.PasswordUtil;

/**
 * Service class for user-related operations including authentication, registration, and password management.
 * This service handles input validation, password hashing, and exception handling.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class UserService implements UserInterface {
    private GymUserDAO userDAO = new GymUserDAOImpl();

    /**
     * Authenticates a user with username and password.
     * Validates input, retrieves user from database, verifies password hash, and checks approval status.
     * 
     * @param username the username to authenticate
     * @param password the plain text password to verify
     * @return the authenticated User object
     * @throws AuthenticationException if authentication fails
     * @throws ValidationException if input validation fails
     * @throws UserNotFoundException if user does not exist
     */
    @Override
    public User login(String username, String password) throws AuthenticationException, ValidationException, UserNotFoundException {
        // Validate input
        try {
            InputValidator.validateUsername(username);
            InputValidator.validatePassword(password);
        } catch (ValidationException e) {
            throw new ValidationException("Invalid login credentials: " + e.getMessage(), e);
        }

        // Retrieve user from database (DAO returns user with hashed password)
        User user = userDAO.getUser(username);
        
        if (user == null) {
            throw new UserNotFoundException("User with username '" + username + "' not found");
        }

        // Verify password using hashed password from database
        String storedPasswordHash = user.getPassword();
        if (!PasswordUtil.verifyPassword(password, storedPasswordHash)) {
            throw new AuthenticationException("Invalid password for user '" + username + "'");
        }

        // Check if gym owner is approved
        if (user.getRole() == Role.GYM_OWNER) {
            GymOwner owner = (GymOwner) user;
            if (!owner.isApproved()) {
                throw new AuthenticationException(
                    "Gym Owner account '" + username + "' is pending Admin approval"
                );
            }
        }

        System.out.println("[SUCCESS] Welcome back, " + user.getName() + "!");
        return user;
    }

    /**
     * Registers a new user in the system.
     * Validates all input, hashes password, and creates user record.
     * 
     * @param username the username for the new user
     * @param password the plain text password (will be hashed)
     * @param email the email address
     * @param roleChoice the role choice (1 for GYM_OWNER, 2 for CUSTOMER)
     * @return true if registration successful, false otherwise
     * @throws ValidationException if input validation fails
     * @throws InvalidInputException if role choice is invalid
     * @throws DatabaseException if database operation fails
     */
    @Override
    public boolean register(String username, String password, String email, int roleChoice) 
            throws ValidationException, InvalidInputException, DatabaseException {
        // Validate all inputs
        try {
            InputValidator.validateUsername(username);
            InputValidator.validatePassword(password);
            InputValidator.validateEmail(email);
        } catch (ValidationException e) {
            throw new ValidationException("Registration validation failed: " + e.getMessage(), e);
        }

        // Validate role choice
        Role role = getRoleFromChoice(roleChoice);
        if (role == null) {
            throw new InvalidInputException("Invalid role choice: " + roleChoice + ". Must be 1 (GYM_OWNER) or 2 (CUSTOMER)");
        }

        // Create appropriate user object
        User newUser;
        if (role == Role.CUSTOMER) {
            newUser = new Customer();
        } else if (role == Role.GYM_OWNER) {
            newUser = new GymOwner();
        } else {
            throw new InvalidInputException("Invalid registration path for role: " + role);
        }

        // Set user properties
        newUser.setUserId(username);
        newUser.setName(username);
        
        // Hash password before storing
        String hashedPassword = PasswordUtil.hashPassword(password);
        newUser.setPassword(hashedPassword);
        
        newUser.setEmail(email);
        newUser.setRole(role);

        // Register user in database
        try {
            boolean success = userDAO.registerUser(newUser);
            if (success) {
                System.out.println("[SUCCESS] User '" + username + "' registered successfully");
            }
            return success;
        } catch (Exception e) {
            throw new DatabaseException("Failed to register user: " + e.getMessage(), e);
        }
    }

    /**
     * Changes a user's password.
     * Validates input, verifies old password, and updates with new hashed password.
     * 
     * @param username the username of the user
     * @param oldPassword the current plain text password
     * @param newPassword the new plain text password (will be hashed)
     * @return true if password change successful, false otherwise
     * @throws AuthenticationException if old password is incorrect
     * @throws ValidationException if input validation fails
     * @throws UserNotFoundException if user does not exist
     * @throws DatabaseException if database operation fails
     */
    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) 
            throws AuthenticationException, ValidationException, UserNotFoundException, DatabaseException {
        // Validate inputs
        try {
            InputValidator.validateUsername(username);
            InputValidator.validatePassword(oldPassword);
            InputValidator.validatePassword(newPassword);
        } catch (ValidationException e) {
            throw new ValidationException("Password change validation failed: " + e.getMessage(), e);
        }

        // Verify user exists and old password is correct
        User user = userDAO.getUser(username);
        if (user == null) {
            throw new UserNotFoundException("User with username '" + username + "' not found");
        }

        String storedPasswordHash = user.getPassword();
        if (!PasswordUtil.verifyPassword(oldPassword, storedPasswordHash)) {
            throw new AuthenticationException("Old password is incorrect");
        }

        // Hash new password
        String hashedNewPassword = PasswordUtil.hashPassword(newPassword);

        // Update password in database
        try {
            boolean success = userDAO.changePassword(username, storedPasswordHash, hashedNewPassword);
            if (success) {
                System.out.println("[SUCCESS] Password changed successfully for user '" + username + "'");
            }
            return success;
        } catch (Exception e) {
            throw new DatabaseException("Failed to change password: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a user by their user ID.
     * 
     * @param userId the user ID to look up
     * @return the User object if found
     * @throws ValidationException if userId is invalid
     * @throws UserNotFoundException if user does not exist
     * @throws DatabaseException if database operation fails
     */
    @Override
    public User getUser(String userId) throws ValidationException, UserNotFoundException, DatabaseException {
        try {
            InputValidator.validateId(userId, "User ID");
        } catch (ValidationException e) {
            throw new ValidationException("Invalid user ID: " + e.getMessage(), e);
        }

        try {
            User user = userDAO.getUser(userId);
            if (user == null) {
                throw new UserNotFoundException("User with ID '" + userId + "' not found");
            }
            return user;
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve user: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all users from the system.
     * 
     * @return a list of all users
     * @throws DatabaseException if database operation fails
     */
    @Override
    public java.util.List<User> getAllUsers() throws DatabaseException {
        try {
            return userDAO.getAllUsers();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve all users: " + e.getMessage(), e);
        }
    }

    /**
     * Converts a role choice integer to a Role enum.
     * 
     * @param choice the role choice (1 for GYM_OWNER, 2 for CUSTOMER)
     * @return the corresponding Role enum, or null if invalid
     */
    private Role getRoleFromChoice(int choice) {
        switch (choice) {
            case 1:
                return Role.GYM_OWNER;
            case 2:
                return Role.CUSTOMER;
            default:
                return null;
        }
    }
}
