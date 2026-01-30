package com.flipfit.business;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Interface PaymentInterface.
 *
 * @author Zeta
 * @ClassName  "PaymentInterface"
 */
public interface PaymentInterface {
    
    /**
     * Process payment.
     *
     * @param bookingId the booking id
     * @param amount the amount
     * @param paymentMethod the payment method
     * @return true, if successful
     */
    boolean processPayment(String bookingId, double amount, String paymentMethod);
    
    /**
     * Gets the pending amount.
     *
     * @param bookingId the booking id
     * @return the pending amount
     */
    double getPendingAmount(String bookingId);
    
    /**
     * Process refund.
     *
     * @param bookingId the booking id
     * @param amount the amount
     * @return true, if successful
     */
    boolean processRefund(String bookingId, double amount);
    
    /**
     * Checks for payment.
     *
     * @param bookingId the booking id
     * @return true, if successful
     */
    boolean hasPayment(String bookingId);
    
    /**
     * Gets the paid amount.
     *
     * @param bookingId the booking id
     * @return the paid amount
     */
    double getPaidAmount(String bookingId);
}