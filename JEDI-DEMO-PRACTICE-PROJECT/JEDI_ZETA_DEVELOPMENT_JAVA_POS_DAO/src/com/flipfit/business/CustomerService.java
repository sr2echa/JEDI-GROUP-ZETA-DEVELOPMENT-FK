package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.impl.GymCustomerDAOImpl;
import com.flipfit.exception.*;
import com.flipfit.utils.InputValidator;
import java.util.List;

/**
 * Service class for customer-related operations including booking, cancellation, and payment processing.
 * This service handles input validation and exception handling for customer operations.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class CustomerService implements CustomerInterface {
    private GymCustomerDAO customerDAO = new GymCustomerDAOImpl();

    /**
     * Books a workout slot for a customer.
     * Validates inputs and creates a booking with PENDING_PAYMENT status.
     * 
     * @param userId the ID of the customer booking the slot
     * @param slotId the ID of the slot to book
     * @throws ValidationException if input validation fails
     * @throws BookingException if booking operation fails
     * @throws DatabaseException if database operation fails
     */
    @Override
    public void bookWorkout(String userId, String slotId) 
            throws ValidationException, BookingException, DatabaseException {
        // Validate inputs
        try {
            InputValidator.validateId(userId, "User ID");
            InputValidator.validateId(slotId, "Slot ID");
        } catch (ValidationException e) {
            throw new ValidationException("Booking validation failed: " + e.getMessage(), e);
        }

        String bookingDate = java.time.LocalDate.now().plusDays(1).toString(); // Book for tomorrow
        System.out.println("[INFO] Booking slot " + slotId + " for User: " + userId + " on " + bookingDate);

        try {
            boolean success = customerDAO.bookSlot(userId, slotId, bookingDate);
            if (success) {
                System.out.println("[SUCCESS] Booking initiated! Please complete your payment to confirm.");
            } else {
                throw new BookingException("Booking failed. Slot might be full or not found.");
            }
        } catch (BookingException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to book slot: " + e.getMessage(), e);
        }
    }

    /**
     * Cancels a workout booking.
     * 
     * @param bookingId the ID of the booking to cancel
     * @throws ValidationException if bookingId is invalid
     * @throws BookingException if booking not found or cancellation fails
     * @throws DatabaseException if database operation fails
     */
    @Override
    public void cancelWorkout(String bookingId) 
            throws ValidationException, BookingException, DatabaseException {
        // Validate input
        try {
            InputValidator.validateId(bookingId, "Booking ID");
        } catch (ValidationException e) {
            throw new ValidationException("Cancel booking validation failed: " + e.getMessage(), e);
        }

        // Check if booking exists
        Booking booking = customerDAO.getBookingById(bookingId);
        if (booking == null) {
            throw new BookingException("Booking with ID '" + bookingId + "' not found");
        }

        try {
            customerDAO.cancelBooking(bookingId);
            System.out.println("[SUCCESS] Booking " + bookingId + " cancelled.");
        } catch (Exception e) {
            throw new DatabaseException("Failed to cancel booking: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves the customer's fitness plan (all bookings).
     * 
     * @param userId the ID of the customer
     * @return a list of bookings for the customer
     * @throws ValidationException if userId is invalid
     * @throws DatabaseException if database operation fails
     */
    @Override
    public List<Booking> getCustomerPlan(String userId) throws ValidationException, DatabaseException {
        // Validate input
        try {
            InputValidator.validateId(userId, "User ID");
        } catch (ValidationException e) {
            throw new ValidationException("Get customer plan validation failed: " + e.getMessage(), e);
        }

        try {
            List<Booking> list = customerDAO.viewMyBookings(userId);
            System.out.println("\n--- Your Fitness Plan for " + userId + " ---");
            for (Booking b : list) {
                System.out.println("Booking ID: " + b.getBookingId() + " | Slot: " + b.getScheduleId() + " | Status: "
                        + b.getStatus());
            }
            return list;
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve customer plan: " + e.getMessage(), e);
        }
    }

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
    @Override
    public boolean processPaymentAndConfirm(String bookingId, double amount, String paymentMethod) 
            throws ValidationException, PaymentException, BookingException, DatabaseException {
        // Validate inputs
        try {
            InputValidator.validateId(bookingId, "Booking ID");
            InputValidator.validateAmount(amount);
            InputValidator.validateNotNullOrEmpty(paymentMethod, "Payment method");
        } catch (ValidationException e) {
            throw new ValidationException("Payment validation failed: " + e.getMessage(), e);
        }

        // Verify booking exists
        Booking booking = customerDAO.getBookingById(bookingId);
        if (booking == null) {
            throw new BookingException("Booking with ID '" + bookingId + "' not found");
        }

        // Delegate to payment service
        PaymentService paymentService = new PaymentService();
        try {
            boolean paymentSuccess = paymentService.processPayment(bookingId, amount, paymentMethod);
            if (!paymentSuccess) {
                throw new PaymentException("Payment failed for booking " + bookingId);
            }

            // Payment succeeded, update status to CONFIRMED
            customerDAO.updateBookingStatus(bookingId, BookingStatus.CONFIRMED.toString());
            System.out.println("[SUCCESS] Payment and confirmation completed for booking " + bookingId);
            return true;
        } catch (PaymentException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to process payment and confirm booking: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all pending payments for a customer.
     * 
     * @param userId the ID of the customer
     * @return a list of bookings with PENDING_PAYMENT status
     * @throws ValidationException if userId is invalid
     * @throws DatabaseException if database operation fails
     */
    @Override
    public List<Booking> getPendingPayments(String userId) throws ValidationException, DatabaseException {
        // Validate input
        try {
            InputValidator.validateId(userId, "User ID");
        } catch (ValidationException e) {
            throw new ValidationException("Get pending payments validation failed: " + e.getMessage(), e);
        }

        try {
            // Get bookings with PENDING_PAYMENT status
            List<Booking> allBookings = customerDAO.viewMyBookings(userId);
            return allBookings.stream()
                    .filter(b -> b.getStatus() == BookingStatus.PENDING_PAYMENT)
                    .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve pending payments: " + e.getMessage(), e);
        }
    }

    /**
     * Cancels a pending booking.
     * 
     * @param bookingId the ID of the booking to cancel
     * @return true if cancellation successful
     * @throws ValidationException if bookingId is invalid
     * @throws BookingException if booking not found or cancellation fails
     * @throws DatabaseException if database operation fails
     */
    @Override
    public boolean cancelPendingBooking(String bookingId) 
            throws ValidationException, BookingException, DatabaseException {
        // Validate input
        try {
            InputValidator.validateId(bookingId, "Booking ID");
        } catch (ValidationException e) {
            throw new ValidationException("Cancel pending booking validation failed: " + e.getMessage(), e);
        }

        // Check if booking exists
        Booking booking = customerDAO.getBookingById(bookingId);
        if (booking == null) {
            throw new BookingException("Booking with ID '" + bookingId + "' not found");
        }

        try {
            customerDAO.cancelBooking(bookingId);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to cancel pending booking: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a booking by its ID.
     * 
     * @param bookingId the ID of the booking
     * @return the Booking object if found, null otherwise
     * @throws ValidationException if bookingId is invalid
     * @throws DatabaseException if database operation fails
     */
    public Booking getBookingById(String bookingId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(bookingId, "Booking ID");
        } catch (ValidationException e) {
            throw new ValidationException("Get booking validation failed: " + e.getMessage(), e);
        }

        try {
            return customerDAO.getBookingById(bookingId);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve booking: " + e.getMessage(), e);
        }
    }

    /**
     * Cancels all bookings for a specific slot.
     * This is typically used when a slot is removed or modified.
     * 
     * @param slotId the ID of the slot
     * @throws ValidationException if slotId is invalid
     * @throws DatabaseException if database operation fails
     */
    public static void cancelAllBookingsForSlot(String slotId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(slotId, "Slot ID");
            // Implementation would cancel all bookings for this slot
            // This is a placeholder for future implementation
        } catch (ValidationException e) {
            throw new ValidationException("Cancel all bookings validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to cancel all bookings for slot: " + e.getMessage(), e);
        }
    }
}