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
     * @param username the username
     * @param password the password
     * @return the user
     * @throws UserNotFoundException the user not found exception
     */
    @Override
    public User login(String username, String password) throws UserNotFoundException {
        // Hash the password with username as salt before checking
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
     * @param username the username
     * @param password the password
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
        // Hash the password with username as salt before storing
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
     * @param username the username
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return true, if successful
     */
    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // Hash both passwords with username as salt
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