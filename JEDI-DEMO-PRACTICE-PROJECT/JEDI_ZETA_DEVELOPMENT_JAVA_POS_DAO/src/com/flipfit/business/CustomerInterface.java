package com.flipfit.business;

import java.util.List;
import com.flipfit.bean.Booking;
import com.flipfit.exception.*;

/**
 * Interface for customer-related operations including booking, cancellation, and payment processing.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public interface CustomerInterface {
    /**
     * Books a workout slot for a customer.
     * 
     * @param userId the ID of the customer booking the slot
     * @param scheduleId the ID of the slot to book
     * @throws ValidationException if input validation fails
     * @throws BookingException if booking operation fails
     * @throws DatabaseException if database operation fails
     */
    void bookWorkout(String userId, String scheduleId) 
            throws ValidationException, BookingException, DatabaseException;

    /**
     * Cancels a workout booking.
     * 
     * @param bookingId the ID of the booking to cancel
     * @throws ValidationException if bookingId is invalid
     * @throws BookingException if booking not found or cancellation fails
     * @throws DatabaseException if database operation fails
     */
    void cancelWorkout(String bookingId) 
            throws ValidationException, BookingException, DatabaseException;

    /**
     * Retrieves the customer's fitness plan (all bookings).
     * 
     * @param userId the ID of the customer
     * @return a list of bookings for the customer
     * @throws ValidationException if userId is invalid
     * @throws DatabaseException if database operation fails
     */
    List<Booking> getCustomerPlan(String userId) throws ValidationException, DatabaseException;
    
    /**
     * Processes payment and confirms a booking.
     * 
     * @param bookingId the ID of the booking
     * @param amount the payment amount
     * @param paymentMethod the payment method used
     * @return true if payment and confirmation successful
     * @throws ValidationException if input validation fails
     * @throws PaymentException if payment processing fails
     * @throws BookingException if booking not found or confirmation fails
     * @throws DatabaseException if database operation fails
     */
    boolean processPaymentAndConfirm(String bookingId, double amount, String paymentMethod) 
            throws ValidationException, PaymentException, BookingException, DatabaseException;
    
    /**
     * Retrieves all pending payments for a customer.
     * 
     * @param userId the ID of the customer
     * @return a list of bookings with PENDING_PAYMENT status
     * @throws ValidationException if userId is invalid
     * @throws DatabaseException if database operation fails
     */
    List<Booking> getPendingPayments(String userId) throws ValidationException, DatabaseException;
    
    /**
     * Cancels a pending booking.
     * 
     * @param bookingId the ID of the booking to cancel
     * @return true if cancellation successful
     * @throws ValidationException if bookingId is invalid
     * @throws BookingException if booking not found or cancellation fails
     * @throws DatabaseException if database operation fails
     */
    boolean cancelPendingBooking(String bookingId) 
            throws ValidationException, BookingException, DatabaseException;
}
