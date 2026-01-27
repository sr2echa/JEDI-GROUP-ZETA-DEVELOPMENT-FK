package com.flipfit.exception;

/**
 * Exception thrown when authentication fails.
 * This includes invalid credentials, account not approved, or account locked scenarios.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class AuthenticationException extends FlipFitException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new AuthenticationException with the specified message.
     * 
     * @param message the detail message explaining the authentication failure
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new AuthenticationException with the specified message and cause.
     * 
     * @param message the detail message explaining the authentication failure
     * @param cause the cause of the exception
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
