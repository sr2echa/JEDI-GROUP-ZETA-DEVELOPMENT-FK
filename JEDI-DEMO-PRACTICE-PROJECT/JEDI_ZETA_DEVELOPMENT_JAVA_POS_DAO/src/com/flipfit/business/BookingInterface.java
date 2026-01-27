package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.exception.*;
import java.util.List;

/**
 * Interface for booking-related operations including waitlist management.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public interface BookingInterface {
    /**
     * Adds a customer to the waitlist for a slot.
     * 
     * @param userId the ID of the customer
     * @param scheduleId the ID of the schedule/slot
     * @return the position in the waitlist (1-based)
     * @throws ValidationException if input validation fails
     * @throws BookingException if waitlist operation fails
     */
    int addCustomerToWaitlist(String userId, String scheduleId) 
            throws ValidationException, BookingException;

    /**
     * Promotes the first user from the waitlist to a booking.
     * 
     * @param scheduleId the ID of the schedule/slot
     * @return the promoted Booking object, or null if waitlist is empty
     * @throws ValidationException if scheduleId is invalid
     * @throws BookingException if promotion fails
     */
    Booking promoteUserFromWaitlist(String scheduleId) 
            throws ValidationException, BookingException;

    /**
     * Retrieves waitlist information for a user.
     * 
     * @param userId the ID of the user
     * @return a list of waitlist entries with position information
     * @throws ValidationException if userId is invalid
     */
    List<String> getUserWaitlist(String userId) throws ValidationException;
}