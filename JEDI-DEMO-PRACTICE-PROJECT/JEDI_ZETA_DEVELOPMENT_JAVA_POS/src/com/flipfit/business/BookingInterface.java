package com.flipfit.business;

public interface BookingInterface {
    // Logic to handle the transition from waitlist to confirmed
    void promoteUserFromWaitlist(String scheduleId);

    // Logic to handle initial waitlist placement
    void addCustomerToWaitlist(String userId, String scheduleId);
}