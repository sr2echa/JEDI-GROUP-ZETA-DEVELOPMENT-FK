package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class BookingService.
 *
 * @author Zeta
 * @ClassName  "BookingService"
 */
public class BookingService implements BookingInterface {
    
    /** The waitlist map. */
    private static Map<String, List<Booking>> waitlistMap = new HashMap<>();

    /**
     * Adds the customer to waitlist.
     *
     * @param userId the user id
     * @param scheduleId the schedule id
     * @return the int
     */
    @Override
    public int addCustomerToWaitlist(String userId, String scheduleId) {
        Booking waitlistEntry = new Booking();
        waitlistEntry.setBookingId("WLT" + System.currentTimeMillis());
        waitlistEntry.setUserId(userId);
        waitlistEntry.setScheduleId(scheduleId);
        waitlistEntry.setStatus(BookingStatus.WAITLISTED);

        List<Booking> waitlist = waitlistMap.computeIfAbsent(scheduleId, k -> new ArrayList<>());
        waitlist.add(waitlistEntry);

        return waitlist.size();
    }

    /**
     * Promote user from waitlist.
     *
     * @param scheduleId the schedule id
     * @return the booking
     */
    @Override
    public Booking promoteUserFromWaitlist(String scheduleId) {
        List<Booking> waitlist = waitlistMap.get(scheduleId);

        if (waitlist != null && !waitlist.isEmpty()) {
            Booking promotedBooking = waitlist.remove(0);
            promotedBooking.setBookingId("B_PROM" + System.currentTimeMillis());
            promotedBooking.setStatus(BookingStatus.PENDING_PAYMENT);
            promotedBooking.setCreatedAt(java.time.LocalDateTime.now());

            System.out.println("[SYSTEM] Waitlist Progress for " + scheduleId + ": User " + promotedBooking.getUserId()
                    + " has been promoted from waitlist. Payment required to confirm booking.");
            return promotedBooking;
        }
        return null;
    }

    /**
     * Gets the user waitlist.
     *
     * @param userId the user id
     * @return the user waitlist
     */
    @Override
    public List<String> getUserWaitlist(String userId) {
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