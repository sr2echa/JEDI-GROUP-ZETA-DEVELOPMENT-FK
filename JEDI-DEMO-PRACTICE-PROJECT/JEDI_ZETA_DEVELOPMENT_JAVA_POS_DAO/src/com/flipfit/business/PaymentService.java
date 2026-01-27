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

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class PaymentService.
 *
 * @author Zeta
 * @ClassName  "PaymentService"
 */
public class PaymentService implements PaymentInterface {
    
    /** The payment DAO. */
    private PaymentDAO paymentDAO = new PaymentDAOImpl();

    /**
     * Instantiates a new payment service.
     */
    public PaymentService() {
    }

    /**
     * Generate transaction id.
     *
     * @param prefix the prefix
     * @return the string
     */
    private String generateTransactionId(String prefix) {
        return prefix + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    /**
     * Gets the payment method.
     *
     * @param bookingId the booking id
     * @return the payment method
     */
    public String getPaymentMethod(String bookingId) {
        List<PaymentRecord> payments = paymentDAO.getPaymentsByBookingId(bookingId);
        if (payments.isEmpty())
            return "UNKNOWN";
        return payments.get(0).getMethod();
    }

    /**
     * Process payment.
     *
     * @param bookingId the booking id
     * @param amount the amount
     * @param paymentMethod the payment method
     * @return true, if successful
     */
    @Override
    public boolean processPayment(String bookingId, double amount, String paymentMethod) {
        System.out.println("\n--- Processing Payment ---");

        com.flipfit.dao.GymCustomerDAO customerDAO = new com.flipfit.dao.impl.GymCustomerDAOImpl();
        Booking booking = customerDAO.getBookingById(bookingId);

        if (booking == null) {
            System.err.println("[ERROR] Booking not found: " + bookingId);
            return false;
        }

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

        history.setUserId(booking.getUserId());
        history.setCenterId(slot.getCenterId());

        paymentDAO.savePayment(history);
        System.out.println("[SUCCESS] Payment processed successfully! TXN: " + txnId);
        return true;
    }

    /**
     * Gets the pending amount.
     *
     * @param bookingId the booking id
     * @return the pending amount
     */
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

    /**
     * Process refund.
     *
     * @param bookingId the booking id
     * @param amount the amount
     * @return true, if successful
     */
    @Override
    public boolean processRefund(String bookingId, double amount) {
        System.out.println("[SUCCESS] Refund of ₹" + amount + " initiated for Booking: " + bookingId);
        return true;
    }

    /**
     * Checks for payment.
     *
     * @param bookingId the booking id
     * @return true, if successful
     */
    @Override
    public boolean hasPayment(String bookingId) {
        return paymentDAO.getPaymentsByBookingId(bookingId).stream()
                .anyMatch(p -> p.getStatus() == com.flipfit.bean.PaymentStatus.COMPLETED);
    }

    /**
     * Gets the paid amount.
     *
     * @param bookingId the booking id
     * @return the paid amount
     */
    @Override
    public double getPaidAmount(String bookingId) {
        return paymentDAO.getPaymentsByBookingId(bookingId).stream()
                .filter(p -> p.getStatus() == com.flipfit.bean.PaymentStatus.COMPLETED)
                .mapToDouble(PaymentRecord::getAmount)
                .sum();
    }

    /**
     * Process payment interactive.
     *
     * @param sc the sc
     * @param bookingId the booking id
     * @param amount the amount
     * @return true, if successful
     */
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

    /**
     * Gets the customer history.
     *
     * @param userId the user id
     * @return the customer history
     */
    public List<PaymentRecord> getCustomerHistory(String userId) {
        return paymentDAO.getPaymentHistory(userId);
    }

    /**
     * Display gym revenue.
     *
     * @param centerId the center id
     * @param ownerId the owner id
     */
    public void displayGymRevenue(String centerId, String ownerId) {
        List<PaymentRecord> list = paymentDAO.getCenterRevenue(centerId);
        double total = list.stream().mapToDouble(PaymentRecord::getAmount).sum();
        System.out.println("Total Revenue for " + centerId + ": ₹" + total);
    }

    /**
     * Display gym revenue.
     *
     * @param centerId the center id
     * @param ownerId the owner id
     * @param user the user
     */
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