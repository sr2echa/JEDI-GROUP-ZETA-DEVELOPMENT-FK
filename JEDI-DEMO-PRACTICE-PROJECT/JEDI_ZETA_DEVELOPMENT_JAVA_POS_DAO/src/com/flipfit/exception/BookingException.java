package com.flipfit.exception;

/**
 * Exception thrown when booking operations fail.
 * This includes slot full, booking not found, or invalid booking operations.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class BookingException extends FlipFitException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new BookingException with the specified message.
     * 
     * @param message the detail message explaining the booking failure
     */
    public BookingException(String message) {
        super(message);
    }

    /**
     * Constructs a new BookingException with the specified message and cause.
     * 
     * @param message the detail message explaining the booking failure
     * @param cause the cause of the exception
     */
    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
