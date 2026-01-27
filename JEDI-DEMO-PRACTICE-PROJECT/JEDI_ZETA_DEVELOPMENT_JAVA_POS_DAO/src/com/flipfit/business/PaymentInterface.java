package com.flipfit.business;

import com.flipfit.exception.*;

/**
 * Interface for payment-related operations including processing payments, refunds, and payment history.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public interface PaymentInterface {
    /**
     * Process payment for a booking.
     * 
     * @param bookingId the booking ID to process payment for
     * @param amount the amount to be paid
     * @param paymentMethod payment method (e.g., "CARD", "UPI", "CASH")
     * @return true if payment is successful
     * @throws ValidationException if input validation fails
     * @throws PaymentException if payment processing fails
     * @throws BookingException if booking not found
     * @throws DatabaseException if database operation fails
     */
    boolean processPayment(String bookingId, double amount, String paymentMethod) 
            throws ValidationException, PaymentException, BookingException, DatabaseException;
    
    /**
     * Get pending payment amount for a booking.
     * 
     * @param bookingId the booking ID
     * @return the amount to be paid, or 0.0 if booking or slot not found
     * @throws ValidationException if bookingId is invalid
     * @throws DatabaseException if database operation fails
     */
    double getPendingAmount(String bookingId) throws ValidationException, DatabaseException;
    
    /**
     * Process refund for a cancelled booking.
     * 
     * @param bookingId the booking ID to refund
     * @param amount the amount to be refunded
     * @return true if refund initiated successfully
     * @throws ValidationException if input validation fails
     * @throws PaymentException if refund processing fails
     * @throws DatabaseException if database operation fails
     */
    boolean processRefund(String bookingId, double amount) 
            throws ValidationException, PaymentException, DatabaseException;
    
    /**
     * Check if a booking has been paid.
     * 
     * @param bookingId the booking ID
     * @return true if payment exists and is completed
     * @throws ValidationException if bookingId is invalid
     * @throws DatabaseException if database operation fails
     */
    boolean hasPayment(String bookingId) throws ValidationException, DatabaseException;
    
    /**
     * Get the amount paid for a booking.
     * 
     * @param bookingId the booking ID
     * @return the total paid amount
     * @throws ValidationException if bookingId is invalid
     * @throws DatabaseException if database operation fails
     */
    double getPaidAmount(String bookingId) throws ValidationException, DatabaseException;
}
