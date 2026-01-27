package com.flipfit.business;

import com.flipfit.bean.Booking;
import java.util.List;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Interface BookingInterface.
 *
 * @author Zeta
 * @ClassName  "BookingInterface"
 */
public interface BookingInterface {
    
    /**
     * Adds the customer to waitlist.
     *
     * @param userId the user id
     * @param scheduleId the schedule id
     * @return the int
     */
    int addCustomerToWaitlist(String userId, String scheduleId);

    /**
     * Promote user from waitlist.
     *
     * @param scheduleId the schedule id
     * @return the booking
     */
    Booking promoteUserFromWaitlist(String scheduleId);

    /**
     * Gets the user waitlist.
     *
     * @param userId the user id
     * @return the user waitlist
     */
    List<String> getUserWaitlist(String userId);
}