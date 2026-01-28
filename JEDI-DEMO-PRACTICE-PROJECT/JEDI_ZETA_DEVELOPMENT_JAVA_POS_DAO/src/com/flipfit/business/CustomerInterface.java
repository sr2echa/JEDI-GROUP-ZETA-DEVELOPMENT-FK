package com.flipfit.business;

import java.util.List;
import com.flipfit.exception.BookingFailedException;
import com.flipfit.bean.Booking;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Interface CustomerInterface.
 *
 * @author Zeta
 * @ClassName  "CustomerInterface"
 */
public interface CustomerInterface {
	
	/**
	 * Book workout.
	 *
	 * @param userId the user id
	 * @param scheduleId the schedule id
	 * @throws BookingFailedException the booking failed exception
	 */
	void bookWorkout(String userId, String scheduleId) throws BookingFailedException;

    /**
     * Cancel workout.
     *
     * @param bookingId the booking id
     */
    void cancelWorkout(String bookingId);

    /**
     * Gets the customer plan.
     *
     * @param userId the user id
     * @return the customer plan
     */
    List<Booking> getCustomerPlan(String userId);
    
    /**
     * Confirm booking.
     * Confirms a booking after payment has been processed.
     *
     * @param bookingId the booking id
     * @param amount the amount
     * @param paymentMethod the payment method
     * @return true, if successful
     */
    boolean confirmBooking(String bookingId, double amount, String paymentMethod);
    
    /**
     * Gets the pending payments.
     *
     * @param userId the user id
     * @return the pending payments
     */
    List<Booking> getPendingPayments(String userId);
    
    /**
     * Cancel pending booking.
     *
     * @param bookingId the booking id
     * @return true, if successful
     */
    boolean cancelPendingBooking(String bookingId);
}