package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.impl.GymCustomerDAOImpl;
import java.util.List;

public class CustomerService implements CustomerInterface {
    private GymCustomerDAO customerDAO = new GymCustomerDAOImpl();

    @Override
    public void bookWorkout(String userId, String slotId) {
        String bookingDate = java.time.LocalDate.now().plusDays(1).toString(); // Book for tomorrow
        System.out.println("[INFO] Booking slot " + slotId + " for User: " + userId + " on " + bookingDate);

        boolean success = customerDAO.bookSlot(userId, slotId, bookingDate);
        if (success) {
            System.out.println("[SUCCESS] Booking initiated! Please complete your payment to confirm.");
        } else {
            System.out.println("[ERROR] Booking failed. Slot might be full or not found.");
        }
    }

    @Override
    public void cancelWorkout(String bookingId) {
        customerDAO.cancelBooking(bookingId);
        System.out.println("[SUCCESS] Booking " + bookingId + " cancelled.");
    }

    @Override
    public List<Booking> getCustomerPlan(String userId) {
        List<Booking> list = customerDAO.viewMyBookings(userId);
        System.out.println("\n--- Your Fitness Plan for " + userId + " ---");
        for (Booking b : list) {
            System.out.println("Booking ID: " + b.getBookingId() + " | Slot: " + b.getScheduleId() + " | Status: "
                    + b.getStatus());
        }
        return list;
    }

    @Override
    public boolean processPaymentAndConfirm(String bookingId, double amount, String paymentMethod) {
        // Delegate to payment service and update booking status
        PaymentService paymentService = new PaymentService();
        boolean paymentSuccess = paymentService.processPayment(bookingId, amount, paymentMethod);

        if (!paymentSuccess) {
            System.err.println("[ERROR] processPaymentAndConfirm: Payment failed for booking " + bookingId);
            return false;
        }

        // Payment succeeded, update status to CONFIRMED
        customerDAO.updateBookingStatus(bookingId, BookingStatus.CONFIRMED.toString());
        System.out.println("[SUCCESS] Payment and confirmation completed for booking " + bookingId);
        return true;
    }

    @Override
    public boolean confirmBooking(String bookingId) {
        // Just update status to CONFIRMED without processing payment again
        customerDAO.updateBookingStatus(bookingId, BookingStatus.CONFIRMED.toString());
        System.out.println("[SUCCESS] Booking " + bookingId + " has been confirmed.");
        return true;
    }

    @Override
    public List<Booking> getPendingPayments(String userId) {
        // Get bookings with PENDING_PAYMENT status
        List<Booking> allBookings = customerDAO.viewMyBookings(userId);
        return allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.PENDING_PAYMENT)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean cancelPendingBooking(String bookingId) {
        customerDAO.cancelBooking(bookingId);
        return true;
    }

    public Booking getBookingById(String bookingId) {
        return customerDAO.getBookingById(bookingId);
    }

    public static void cancelAllBookingsForSlot(String slotId) {
        // Implementation
    }
}