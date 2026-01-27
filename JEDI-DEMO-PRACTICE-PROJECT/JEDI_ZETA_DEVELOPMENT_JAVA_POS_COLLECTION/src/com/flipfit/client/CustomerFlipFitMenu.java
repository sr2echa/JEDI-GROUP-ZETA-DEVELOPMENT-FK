package com.flipfit.client;

import com.flipfit.business.*;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.SlotMaster;
import com.flipfit.bean.Booking;
import java.util.List;
import java.util.Scanner;

public class CustomerFlipFitMenu {
    CustomerInterface customerService = new CustomerService();
    GymOwnerInterface ownerService = new GymOwnerService();
    UserInterface userService = new UserService();
    PaymentService paymentService = new PaymentService();

    public void registerCustomer(Scanner sc) {
        System.out.println("\n--- Registration ---");
        System.out.print("Username: ");
        String username = sc.next();
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Password: ");
        String password = sc.next();

        if (userService.register(username, password, email, 2)) {
            System.out.println("[SYSTEM] Customer " + username + " Registration Successful!");
        }
    }

    public void displayMenu(Scanner sc, String userId) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Customer Dashboard (" + userId + ") ---");
            System.out.println("1. Book Workout Slot (Browse Centers)");
            System.out.println("2. View My Plan");
            System.out.println("3. Pay for Pending Bookings");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Back to Main Menu");
            System.out.println("6. View My Payment History");
            System.out.print("Choice: ");

            int choice = 0;
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            } else {
                sc.next();
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1:
                    browseAndBook(sc, userId);
                    break;
                case 2:
                    customerService.getCustomerPlan(userId);
                    break;
                case 3:
                    handlePendingPayments(sc, userId);
                    break;
                case 4:
                    System.out.print("Enter Booking ID: ");
                    customerService.cancelWorkout(sc.next());
                    break;
                case 5:
                    back = true;
                    break;
                case 6:
                    List<com.flipfit.bean.PaymentRecord> history = paymentService.getCustomerHistory(userId);
                    if (history.isEmpty()) {
                        System.out.println("\n[INFO] No payment history found.");
                    } else {
                        System.out.println("\n--- Your Payment History ---");
                        history.forEach(h -> System.out.println("TXN ID: " + h.getTransactionId() + " | Amount: ₹" + h.getAmount() + " | Status: " + h.getStatus() + " | Date: " + h.getTimestamp()));
                    }
                    break;
                default:
                    System.out.println("Invalid Selection.");
            }
        }
    }

    private void browseAndBook(Scanner sc, String userId) {
        // 1. Show all approved centers
        List<GymCenter> centers = ownerService.getAllCenters();
        if (centers.isEmpty()) {
            System.out.println("No gym centers currently available.");
            return;
        }

        System.out.println("\nAvailable Gym Centers:");
        centers.forEach(c -> System.out
                .println(" - ID: " + c.getCenterId() + " | Name: " + c.getName() + " | City: " + c.getCity()));

        System.out.print("\nEnter Center ID to view slots: ");
        String centerId = sc.next();

        // 2. Show slots for that center
        List<SlotMaster> slots = ownerService.viewSlots(centerId);
        if (slots.isEmpty()) {
            System.out.println("No slots found for this center.");
            return;
        }

        System.out.println("\nAvailable Slots for " + centerId + ":");
        slots.forEach(s -> System.out.println(" - Slot ID: " + s.getSlotId() +
                " | Timing: " + s.getStartTime() + " - " + s.getEndTime() +
                " | Seats Left: " + s.getAvailableSeats() + "/" + s.getCapacity() +
                " | Price: ₹" + s.getPrice()));

        System.out.print("\nEnter Slot ID to book: ");
        String slotId = sc.next();

        // 3. Get slot details for payment
        SlotMaster selectedSlot = slots.stream()
                .filter(s -> s.getSlotId().equals(slotId))
                .findFirst()
                .orElse(null);
        
        if (selectedSlot == null) {
            System.out.println("[ERROR] Slot ID not found.");
            return;
        }

        // 4. Create booking and immediately request payment
        customerService.bookWorkout(userId, slotId);
        
        // Get the latest booking that was just created
        List<Booking> pending = customerService.getPendingPayments(userId);
        if (!pending.isEmpty()) {
            Booking latestBooking = pending.get(pending.size() - 1);
            // Immediately process payment - no option to defer
            processPaymentForBooking(sc, latestBooking, selectedSlot.getPrice());
        }
    }

    private void handlePendingPayments(Scanner sc, String userId) {
        List<Booking> pendingBookings = customerService.getPendingPayments(userId);
        
        if (pendingBookings.isEmpty()) {
            System.out.println("\n[INFO] No pending payments.");
            return;
        }

        System.out.println("\n--- Pending Payments ---");
        for (int i = 0; i < pendingBookings.size(); i++) {
            Booking booking = pendingBookings.get(i);
            SlotMaster slot = GymOwnerService.getSlot(booking.getScheduleId());
            String time = (slot != null) ? slot.getStartTime() + " - " + slot.getEndTime() : "N/A";
            double amount = (slot != null) ? slot.getPrice() : 0.0;
            String source = booking.getBookingId().startsWith("B_PROM") ? " [From Waitlist]" : "";
            
            System.out.println((i + 1) + ". Booking ID: " + booking.getBookingId() + source + 
                    " | Slot: " + booking.getScheduleId() + 
                    " | Time: " + time + 
                    " | Amount: ₹" + amount);
        }

        System.out.print("\nEnter Booking ID to pay (or 'back' to go back): ");
        String input = sc.next();
        
        if (input.equalsIgnoreCase("back")) {
            return;
        }

        // Find the booking
        Booking selectedBooking = pendingBookings.stream()
                .filter(b -> b.getBookingId().equals(input))
                .findFirst()
                .orElse(null);

        if (selectedBooking == null) {
            System.out.println("[ERROR] Booking ID not found in pending payments.");
            return;
        }

        SlotMaster slot = GymOwnerService.getSlot(selectedBooking.getScheduleId());
        if (slot == null) {
            System.out.println("[ERROR] Slot information not found.");
            return;
        }

        processPaymentForBooking(sc, selectedBooking, slot.getPrice());
    }

    private void processPaymentForBooking(Scanner sc, Booking booking, double amount) {
        System.out.println("\n--- Payment Required ---");
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Amount: ₹" + amount);
        
        if (paymentService.processPaymentInteractive(sc, booking.getBookingId(), amount)) {
            // Payment successful, now confirm the booking
            String paymentMethod = paymentService.getPaymentMethod(booking.getBookingId());
            if (customerService.processPaymentAndConfirm(booking.getBookingId(), amount, paymentMethod)) {
                System.out.println("\n[SUCCESS] Your booking has been confirmed!");
            }
        } else {
            System.out.println("\n[ERROR] Payment was not completed. Booking has been cancelled.");
            // Cancel the booking if payment fails
            customerService.cancelPendingBooking(booking.getBookingId());
            // If it was from waitlist, we need to free up the slot for next person
            if (booking.getBookingId().startsWith("B_PROM")) {
                GymOwnerService.updateAvailability(booking.getScheduleId(), 1);
            }
        }
    }
}