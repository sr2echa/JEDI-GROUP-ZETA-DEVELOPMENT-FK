package com.flipfit.business;

import com.flipfit.bean.User;
import com.flipfit.exception.RegistrationFailedException;
import com.flipfit.exception.UserNotFoundException;

/**
 * The Interface UserInterface.
 * * @author Zeta
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
	 */
	User login(String username, String password) throws UserNotFoundException;

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

    boolean changePassword(String username, String oldPassword, String newPassword);

    User getUser(String userId);

    java.util.List<User> getAllUsers();
}
