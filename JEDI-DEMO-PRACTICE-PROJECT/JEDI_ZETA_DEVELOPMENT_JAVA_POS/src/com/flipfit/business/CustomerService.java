package com.flipfit.business;

import com.flipfit.bean.Booking;

import java.util.List;
import java.util.ArrayList;

public class CustomerService implements CustomerInterface {

    // Mock method to simulate DB check - this fixes your "not defined" error
    private boolean checkIsFull(String scheduleId) {
        // In a real app, you would fetch the SlotSchedule from a DAO/DB
        // For now, let's assume schedule "S1" is full
        return "S1".equals(scheduleId);
    }

    private void addToWaitlist(String userId, String scheduleId) {
        System.out.println("[SYSTEM] Slot Full. User " + userId + " added to Waitlist for " + scheduleId);
    }

    @Override
    public void bookWorkout(String userId, String scheduleId) {
        if (checkIsFull(scheduleId)) {
            addToWaitlist(userId, scheduleId);
        } else {
            System.out.println("[SYSTEM] Booking Confirmed for User: " + userId);
        }
    }

    @Override
    public void cancelWorkout(String bookingId) {
        System.out.println("[SYSTEM] Cancelling booking: " + bookingId);
        System.out.println("[SYSTEM] Triggering refund and promoting next user from Waitlist.");
    }

    @Override
    public List<Booking> getCustomerPlan(String userId) {
        System.out.println("[SYSTEM] Fetching plans for User: " + userId);
        // Returning an empty list as a mock implementation
        return new ArrayList<>();
    }
}