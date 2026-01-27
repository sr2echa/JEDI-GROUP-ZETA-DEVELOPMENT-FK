package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.PaymentRecord;
import com.flipfit.bean.Role;
import com.flipfit.bean.SlotMaster;
import com.flipfit.bean.User;
import com.flipfit.dao.PaymentDAO;
import com.flipfit.dao.impl.PaymentDAOImpl;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class PaymentService implements PaymentInterface {
    private PaymentDAO paymentDAO = new PaymentDAOImpl();

    public PaymentService() {
    }

    private String generateTransactionId(String prefix) {
        return prefix + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    public String getPaymentMethod(String bookingId) {
        List<PaymentRecord> payments = paymentDAO.getPaymentsByBookingId(bookingId);
        if (payments.isEmpty())
            return "UNKNOWN";
        return payments.get(0).getMethod();
    }

    @Override
    public boolean processPayment(String bookingId, double amount, String paymentMethod) {
        System.out.println("\n--- Processing Payment ---");

        // Fetch booking details to get userId and centerId
        com.flipfit.dao.GymCustomerDAO customerDAO = new com.flipfit.dao.impl.GymCustomerDAOImpl();
        Booking booking = customerDAO.getBookingById(bookingId);

        if (booking == null) {
            System.err.println("[ERROR] Booking not found: " + bookingId);
            return false;
        }

        // Get slot details to find centerId
        com.flipfit.dao.GymOwnerDAO ownerDAO = new com.flipfit.dao.impl.GymOwnerDAOImpl();
        SlotMaster slot = ownerDAO.getSlotById(booking.getScheduleId());

        if (slot == null) {
            System.err.println("[ERROR] Slot not found for booking: " + bookingId);
            return false;
        }

        PaymentRecord history = new PaymentRecord();
        String txnId = generateTransactionId("TXN");
        history.setTransactionId(txnId);
        history.setBookingId(bookingId);
        history.setAmount(amount);
        history.setMethod(paymentMethod);
        history.setTimestamp(java.time.LocalDateTime.now());
        history.setStatus(com.flipfit.bean.PaymentStatus.COMPLETED);

        // Set actual userId and centerId from booking
        history.setUserId(booking.getUserId());
        history.setCenterId(slot.getCenterId());

        paymentDAO.savePayment(history);
        System.out.println("[SUCCESS] Payment processed successfully! TXN: " + txnId);
        return true;
    }

    @Override
    public double getPendingAmount(String bookingId) {
        com.flipfit.dao.GymCustomerDAO customerDAO = new com.flipfit.dao.impl.GymCustomerDAOImpl();
        Booking booking = customerDAO.getBookingById(bookingId);
        if (booking == null)
            return 0.0;

        com.flipfit.dao.GymOwnerDAO ownerDAO = new com.flipfit.dao.impl.GymOwnerDAOImpl();
        SlotMaster slot = ownerDAO.getSlotById(booking.getScheduleId());
        if (slot == null)
            return 0.0;

        return slot.getPrice() - getPaidAmount(bookingId);
    }

    @Override
    public boolean processRefund(String bookingId, double amount) {
        System.out.println("[SUCCESS] Refund of ₹" + amount + " initiated for Booking: " + bookingId);
        return true;
    }

    @Override
    public boolean hasPayment(String bookingId) {
        return paymentDAO.getPaymentsByBookingId(bookingId).stream()
                .anyMatch(p -> p.getStatus() == com.flipfit.bean.PaymentStatus.COMPLETED);
    }

    @Override
    public double getPaidAmount(String bookingId) {
        return paymentDAO.getPaymentsByBookingId(bookingId).stream()
                .filter(p -> p.getStatus() == com.flipfit.bean.PaymentStatus.COMPLETED)
                .mapToDouble(PaymentRecord::getAmount)
                .sum();
    }

    public boolean processPaymentInteractive(Scanner sc, String bookingId, double amount) {
        System.out.println("\n--- Interactive Payment ---");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Amount to pay: ₹" + amount);

        String paymentMethod = null;
        while (paymentMethod == null) {
            System.out.println("\nSelect payment method:");
            System.out.println("1. UPI");
            System.out.println("2. Card");
            System.out.println("3. Net Banking");
            System.out.println("4. Wallet");
            System.out.println("5. Cash");
            System.out.print("Enter choice (1-5): ");
            String choice = sc.next();

            switch (choice) {
                case "1":
                    paymentMethod = "UPI";
                    break;
                case "2":
                    paymentMethod = "CARD";
                    break;
                case "3":
                    paymentMethod = "NET_BANKING";
                    break;
                case "4":
                    paymentMethod = "WALLET";
                    break;
                case "5":
                    paymentMethod = "CASH";
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice.");
                    break;
            }
        }
        return processPayment(bookingId, amount, paymentMethod);
    }

    public List<PaymentRecord> getCustomerHistory(String userId) {
        return paymentDAO.getPaymentHistory(userId);
    }

    public void displayGymRevenue(String centerId, String ownerId) {
        // Compatibility overload
        List<PaymentRecord> list = paymentDAO.getCenterRevenue(centerId);
        double total = list.stream().mapToDouble(PaymentRecord::getAmount).sum();
        System.out.println("Total Revenue for " + centerId + ": ₹" + total);
    }

    public void displayGymRevenue(String centerId, String ownerId, User user) {
        if (user == null) {
            System.out.println("[ERROR] Unauthorized: User context missing.");
            return;
        }

        if (user.getRole() == Role.ADMIN || (user.getRole() == Role.GYM_OWNER && user.getUserId().equals(ownerId))) {
            List<PaymentRecord> list = paymentDAO.getCenterRevenue(centerId);
            double total = list.stream().mapToDouble(PaymentRecord::getAmount).sum();
            System.out.println("\n--- Revenue Details for " + centerId + " ---");
            list.forEach(p -> System.out.println("  - TXN: " + p.getTransactionId() + " | Amount: ₹" + p.getAmount()
                    + " | Status: " + p.getStatus()));
            System.out.println("Total Revenue: ₹" + total);
        } else {
            System.out.println("[ERROR] Access Denied: You are not authorized to view this revenue.");
        }
    }
}
