package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.exception.*;
import com.flipfit.utils.InputValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Service class for booking-related operations including waitlist management.
 * This service handles input validation and exception handling for booking operations.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class BookingService implements BookingInterface {
    private static Map<String, List<Booking>> waitlistMap = new HashMap<>();

    /**
     * Adds a customer to the waitlist for a slot.
     * 
     * @param userId the ID of the customer
     * @param scheduleId the ID of the schedule/slot
     * @return the position in the waitlist (1-based)
     * @throws ValidationException if input validation fails
     * @throws BookingException if waitlist operation fails
     */
    @Override
    public int addCustomerToWaitlist(String userId, String scheduleId) 
            throws ValidationException, BookingException {
        // Validate inputs
        try {
            InputValidator.validateId(userId, "User ID");
            InputValidator.validateId(scheduleId, "Schedule ID");
        } catch (ValidationException e) {
            throw new ValidationException("Add to waitlist validation failed: " + e.getMessage(), e);
        }

        try {
            Booking waitlistEntry = new Booking();
            waitlistEntry.setBookingId("WLT" + System.currentTimeMillis());
            waitlistEntry.setUserId(userId);
            waitlistEntry.setScheduleId(scheduleId);
            waitlistEntry.setStatus(BookingStatus.WAITLISTED);

            List<Booking> waitlist = waitlistMap.computeIfAbsent(scheduleId, k -> new ArrayList<>());
            waitlist.add(waitlistEntry);

            return waitlist.size();
        } catch (Exception e) {
            throw new BookingException("Failed to add customer to waitlist: " + e.getMessage(), e);
        }
    }

    /**
     * Promotes the first user from the waitlist to a booking.
     * 
     * @param scheduleId the ID of the schedule/slot
     * @return the promoted Booking object, or null if waitlist is empty
     * @throws ValidationException if scheduleId is invalid
     * @throws BookingException if promotion fails
     */
    @Override
    public Booking promoteUserFromWaitlist(String scheduleId) 
            throws ValidationException, BookingException {
        // Validate input
        try {
            InputValidator.validateId(scheduleId, "Schedule ID");
        } catch (ValidationException e) {
            throw new ValidationException("Promote from waitlist validation failed: " + e.getMessage(), e);
        }

        try {
            List<Booking> waitlist = waitlistMap.get(scheduleId);

            if (waitlist != null && !waitlist.isEmpty()) {
                Booking promotedBooking = waitlist.remove(0);
                promotedBooking.setBookingId("B_PROM" + System.currentTimeMillis());
                // Set to PENDING_PAYMENT instead of CONFIRMED - payment required
                promotedBooking.setStatus(BookingStatus.PENDING_PAYMENT);
                promotedBooking.setCreatedAt(java.time.LocalDateTime.now());

                System.out.println("[SYSTEM] Waitlist Progress for " + scheduleId + ": User " + promotedBooking.getUserId()
                        + " has been promoted from waitlist. Payment required to confirm booking.");
                return promotedBooking;
            }
            return null;
        } catch (Exception e) {
            throw new BookingException("Failed to promote user from waitlist: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves waitlist information for a user.
     * 
     * @param userId the ID of the user
     * @return a list of waitlist entries with position information
     * @throws ValidationException if userId is invalid
     */
    @Override
    public List<String> getUserWaitlist(String userId) throws ValidationException {
        // Validate input
        try {
            InputValidator.validateId(userId, "User ID");
        } catch (ValidationException e) {
            throw new ValidationException("Get user waitlist validation failed: " + e.getMessage(), e);
        }

        List<String> userWaitlistDetails = new ArrayList<>();

        for (Map.Entry<String, List<Booking>> entry : waitlistMap.entrySet()) {
            String scheduleId = entry.getKey();
            List<Booking> waitlist = entry.getValue();

            for (int i = 0; i < waitlist.size(); i++) {
                if (waitlist.get(i).getUserId().equals(userId)) {
                    userWaitlistDetails.add("Slot: " + scheduleId + " | Position: " + (i + 1));
                }
            }
        }
        return userWaitlistDetails;
    }
}