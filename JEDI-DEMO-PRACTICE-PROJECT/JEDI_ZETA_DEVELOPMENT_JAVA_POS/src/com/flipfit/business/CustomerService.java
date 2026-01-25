package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerService implements CustomerInterface {
    private static List<Booking> bookings = new ArrayList<>();

    static {
        // Hardcoded data
        Booking b1 = new Booking();
        b1.setBookingId("B101");
        b1.setUserId("customer1");
        b1.setScheduleId("S1");
        b1.setStatus(BookingStatus.CONFIRMED);
        bookings.add(b1);
    }

    @Override
    public void bookWorkout(String userId, String scheduleId) {
        Booking booking = new Booking();
        booking.setBookingId("B" + (bookings.size() + 101));
        booking.setUserId(userId);
        booking.setScheduleId(scheduleId);
        booking.setStatus(BookingStatus.CONFIRMED);
        bookings.add(booking);
        System.out
                .println("[SYSTEM] Booking Confirmed for User: " + userId + ". Booking ID: " + booking.getBookingId());
    }

    @Override
    public void cancelWorkout(String bookingId) {
        boolean removed = bookings.removeIf(b -> b.getBookingId().equals(bookingId));
        if (removed) {
            System.out.println("[SYSTEM] Booking " + bookingId + " cancelled successfully.");
        } else {
            System.out.println("[ERROR] Booking ID " + bookingId + " not found.");
        }
    }

    @Override
    public List<Booking> getCustomerPlan(String userId) {
        List<Booking> userPlan = bookings.stream()
                .filter(b -> b.getUserId().equals(userId))
                .collect(Collectors.toList());

        if (userPlan.isEmpty()) {
            System.out.println("[INFO] No active plans found for user: " + userId);
        } else {
            System.out.println("[INFO] Displaying plans for " + userId + ":");
            userPlan.forEach(
                    p -> System.out.println(" - Booking ID: " + p.getBookingId() + ", Schedule: " + p.getScheduleId()));
        }
        return userPlan;
    }
}