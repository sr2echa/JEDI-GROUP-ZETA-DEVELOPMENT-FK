package com.flipfit.exception;

/**
 * Exception thrown when input validation fails.
 * This includes invalid format, missing required fields, or values outside acceptable ranges.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class ValidationException extends FlipFitException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ValidationException with the specified message.
     * 
     * @param message the detail message explaining the validation failure
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ValidationException with the specified message and cause.
     * 
     * @param message the detail message explaining the validation failure
     * @param cause the cause of the exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
