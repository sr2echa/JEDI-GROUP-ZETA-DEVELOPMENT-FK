package com.flipfit.business;

import com.flipfit.bean.*;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.dao.impl.GymUserDAOImpl;
import com.flipfit.exception.RegistrationFailedException;
import com.flipfit.exception.UserNotFoundException;
import com.flipfit.utils.PasswordHashUtil;

/**
 * The Class UserService.
 *
 * @author Zeta
 * @ClassName  "UserService"
 */
public class UserService implements UserInterface {
    
    /** The user DAO. */
    private GymUserDAO userDAO = new GymUserDAOImpl();

    /**
     * Login.
     * 
     * **SERVER-SIDE PASSWORD HASHING**:
     * This method receives a plain text password from the API endpoint,
     * hashes it using SHA-256 with username as salt, and then checks
     * against the database where passwords are stored hashed.
     *
     * @param username the username
     * @param password the password (PLAIN TEXT from API request)
     * @return the user
     * @throws UserNotFoundException the user not found exception
     */
    @Override
    public User login(String username, String password) throws UserNotFoundException {
        // Hash the plain text password with username as salt before checking database
        String hashedPassword = PasswordHashUtil.hashPassword(password, username);
        User user = userDAO.loginUser(username, hashedPassword);

        if (user != null) {
            if (user.getRole() == Role.GYM_OWNER) {
                GymOwner owner = (GymOwner) user;
                if (!owner.isApproved()) {
                    System.out.println("[ERROR] Gym Owner account '" + username + "' is pending Admin approval.");
                    return null;
                }
            }
            System.out.println("[SUCCESS] Welcome back, " + user.getName() + "!");
            return user;
        }

        throw new UserNotFoundException("[ERROR] Invalid Username or Password. Please try again or Register.");
    }

    /**
     * Register.
     * 
     * **SERVER-SIDE PASSWORD HASHING**:
     * This method receives a plain text password from the API endpoint,
     * hashes it using SHA-256 with username as salt before storing
     * in the database.
     *
     * @param username the username
     * @param password the password (PLAIN TEXT from API request)
     * @param email the email
     * @param roleChoice the role choice
     * @return true, if successful
     * @throws RegistrationFailedException the registration failed exception
     */
    @Override
    public boolean register(String username, String password, String email, int roleChoice) throws RegistrationFailedException {
        Role role = getRoleFromChoice(roleChoice);
        User newUser;
        if (role == Role.CUSTOMER) {
            newUser = new Customer();
        } else if (role == Role.GYM_OWNER) {
            newUser = new GymOwner();
        } else {
            throw new RegistrationFailedException("[ERROR] Invalid registration path.");
        }

        newUser.setUserId(username);
        newUser.setName(username);
        // Hash the plain text password with username as salt before storing
        newUser.setPassword(PasswordHashUtil.hashPassword(password, username));
        newUser.setEmail(email);
        newUser.setRole(role);

        boolean success = userDAO.registerUser(newUser);
        if (!success) {
            throw new RegistrationFailedException("[ERROR] Registration failed due to a database issue.");
        }
        return true;
    }

    /**
     * Change password.
     * 
     * **SERVER-SIDE PASSWORD HASHING**:
     * This method receives plain text passwords (old and new) from the API endpoint,
     * hashes them using SHA-256 with username as salt before database operations.
     *
     * @param username the username
     * @param oldPassword the old password (PLAIN TEXT from API request)
     * @param newPassword the new password (PLAIN TEXT from API request)
     * @return true, if successful
     */
    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // Hash both plain text passwords with username as salt
        String hashedOldPassword = PasswordHashUtil.hashPassword(oldPassword, username);
        String hashedNewPassword = PasswordHashUtil.hashPassword(newPassword, username);
        return userDAO.changePassword(username, hashedOldPassword, hashedNewPassword);
    }

    /**
     * Gets the user.
     *
     * @param userId the user id
     * @return the user
     */
    @Override
    public User getUser(String userId) {
        return userDAO.getUser(userId);
    }

    /**
     * Gets the all users.
     *
     * @return the all users
     */
    @Override
    public java.util.List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Gets the role from choice.
     *
     * @param choice the choice
     * @return the role from choice
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