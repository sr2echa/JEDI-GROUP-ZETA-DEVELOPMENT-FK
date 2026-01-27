package com.flipfit.exception;

/**
 * Exception thrown when payment operations fail.
 * This includes payment processing errors, insufficient funds, or invalid payment methods.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class PaymentException extends FlipFitException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new PaymentException with the specified message.
     * 
     * @param message the detail message explaining the payment failure
     */
    public PaymentException(String message) {
        super(message);
    }

    /**
     * Constructs a new PaymentException with the specified message and cause.
     * 
     * @param message the detail message explaining the payment failure
     * @param cause the cause of the exception
     */
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
