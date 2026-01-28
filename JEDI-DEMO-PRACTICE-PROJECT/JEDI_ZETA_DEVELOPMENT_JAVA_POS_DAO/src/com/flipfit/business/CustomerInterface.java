package com.flipfit.business;

import java.util.List;

import com.flipfit.bean.Booking;

public interface CustomerInterface {
    void bookWorkout(String userId, String scheduleId);

    void cancelWorkout(String bookingId);

    List<Booking> getCustomerPlan(String userId);
    
    boolean processPaymentAndConfirm(String bookingId, double amount, String paymentMethod);
    
    List<Booking> getPendingPayments(String userId);
    
    boolean cancelPendingBooking(String bookingId);
    
    boolean confirmBooking(String bookingId);
}
