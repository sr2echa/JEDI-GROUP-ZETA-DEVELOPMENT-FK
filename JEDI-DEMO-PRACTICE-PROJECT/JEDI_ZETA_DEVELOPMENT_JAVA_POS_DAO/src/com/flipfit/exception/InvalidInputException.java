package com.flipfit.exception;

/**
 * Exception thrown when invalid input is provided to a method.
 * This is a more specific type of ValidationException for input parameter validation.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class InvalidInputException extends ValidationException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidInputException with the specified message.
     * 
     * @param message the detail message explaining which input is invalid
     */
    public InvalidInputException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidInputException with the specified message and cause.
     * 
     * @param message the detail message explaining which input is invalid
     * @param cause the cause of the exception
     */
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
