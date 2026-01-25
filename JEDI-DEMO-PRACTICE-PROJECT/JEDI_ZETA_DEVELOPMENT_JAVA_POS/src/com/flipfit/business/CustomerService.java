package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.SlotMaster;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerService implements CustomerInterface {
    private static List<Booking> bookings = new ArrayList<>();
    private BookingInterface bookingService = new BookingService();

    @Override
    public void bookWorkout(String userId, String slotId) {
        SlotMaster newSlot = GymOwnerService.getSlot(slotId);
        if (newSlot == null) {
            System.out.println("[ERROR] Slot ID " + slotId + " not found.");
            return;
        }

        // 1. Overlap Check & Auto-Cancel
        List<Booking> overlappingBookings = new ArrayList<>();
        for (Booking existing : bookings) {
            if (existing.getUserId().equals(userId) && existing.getStatus() == BookingStatus.CONFIRMED) {
                SlotMaster existingSlot = GymOwnerService.getSlot(existing.getScheduleId());
                if (existingSlot != null) {
                    if (newSlot.getStartTime().isBefore(existingSlot.getEndTime()) &&
                            newSlot.getEndTime().isAfter(existingSlot.getStartTime())) {
                        overlappingBookings.add(existing);
                    }
                }
            }
        }

        if (!overlappingBookings.isEmpty()) {
            System.out.println("[SYSTEM] Overlapping detected. Auto-cancelling previous bookings...");
            for (Booking overlap : overlappingBookings) {
                cancelWorkout(overlap.getBookingId());
            }
        }

        // 2. Booking / Waitlisting
        if (newSlot.getAvailableSeats() > 0) {
            Booking booking = new Booking();
            booking.setBookingId("B" + (bookings.size() + 101));
            booking.setUserId(userId);
            booking.setScheduleId(slotId);
            booking.setStatus(BookingStatus.CONFIRMED);
            bookings.add(booking);

            GymOwnerService.updateAvailability(slotId, -1);
            System.out.println("[SUCCESS] Booking Confirmed! Slot: " + slotId);
        } else {
            int position = bookingService.addCustomerToWaitlist(userId, slotId);
            System.out.println("[INFO] Slot FULL. You are on the WAITLIST at position: " + position);
        }
    }

    @Override
    public void cancelWorkout(String bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId().equals(bookingId) && b.getStatus() == BookingStatus.CONFIRMED) {
                b.setStatus(BookingStatus.CANCELLED);
                GymOwnerService.updateAvailability(b.getScheduleId(), 1);
                System.out.println("[SYSTEM] Booking " + bookingId + " cancelled.");

                Booking promoted = bookingService.promoteUserFromWaitlist(b.getScheduleId());
                if (promoted != null) {
                    bookings.add(promoted);
                    GymOwnerService.updateAvailability(b.getScheduleId(), -1);
                    System.out.println("[SYSTEM] Waitlist Alert: User " + promoted.getUserId()
                            + " has been auto-promoted to CONFIRMED.");
                }
                return;
            }
        }
        System.out.println("[ERROR] Booking ID " + bookingId + " not found or not confirmed.");
    }

    @Override
    public List<Booking> getCustomerPlan(String userId) {
        // 1. Confirmed Bookings
        List<Booking> confirmedPlan = bookings.stream()
                .filter(b -> b.getUserId().equals(userId) && b.getStatus() == BookingStatus.CONFIRMED)
                .collect(Collectors.toList());

        System.out.println("\n--- Your Fitness Plan for " + userId + " ---");

        if (confirmedPlan.isEmpty()) {
            System.out.println("[INFO] No active confirmed bookings.");
        } else {
            System.out.println("Confirmed Bookings:");
            for (Booking p : confirmedPlan) {
                SlotMaster s = GymOwnerService.getSlot(p.getScheduleId());
                String time = (s != null) ? s.getStartTime() + " - " + s.getEndTime() : "N/A";
                System.out.println(
                        "  - Booking ID: " + p.getBookingId() + " | Slot: " + p.getScheduleId() + " | Time: " + time);
            }
        }

        // 2. Waitlisted Bookings with dynamic positions
        List<String> waitlistEntries = bookingService.getUserWaitlist(userId);
        if (!waitlistEntries.isEmpty()) {
            System.out.println("\nWaitlisted Bookings:");
            for (String entry : waitlistEntries) {
                System.out.println("  - " + entry);
            }
        }

        return confirmedPlan;
    }
}