package com.flipfit.exception;

/**
 * Exception thrown when a user attempts to perform an operation they are not authorized to perform.
 * This includes accessing resources without proper permissions or role-based access violations.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class AuthorizationException extends FlipFitException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new AuthorizationException with the specified message.
     * 
     * @param message the detail message explaining the authorization failure
     */
    public AuthorizationException(String message) {
        super(message);
    }

    /**
     * Constructs a new AuthorizationException with the specified message and cause.
     * 
     * @param message the detail message explaining the authorization failure
     * @param cause the cause of the exception
     */
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
