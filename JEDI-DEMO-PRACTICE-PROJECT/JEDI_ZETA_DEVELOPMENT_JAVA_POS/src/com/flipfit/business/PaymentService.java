package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.PaymentRecord;
import com.flipfit.bean.Role;
import com.flipfit.bean.SlotMaster;
import com.flipfit.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PaymentService implements PaymentInterface {
    private static Map<String, Double> paymentRecords = new HashMap<>();
    private static Map<String, String> paymentMethods = new HashMap<>();
    private static List<PaymentRecord> globalPaymentHistory = new ArrayList<>();

    public PaymentService() {
        // We'll need to access CustomerService to update booking status
        // This creates a circular dependency, so we'll handle it differently
    }
    
    /**
     * Get the payment method used for a booking
     */
    public String getPaymentMethod(String bookingId) {
        return paymentMethods.get(bookingId);
    }

    @Override
    public boolean processPayment(String bookingId, double amount, String paymentMethod) {
        // Simulate payment processing
        System.out.println("\n--- Processing Payment ---");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Amount: ₹" + amount);
        System.out.println("Payment Method: " + paymentMethod);
        
        // Create payment record with PROCESSING status
        PaymentRecord history = new PaymentRecord();
        String txnId = "TXN" + System.currentTimeMillis();
        history.setTransactionId(txnId);
        history.setBookingId(bookingId);
        history.setAmount(amount);
        history.setMethod(paymentMethod);
        history.setTimestamp(java.time.LocalDateTime.now());
        history.setStatus(com.flipfit.bean.PaymentStatus.PROCESSING);
        
        // Simulate payment gateway processing
        try {
            Thread.sleep(500); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // For demo purposes, accept any payment method
        // In real scenario, this would integrate with payment gateway
        boolean paymentSuccess = true;
        
        if (paymentSuccess) {
            paymentRecords.put(bookingId, amount);
            paymentMethods.put(bookingId, paymentMethod);
            System.out.println("[SUCCESS] Payment processed successfully!");
            System.out.println("Transaction ID: " + txnId);
            
            // Update status to COMPLETED
            history.setStatus(com.flipfit.bean.PaymentStatus.COMPLETED);

            Booking b = CustomerService.getBookingById(bookingId);
            if (b != null) {
                history.setUserId(b.getUserId());
                history.setCenterId(GymOwnerService.getSlot(b.getScheduleId()).getCenterId());
            }
            globalPaymentHistory.add(history);
            return true;
        } else {
            System.out.println("[ERROR] Payment failed. Please try again.");
            history.setStatus(com.flipfit.bean.PaymentStatus.CANCELLED);
            globalPaymentHistory.add(history);
            return false;
        }
    }

    @Override
    public double getPendingAmount(String bookingId) {
        // Get booking from CustomerService
        Booking booking = getBookingById(bookingId);
        if (booking == null || booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            return -1;
        }
        
        SlotMaster slot = GymOwnerService.getSlot(booking.getScheduleId());
        if (slot == null) {
            return -1;
        }
        
        return slot.getPrice();
    }

    /**
     * Helper method to get booking by ID
     */
    private Booking getBookingById(String bookingId) {
        return CustomerService.getBookingById(bookingId);
    }

    @Override
    public boolean processRefund(String bookingId, double amount) {
        // Check if payment was made for this booking
        if (!paymentRecords.containsKey(bookingId)) {
            System.out.println("[ERROR] No payment found for booking " + bookingId + ". Cannot process refund.");
            return false;
        }

        double paidAmount = paymentRecords.get(bookingId);
        
        // Verify refund amount doesn't exceed paid amount
        if (amount > paidAmount) {
            System.out.println("[ERROR] Refund amount (₹" + amount + ") exceeds paid amount (₹" + paidAmount + ").");
            return false;
        }

        // Simulate refund processing
        System.out.println("\n--- Processing Refund ---");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Refund Amount: ₹" + amount);
        System.out.println("Original Payment: ₹" + paidAmount);
        System.out.println("Payment Method: " + paymentMethods.get(bookingId));
        
        // Simulate refund gateway processing
        try {
            Thread.sleep(500); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // For demo purposes, always succeed
        // In real scenario, this would integrate with payment gateway
        boolean refundSuccess = true;
        
        if (refundSuccess) {
            // Update the payment record status to REFUNDED
            globalPaymentHistory.stream()
                .filter(p -> p.getBookingId().equals(bookingId))
                .findFirst()
                .ifPresent(p -> p.setStatus(com.flipfit.bean.PaymentStatus.REFUNDED));
            
            // Remove payment record (or mark as refunded)
            paymentRecords.remove(bookingId);
            paymentMethods.remove(bookingId);
            System.out.println("[SUCCESS] Refund processed successfully!");
            System.out.println("Refund Transaction ID: REF" + System.currentTimeMillis());
            System.out.println("[INFO] Refund will be credited to your original payment method within 5-7 business days.");
            return true;
        } else {
            System.out.println("[ERROR] Refund failed. Please contact support.");
            return false;
        }
    }

    @Override
    public boolean hasPayment(String bookingId) {
        return paymentRecords.containsKey(bookingId);
    }

    @Override
    public double getPaidAmount(String bookingId) {
        return paymentRecords.getOrDefault(bookingId, 0.0);
    }

    /**
     * Process payment interactively
     */
    public boolean processPaymentInteractive(Scanner sc, String bookingId, double amount) {
        System.out.println("\n--- Payment Gateway ---");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Amount to Pay: ₹" + amount);
        System.out.println("\nSelect Payment Method:");
        System.out.println("1. Credit/Debit Card");
        System.out.println("2. UPI");
        System.out.println("3. Net Banking");
        System.out.println("4. Cancel Payment");
        System.out.print("Choice: ");
        
        int choice = 0;
        if (sc.hasNextInt()) {
            choice = sc.nextInt();
            sc.nextLine(); // consume newline
        } else {
            sc.nextLine();
            System.out.println("Invalid input.");
            return false;
        }
        
        String paymentMethod;
        switch (choice) {
            case 1:
                paymentMethod = "CARD";
                System.out.print("Enter Card Number (16 digits): ");
                String cardNumber = sc.nextLine();
                if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
                    System.out.println("[ERROR] Invalid card number.");
                    return false;
                }
                System.out.print("Enter CVV: ");
                String cvv = sc.nextLine();
                if (cvv.length() != 3 || !cvv.matches("\\d+")) {
                    System.out.println("[ERROR] Invalid CVV.");
                    return false;
                }
                break;
            case 2:
                paymentMethod = "UPI";
                System.out.print("Enter UPI ID: ");
                String upiId = sc.nextLine();
                if (upiId.isEmpty()) {
                    System.out.println("[ERROR] UPI ID cannot be empty.");
                    return false;
                }
                break;
            case 3:
                paymentMethod = "NET_BANKING";
                System.out.print("Enter Bank Name: ");
                String bankName = sc.nextLine();
                if (bankName.isEmpty()) {
                    System.out.println("[ERROR] Bank name cannot be empty.");
                    return false;
                }
                break;
            case 4:
                System.out.println("[INFO] Payment cancelled.");
                return false;
            default:
                System.out.println("[ERROR] Invalid selection.");
                return false;
        }
        
        return processPayment(bookingId, amount, paymentMethod);
    }
    public List<PaymentRecord> getCustomerHistory(String userId) {
    return globalPaymentHistory.stream()
            .filter(r -> r.getUserId() != null && r.getUserId().equals(userId))
            .collect(Collectors.toList());
    }

    public void displayGymRevenue(String centerId, String ownerId) {
        displayGymRevenue(centerId, ownerId, null);
    }

    /**
     * Display gym revenue for a center with role-based access control.
     * Admins can view any center's revenue, gym owners can only view their own.
     * 
     * @param centerId The ID of the center
     * @param ownerId The ID of the owner/user requesting the revenue
     * @param user The User object (if available) to check admin privileges
     */
    public void displayGymRevenue(String centerId, String ownerId, User user) {
        // Validate parameters
        if (centerId == null) {
            System.out.println("[ERROR] Center ID cannot be null.");
            return;
        }
        
        if (ownerId == null) {
            System.out.println("[ERROR] Owner ID cannot be null.");
            return;
        }
        
        // Validate center exists
        GymCenter center = GymOwnerService.getCenterById(centerId);
        if (center == null) {
            System.out.println("[ERROR] Center not found with ID: " + centerId);
            return;
        }
        
        // Check if user is admin - admins can view any center's revenue
        boolean isAdmin = user != null && user.getRole() == Role.ADMIN;
        
        // Validate ownership if not admin
        if (!isAdmin && (center.getOwnerId() == null || !Objects.equals(center.getOwnerId(), ownerId))) {
            System.out.println("[ERROR] Access denied. You are not authorized to view revenue for this center.");
            return;
        }
        
        List<PaymentRecord> gymPayments = globalPaymentHistory.stream()
                .filter(r -> r.getCenterId() != null && r.getCenterId().equals(centerId))
                .collect(Collectors.toList());

        double totalRevenue = gymPayments.stream().mapToDouble(PaymentRecord::getAmount).sum();

        System.out.println("\n--- Revenue Report for " + centerId + " ---");
        System.out.println("Total Revenue Generated: ₹" + totalRevenue);
        System.out.println("Transaction History:");
        if (gymPayments.isEmpty()) {
            System.out.println("  No transactions found.");
        } else {
            gymPayments.forEach(p -> System.out.println("  - TXN: " + p.getTransactionId() + " | Amount: ₹" + p.getAmount() + " | Status: " + p.getStatus() + " | Date: " + p.getTimestamp()));
        }
    }
}