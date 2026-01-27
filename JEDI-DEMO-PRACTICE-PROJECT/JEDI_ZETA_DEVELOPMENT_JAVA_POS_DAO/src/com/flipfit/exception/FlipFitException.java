package com.flipfit.exception;

/**
 * Base exception class for all FlipFit application exceptions.
 * All custom exceptions in the application should extend this class.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class FlipFitException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new FlipFitException with the specified message.
     * 
     * @param message the detail message explaining the exception
     */
    public FlipFitException(String message) {
        super(message);
    }

    /**
     * Constructs a new FlipFitException with the specified message and cause.
     * 
     * @param message the detail message explaining the exception
     * @param cause the cause of the exception
     */
    public FlipFitException(String message, Throwable cause) {
        super(message, cause);
    }
}
