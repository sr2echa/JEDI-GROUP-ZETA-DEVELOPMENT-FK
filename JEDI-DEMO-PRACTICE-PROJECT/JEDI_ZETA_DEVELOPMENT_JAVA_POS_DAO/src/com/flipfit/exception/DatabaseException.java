package com.flipfit.exception;

/**
 * Exception thrown when database operations fail.
 * This includes connection errors, SQL errors, or transaction failures.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class DatabaseException extends FlipFitException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new DatabaseException with the specified message.
     * 
     * @param message the detail message explaining the database error
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructs a new DatabaseException with the specified message and cause.
     * 
     * @param message the detail message explaining the database error
     * @param cause the cause of the exception (typically a SQLException)
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
