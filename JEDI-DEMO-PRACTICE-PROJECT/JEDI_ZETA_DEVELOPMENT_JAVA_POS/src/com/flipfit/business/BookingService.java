package com.flipfit.business;

import com.flipfit.bean.BookingStatus;

public class BookingService implements BookingInterface {

    @Override
    public void addCustomerToWaitlist(String userId, String scheduleId) {
        // 1. Log the system event
        System.out.println("[SYSTEM_LOG] Creating Waitlist entry for User: " + userId);

        // 2. Logic: Create a new Booking bean with status WAITLISTED
        // In a real app: bookingDAO.createBooking(userId, scheduleId,
        // BookingStatus.WAITLISTED);

        System.out.println("[DB_UPDATE] User " + userId + " successfully added to Waitlist table.");
    }

    @Override
    public void promoteUserFromWaitlist(String scheduleId) {
        // 1. Logic: Fetch the first user from the waitlist for this schedule (FIFO)
        // String nextUserId = bookingDAO.getTopWaitlistedUser(scheduleId);

        String nextUserId = "USER_WAITING_01"; // Placeholder logic

        if (nextUserId != null) {
            // 2. Logic: Update their status from WAITLISTED to CONFIRMED
            // bookingDAO.updateBookingStatus(nextUserId, scheduleId,
            // BookingStatus.CONFIRMED);

            System.out.println("[DB_UPDATE] Waitlist Data Changed: User " + nextUserId + " promoted to CONFIRMED.");

            // 3. Trigger Notification
            System.out.println("[NOTIFICATION] Alerting user " + nextUserId + " of their new booking.");
        }
    }
}