package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.GymCenter;
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
        // This could be fetched from DB if needed
        return "UNKNOWN";
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
        return 500.0; // Placeholder
    }

    @Override
    public boolean processRefund(String bookingId, double amount) {
        System.out.println("[SUCCESS] Refund of ₹" + amount + " initiated for Booking: " + bookingId);
        return true;
    }

    @Override
    public boolean hasPayment(String bookingId) {
        return true; // Placeholder
    }

    @Override
    public double getPaidAmount(String bookingId) {
        return 500.0; // Placeholder
    }

    public boolean processPaymentInteractive(Scanner sc, String bookingId, double amount) {
        System.out.println("Processing interactive payment for " + bookingId);
        return processPayment(bookingId, amount, "UPI");
    }

    public List<PaymentRecord> getCustomerHistory(String userId) {
        return paymentDAO.getPaymentHistory(userId);
    }

    public void displayGymRevenue(String centerId, String ownerId) {
        List<PaymentRecord> list = paymentDAO.getCenterRevenue(centerId);
        double total = list.stream().mapToDouble(PaymentRecord::getAmount).sum();
        System.out.println("Total Revenue for " + centerId + ": ₹" + total);
    }

    public void displayGymRevenue(String centerId, String ownerId, User user) {
        displayGymRevenue(centerId, ownerId);
    }
}
