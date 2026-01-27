package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.SlotMaster;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.impl.GymCustomerDAOImpl;
import java.util.List;

public class CustomerService implements CustomerInterface {
    private GymCustomerDAO customerDAO = new GymCustomerDAOImpl();

    @Override
    public void bookWorkout(String userId, String slotId) {
        boolean success = customerDAO.bookSlot(userId, slotId, "2024-01-01"); // Date placeholder
        if (success) {
            System.out.println("[SUCCESS] Booking successful for User: " + userId + " Slot: " + slotId);
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
        // Implementation logic
        return true;
    }

    @Override
    public List<Booking> getPendingPayments(String userId) {
        // Returning empty list for now, integrate with DAO if needed
        return java.util.Collections.emptyList();
    }

    @Override
    public boolean cancelPendingBooking(String bookingId) {
        customerDAO.cancelBooking(bookingId);
        return true;
    }

    public static Booking getBookingById(String bookingId) {
        // Implementation for other services to use
        return null;
    }

    public static void cancelAllBookingsForSlot(String slotId) {
        // Implementation
    }
}