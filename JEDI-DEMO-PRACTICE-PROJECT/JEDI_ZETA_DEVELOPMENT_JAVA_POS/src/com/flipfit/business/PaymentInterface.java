package com.flipfit.business;

public interface PaymentInterface {
    /**
     * Process payment for a booking
     * @param bookingId The booking ID to process payment for
     * @param amount The amount to be paid
     * @param paymentMethod Payment method (e.g., "CARD", "UPI", "CASH")
     * @return true if payment is successful, false otherwise
     */
    boolean processPayment(String bookingId, double amount, String paymentMethod);
    
    /**
     * Get pending payment amount for a booking
     * @param bookingId The booking ID
     * @return The amount to be paid, or -1 if booking not found
     */
    double getPendingAmount(String bookingId);
    
    /**
     * Process refund for a cancelled booking
     * @param bookingId The booking ID to refund
     * @param amount The amount to be refunded
     * @return true if refund is successful, false otherwise
     */
    boolean processRefund(String bookingId, double amount);
    
    /**
     * Check if a booking has been paid
     * @param bookingId The booking ID
     * @return true if payment exists for this booking, false otherwise
     */
    boolean hasPayment(String bookingId);
    
    /**
     * Get the amount paid for a booking
     * @param bookingId The booking ID
     * @return The amount paid, or 0 if no payment found
     */
    double getPaidAmount(String bookingId);
}
