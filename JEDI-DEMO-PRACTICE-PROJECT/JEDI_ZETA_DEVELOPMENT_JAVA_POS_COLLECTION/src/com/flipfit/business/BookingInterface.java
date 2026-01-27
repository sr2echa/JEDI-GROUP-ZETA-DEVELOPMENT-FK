package com.flipfit.business;

import com.flipfit.bean.Booking;
import java.util.List;

public interface BookingInterface {
    // Returns waitlist position
    int addCustomerToWaitlist(String userId, String scheduleId);

    // Returns the promoted Booking if successful, null otherwise
    Booking promoteUserFromWaitlist(String scheduleId);

    // Returns a summary of a user's waitlisted items with positions
    List<String> getUserWaitlist(String userId);
}