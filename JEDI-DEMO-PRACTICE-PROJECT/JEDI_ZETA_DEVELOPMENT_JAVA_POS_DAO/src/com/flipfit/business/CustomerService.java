package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.impl.GymCustomerDAOImpl;
import com.flipfit.exception.BookingFailedException;
import java.util.List;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class CustomerService.
 *
 * @author Zeta
 * @ClassName  "CustomerService"
 */
public class CustomerService implements CustomerInterface {
    
    /** The customer DAO. */
    private GymCustomerDAO customerDAO = new GymCustomerDAOImpl();

    /**
     * Book workout.
     *
     * @param userId the user id
     * @param slotId the slot id
     * @throws BookingFailedException the booking failed exception
     */
    @Override
    public void bookWorkout(String userId, String slotId) throws BookingFailedException {
        String bookingDate = java.time.LocalDate.now().plusDays(1).toString(); 
        System.out.println("[INFO] Booking slot " + slotId + " for User: " + userId + " on " + bookingDate);

        boolean success = customerDAO.bookSlot(userId, slotId, bookingDate);
        if (success) {
            System.out.println("[SUCCESS] Booking initiated! Please complete your payment to confirm.");
        } else {
            throw new BookingFailedException("[ERROR] Booking failed. Slot " + slotId + " might be full or not found.");
        }
    }

    /**
     * Cancel workout.
     *
     * @param bookingId the booking id
     */
    @Override
    public void cancelWorkout(String bookingId) {
        customerDAO.cancelBooking(bookingId);
        System.out.println("[SUCCESS] Booking " + bookingId + " cancelled.");
    }

    /**
     * Gets the customer plan.
     *
     * @param userId the user id
     * @return the customer plan
     */
    @Override
    public List<Booking> getCustomerPlan(String userId) {
        List<Booking> list = customerDAO.viewMyBookings(userId);
        System.out.println("\n--- Your Fitness Plan for " + userId + " ---");
        for (Booking b : list) {
            System.out.println("Booking ID: " + b.getBookingId() + " | Slot: " + b.getScheduleId() + " | Status: "
                    + b.getStatus());
        }
        return list;
    }

    /**
     * Process payment and confirm.
     *
     * @param bookingId the booking id
     * @param amount the amount
     * @param paymentMethod the payment method
     * @return true, if successful
     */
    @Override
    public boolean processPaymentAndConfirm(String bookingId, double amount, String paymentMethod) {
        PaymentService paymentService = new PaymentService();
        boolean paymentSuccess = paymentService.processPayment(bookingId, amount, paymentMethod);

        if (!paymentSuccess) {
            System.err.println("[ERROR] processPaymentAndConfirm: Payment failed for booking " + bookingId);
            return false;
        }

        customerDAO.updateBookingStatus(bookingId, BookingStatus.CONFIRMED.toString());
        System.out.println("[SUCCESS] Payment and confirmation completed for booking " + bookingId);
        return true;
    }

    /**
     * Gets the pending payments.
     *
     * @param userId the user id
     * @return the pending payments
     */
    @Override
    public List<Booking> getPendingPayments(String userId) {
        List<Booking> allBookings = customerDAO.viewMyBookings(userId);
        return allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.PENDING_PAYMENT)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Cancel pending booking.
     *
     * @param bookingId the booking id
     * @return true, if successful
     */
    @Override
    public boolean cancelPendingBooking(String bookingId) {
        customerDAO.cancelBooking(bookingId);
        return true;
    }

    /**
     * Gets the booking by id.
     *
     * @param bookingId the booking id
     * @return the booking
     */
    public Booking getBookingById(String bookingId) {
        return customerDAO.getBookingById(bookingId);
    }
}