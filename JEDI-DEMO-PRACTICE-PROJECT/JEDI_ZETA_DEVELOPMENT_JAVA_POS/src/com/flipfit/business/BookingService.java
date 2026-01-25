package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class BookingService implements BookingInterface {
    // In-memory waitlist map: ScheduleID -> List of Bookings (Waiting)
    private static Map<String, List<Booking>> waitlistMap = new HashMap<>();

    @Override
    public void addCustomerToWaitlist(String userId, String scheduleId) {
        Booking waitlistEntry = new Booking();
        waitlistEntry.setBookingId("W" + System.currentTimeMillis());
        waitlistEntry.setUserId(userId);
        waitlistEntry.setScheduleId(scheduleId);
        waitlistEntry.setStatus(BookingStatus.WAITLISTED);

        waitlistMap.computeIfAbsent(scheduleId, k -> new ArrayList<>()).add(waitlistEntry);

        System.out.println("[SYSTEM] Slot Full. User " + userId + " added to WAITLIST for " + scheduleId);
    }

    @Override
    public void promoteUserFromWaitlist(String scheduleId) {
        List<Booking> waitlist = waitlistMap.get(scheduleId);

        if (waitlist != null && !waitlist.isEmpty()) {
            // FIFO Promotion
            Booking promotedBooking = waitlist.remove(0);
            promotedBooking.setStatus(BookingStatus.CONFIRMED);

            System.out.println("[SYSTEM] Progressing Waitlist: User " + promotedBooking.getUserId()
                    + " promoted to CONFIRMED for " + scheduleId);

            // In a real app, this would then be moved to the active bookings list in
            // CustomerService
        } else {
            System.out.println("[SYSTEM] No users found in waitlist for schedule: " + scheduleId);
        }
    }
}