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
    private PaymentInterface paymentService = new PaymentService();

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
                // cancelWorkout already handles refunds for confirmed bookings
                cancelWorkout(overlap.getBookingId());
            }
        }

        // 2. Booking / Waitlisting
        if (newSlot.getAvailableSeats() > 0) {
            Booking booking = new Booking();
            booking.setBookingId("B" + (bookings.size() + 101));
            booking.setUserId(userId);
            booking.setScheduleId(slotId);
            booking.setStatus(BookingStatus.PENDING_PAYMENT);
            booking.setCreatedAt(java.time.LocalDateTime.now());
            bookings.add(booking);

            // Reserve the slot temporarily (don't reduce availability yet)
            // Availability will be reduced only after payment confirmation
            System.out.println("[INFO] Slot reserved! Booking ID: " + booking.getBookingId());
            System.out.println("[INFO] Amount to Pay: ₹" + newSlot.getPrice());
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
                
                // Process refund if payment was made
                if (paymentService.hasPayment(bookingId)) {
                    SlotMaster slot = GymOwnerService.getSlot(b.getScheduleId());
                    double refundAmount = (slot != null) ? slot.getPrice() : paymentService.getPaidAmount(bookingId);
                    
                    if (paymentService.processRefund(bookingId, refundAmount)) {
                        System.out.println("[SYSTEM] Booking " + bookingId + " cancelled. Refund initiated.");
                    } else {
                        System.out.println("[SYSTEM] Booking " + bookingId + " cancelled. Refund processing failed - please contact support.");
                    }
                } else {
                    System.out.println("[SYSTEM] Booking " + bookingId + " cancelled.");
                }

                Booking promoted = bookingService.promoteUserFromWaitlist(b.getScheduleId());
                if (promoted != null) {
                    bookings.add(promoted);
                    // Don't reduce availability yet - wait for payment confirmation
                    SlotMaster slot = GymOwnerService.getSlot(promoted.getScheduleId());
                    if (slot != null) {
                        System.out.println("[SYSTEM] Waitlist Alert: User " + promoted.getUserId()
                                + " has been promoted. Booking ID: " + promoted.getBookingId()
                                + " | Amount to Pay: ₹" + slot.getPrice());
                        System.out.println("[SYSTEM] User must complete payment to confirm the booking.");
                    }
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

        // 2. Pending Payment Bookings (including waitlist promotions)
        List<Booking> pendingPayments = bookings.stream()
                .filter(b -> b.getUserId().equals(userId) && b.getStatus() == BookingStatus.PENDING_PAYMENT)
                .collect(Collectors.toList());
        
        if (!pendingPayments.isEmpty()) {
            System.out.println("\nPending Payment Bookings (Payment Required):");
            for (Booking p : pendingPayments) {
                SlotMaster s = GymOwnerService.getSlot(p.getScheduleId());
                String time = (s != null) ? s.getStartTime() + " - " + s.getEndTime() : "N/A";
                double amount = (s != null) ? s.getPrice() : 0.0;
                String source = p.getBookingId().startsWith("B_PROM") ? " [From Waitlist]" : "";
                System.out.println(
                        "  - Booking ID: " + p.getBookingId() + source + " | Slot: " + p.getScheduleId() + 
                        " | Time: " + time + " | Amount: ₹" + amount);
            }
            System.out.println("[INFO] Please complete payment to confirm these bookings.");
        }

        // 3. Waitlisted Bookings with dynamic positions
        List<String> waitlistEntries = bookingService.getUserWaitlist(userId);
        if (!waitlistEntries.isEmpty()) {
            System.out.println("\nWaitlisted Bookings:");
            for (String entry : waitlistEntries) {
                System.out.println("  - " + entry);
            }
        }

        return confirmedPlan;
    }

    /**
     * Process payment for a pending booking and confirm it
     */
    public boolean processPaymentAndConfirm(String bookingId, double amount, String paymentMethod) {
        Booking booking = getBookingById(bookingId);
        if (booking == null) {
            System.out.println("[ERROR] Booking ID " + bookingId + " not found.");
            return false;
        }

        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            System.out.println("[ERROR] Booking " + bookingId + " is not pending payment.");
            return false;
        }

        // REMOVED: paymentService.processPayment call here because it is 
        // already called in CustomerFlipFitMenu.java.
        
        // Confirm booking
        booking.setStatus(BookingStatus.CONFIRMED);
        GymOwnerService.updateAvailability(booking.getScheduleId(), -1);
        System.out.println("[SUCCESS] Booking Confirmed! Slot: " + booking.getScheduleId());
        return true;
    }

    /**
     * Get all pending payment bookings for a user
     */
    public List<Booking> getPendingPayments(String userId) {
        return bookings.stream()
                .filter(b -> b.getUserId().equals(userId) && b.getStatus() == BookingStatus.PENDING_PAYMENT)
                .collect(Collectors.toList());
    }

    /**
     * Cancel a pending payment booking (before payment)
     */
    public boolean cancelPendingBooking(String bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking == null) {
            System.out.println("[ERROR] Booking ID " + bookingId + " not found.");
            return false;
        }

        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            System.out.println("[ERROR] Booking " + bookingId + " is not pending payment.");
            return false;
        }

        booking.setStatus(BookingStatus.CANCELLED);
        System.out.println("[SYSTEM] Pending booking " + bookingId + " cancelled.");
        return true;
    }

    /**
     * Get booking by ID (static method for PaymentService access)
     */
    public static Booking getBookingById(String bookingId) {
        return bookings.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Cancel all bookings for a slot (used when slot is deleted/cancelled by gym owner/admin)
     * Processes refunds for all confirmed bookings
     */
    public static void cancelAllBookingsForSlot(String slotId) {
        List<Booking> slotBookings = bookings.stream()
                .filter(b -> b.getScheduleId().equals(slotId) && 
                            (b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.PENDING_PAYMENT))
                .collect(Collectors.toList());
        
        if (slotBookings.isEmpty()) {
            return;
        }
        
        PaymentService paymentService = new PaymentService();
        SlotMaster slot = GymOwnerService.getSlot(slotId);
        double refundAmount = (slot != null) ? slot.getPrice() : 0.0;
        
        System.out.println("[SYSTEM] Slot " + slotId + " has been cancelled. Processing cancellations and refunds...");
        
        for (Booking booking : slotBookings) {
            boolean wasConfirmed = (booking.getStatus() == BookingStatus.CONFIRMED);
            booking.setStatus(BookingStatus.CANCELLED);
            
            // Process refund only for confirmed bookings that were paid
            if (wasConfirmed && paymentService.hasPayment(booking.getBookingId())) {
                double actualRefund = (refundAmount > 0) ? refundAmount : paymentService.getPaidAmount(booking.getBookingId());
                paymentService.processRefund(booking.getBookingId(), actualRefund);
            }
            
            System.out.println("[SYSTEM] Booking " + booking.getBookingId() + " cancelled due to slot cancellation.");
        }
        
        // Update availability (in case slot is being modified, not deleted)
        if (slot != null) {
            slot.setAvailableSeats(slot.getCapacity());
        }
    }
}