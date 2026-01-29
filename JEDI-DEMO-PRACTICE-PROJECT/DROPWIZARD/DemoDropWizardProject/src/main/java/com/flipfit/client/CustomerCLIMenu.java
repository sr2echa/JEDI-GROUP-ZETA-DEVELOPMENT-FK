package com.flipfit.client;

import java.util.*;

/**
 * Customer CLI Menu - Makes HTTP requests for customer operations
 */
public class CustomerCLIMenu {

    public void displayMenu(Scanner sc, String userId) {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Customer Dashboard (" + userId + ") ---");
            System.out.println("1. Browse and Book Slots");
            System.out.println("2. View My Plan");
            System.out.println("3. Pay for Pending Bookings");
            System.out.println("4. Cancel Booking");
            System.out.println("5. View Payment History");
            System.out.println("6. View Notifications");
            System.out.println("7. Back to Main Menu");
            System.out.print("Choice: ");

            int choice = getIntInput(sc);

            try {
                switch (choice) {
                    case 1:
                        browseAndBook(sc, userId);
                        break;
                    case 2:
                        viewCustomerPlan(sc, userId);
                        break;
                    case 3:
                        handlePendingPayments(sc, userId);
                        break;
                    case 4:
                        cancelBooking(sc);
                        break;
                    case 5:
                        viewPaymentHistory(sc, userId);
                        break;
                    case 6:
                        viewNotifications(sc, userId);
                        break;
                    case 7:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("\n✗ Error: " + e.getMessage());
            }
        }
    }

    private void browseAndBook(Scanner sc, String userId) throws Exception {
        Map<String, Object> response = HttpClientUtil.get("/centers");

        List<Map<String, Object>> centers = (List<Map<String, Object>>) response.get("centers");

        if (centers == null || centers.isEmpty()) {
            System.out.println("\n[INFO] No gym centers available.");
            return;
        }

        System.out.println("\n--- Available Gym Centers ---");
        for (Map<String, Object> center : centers) {
            System.out.println(" • ID: " + center.get("centerId") +
                    " | Name: " + center.get("name") +
                    " | City: " + center.get("city"));
        }

        System.out.print("\nEnter Center ID to view slots: ");
        String centerId = sc.nextLine();

        Map<String, Object> slotsResponse = HttpClientUtil.get("/slots/center/" + centerId);
        List<Map<String, Object>> slots = (List<Map<String, Object>>) slotsResponse.get("slots");

        if (slots == null || slots.isEmpty()) {
            System.out.println("\n[INFO] No slots available for this center.");
            return;
        }

        System.out.println("\n--- Available Slots ---");
        for (Map<String, Object> slot : slots) {
            System.out.println(" • Slot ID: " + slot.get("slotId") +
                    " | Time: " + slot.get("startTime") + " - " + slot.get("endTime") +
                    " | Seats: " + slot.get("availableSeats") + "/" + slot.get("capacity") +
                    " | Price: ₹" + slot.get("price"));
        }

        System.out.print("\nEnter Slot ID to book: ");
        String slotId = sc.nextLine();

        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("slotId", slotId);

        Map<String, Object> bookingResponse = HttpClientUtil.post("/customers/" + userId + "/bookings", bookingData);

        if (bookingResponse.get("success") != null && (Boolean) bookingResponse.get("success")) {
            System.out.println("\n✓ " + bookingResponse.get("message"));
            // Now handle payment for the booking
            handlePendingPayments(sc, userId);
        } else {
            System.out.println("\n✗ Booking failed: " + bookingResponse.get("error"));
        }
    }

    private void viewCustomerPlan(Scanner sc, String userId) throws Exception {
        Map<String, Object> response = HttpClientUtil.get("/customers/" + userId + "/plan");

        List<Map<String, Object>> bookings = (List<Map<String, Object>>) response.get("bookings");

        if (bookings == null || bookings.isEmpty()) {
            System.out.println("\n[INFO] No bookings found.");
            return;
        }

        System.out.println("\n--- Your Fitness Plan ---");
        for (Map<String, Object> booking : bookings) {
            System.out.println(" • Booking ID: " + booking.get("bookingId") +
                    " | Slot: " + booking.get("scheduleId") +
                    " | Status: " + booking.get("status"));
        }
    }

    private void handlePendingPayments(Scanner sc, String userId) throws Exception {
        Map<String, Object> response = HttpClientUtil.get("/customers/" + userId + "/payments/pending");

        List<Map<String, Object>> pendingBookings = (List<Map<String, Object>>) response.get("pendingBookings");

        if (pendingBookings == null || pendingBookings.isEmpty()) {
            System.out.println("\n[INFO] No pending payments.");
            return;
        }

        System.out.println("\n--- Pending Payments ---");
        for (int i = 0; i < pendingBookings.size(); i++) {
            Map<String, Object> booking = pendingBookings.get(i);
            System.out.println((i + 1) + ". Booking ID: " + booking.get("bookingId") +
                    " | Slot: " + booking.get("scheduleId"));
        }

        System.out.print("\nEnter Booking ID to pay (or 'back'): ");
        String bookingId = sc.nextLine();

        if (bookingId.equalsIgnoreCase("back")) {
            return;
        }

        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        System.out.print("Payment Method (CARD/UPI/NETBANKING): ");
        String paymentMethod = sc.nextLine();

        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("amount", amount);
        paymentData.put("paymentMethod", paymentMethod);

        Map<String, Object> confirmResponse = HttpClientUtil.post("/customers/bookings/" + bookingId + "/confirm",
                paymentData);

        if (confirmResponse.get("success") != null && (Boolean) confirmResponse.get("success")) {
            System.out.println("\n✓ " + confirmResponse.get("message"));
        } else {
            System.out.println("\n✗ Payment failed: " + confirmResponse.get("error"));
        }
    }

    private void cancelBooking(Scanner sc) throws Exception {
        System.out.print("\nEnter Booking ID to cancel: ");
        String bookingId = sc.nextLine();

        Map<String, Object> response = HttpClientUtil.delete("/customers/bookings/" + bookingId);

        if (response.get("success") != null && (Boolean) response.get("success")) {
            System.out.println("\n✓ Booking cancelled successfully!");
        } else {
            System.out.println("\n✗ Cancellation failed: " + response.get("error"));
        }
    }

    private void viewPaymentHistory(Scanner sc, String userId) throws Exception {
        Map<String, Object> response = HttpClientUtil.get("/payments/history/" + userId);

        List<Map<String, Object>> history = (List<Map<String, Object>>) response.get("history");

        if (history == null || history.isEmpty()) {
            System.out.println("\n[INFO] No payment history found.");
            return;
        }

        System.out.println("\n--- Payment History ---");
        for (Map<String, Object> payment : history) {
            System.out.println(" • TXN ID: " + payment.get("transactionId") +
                    " | Amount: ₹" + payment.get("amount") +
                    " | Status: " + payment.get("status") +
                    " | Date: " + payment.get("timestamp"));
        }
    }

    private void viewNotifications(Scanner sc, String userId) throws Exception {
        Map<String, Object> response = HttpClientUtil.get("/notifications/" + userId);

        List<Map<String, Object>> notifications = (List<Map<String, Object>>) response.get("notifications");

        if (notifications == null || notifications.isEmpty()) {
            System.out.println("\n[INFO] No notifications found.");
            return;
        }

        System.out.println("\n--- Your Notifications ---");
        for (Map<String, Object> notification : notifications) {
            System.out.println(" • [" + notification.get("timestamp") + "] " + notification.get("message"));
        }
    }

    private int getIntInput(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Invalid input. Enter a number: ");
        }
        int num = sc.nextInt();
        sc.nextLine();
        return num;
    }
}
