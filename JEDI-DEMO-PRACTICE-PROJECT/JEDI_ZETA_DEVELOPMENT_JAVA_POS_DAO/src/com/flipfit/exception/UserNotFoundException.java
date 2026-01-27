package com.flipfit.exception;

/**
 * Exception thrown when a requested user cannot be found in the system.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class UserNotFoundException extends FlipFitException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new UserNotFoundException with the specified message.
     * 
     * @param message the detail message explaining which user was not found
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new UserNotFoundException with the specified message and cause.
     * 
     * @param message the detail message explaining which user was not found
     * @param cause the cause of the exception
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
