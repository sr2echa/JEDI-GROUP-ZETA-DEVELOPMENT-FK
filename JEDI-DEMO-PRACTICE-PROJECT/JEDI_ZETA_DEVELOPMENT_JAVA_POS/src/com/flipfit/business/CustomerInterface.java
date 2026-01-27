package com.flipfit.business;

import java.util.List;

import com.flipfit.bean.Booking;

public interface CustomerInterface {
    void bookWorkout(String userId, String scheduleId);

    void cancelWorkout(String bookingId);

    List<Booking> getCustomerPlan(String userId);
    
    /**
     * Confirm a pending booking after payment has been processed.
     * Note: Payment processing must be completed before calling this method.
     * @param bookingId the ID of the booking to confirm
     * @return true if booking was confirmed successfully, false otherwise
     */
    boolean processPaymentAndConfirm(String bookingId);
    
    List<Booking> getPendingPayments(String userId);
    
    boolean cancelPendingBooking(String bookingId);
}
