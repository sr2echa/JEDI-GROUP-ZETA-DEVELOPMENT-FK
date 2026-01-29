package com.flipfit.business;

import com.flipfit.bean.User;
import com.flipfit.exception.InvalidApprovalException;
import com.flipfit.exception.RegistrationFailedException;
import com.flipfit.exception.UserNotFoundException;

/**
 * The Interface UserInterface.
 * Defines business operations for user management.
 *
 * @author Zeta
 * @ClassName "UserInterface"
 */
public interface UserInterface {
	/**
	 * Login.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the user
	 * @throws UserNotFoundException the user not found exception
	 * @throws InvalidApprovalException the invalid approval exception
	 */
	User login(String username, String password) throws UserNotFoundException, InvalidApprovalException;

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
	boolean register(String username, String password, String email, int roleChoice) throws RegistrationFailedException;

    /**
     * Change password.
     * Updates user's password after validating the old password.
     *
     * @param username the username
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return true, if successful
     */
    boolean changePassword(String username, String oldPassword, String newPassword);

    /**
     * Gets the user.
     * Retrieves user details by user ID.
     *
     * @param userId the user id
     * @return the user object
     */
    User getUser(String userId);

    /**
     * Gets all users.
     * Retrieves all registered users.
     *
     * @return the list of all users
     */
    java.util.List<User> getAllUsers();
}
