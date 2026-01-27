package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.PaymentRecord;
import com.flipfit.bean.Role;
import com.flipfit.bean.SlotMaster;
import com.flipfit.bean.User;
import com.flipfit.dao.PaymentDAO;
import com.flipfit.dao.impl.PaymentDAOImpl;
import com.flipfit.exception.*;
import com.flipfit.utils.InputValidator;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Service class for payment-related operations including processing payments, refunds, and payment history.
 * This service handles input validation and exception handling for payment operations.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class PaymentService implements PaymentInterface {
    private PaymentDAO paymentDAO = new PaymentDAOImpl();

    /**
     * Default constructor.
     */
    public PaymentService() {
    }

    /**
     * Generates a unique transaction ID.
     * 
     * @param prefix the prefix for the transaction ID
     * @return a unique transaction ID string
     */
    private String generateTransactionId(String prefix) {
        return prefix + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    /**
     * Retrieves the payment method for a booking.
     * 
     * @param bookingId the ID of the booking
     * @return the payment method, or "UNKNOWN" if no payment found
     * @throws ValidationException if bookingId is invalid
     * @throws DatabaseException if database operation fails
     */
    public String getPaymentMethod(String bookingId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(bookingId, "Booking ID");
            List<PaymentRecord> payments = paymentDAO.getPaymentsByBookingId(bookingId);
            if (payments.isEmpty())
                return "UNKNOWN";
            return payments.get(0).getMethod();
        } catch (ValidationException e) {
            throw new ValidationException("Get payment method validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve payment method: " + e.getMessage(), e);
        }
    }

    /**
     * Processes a payment for a booking.
     * 
     * @param bookingId the ID of the booking
     * @param amount the payment amount
     * @param paymentMethod the payment method used
     * @return true if payment processed successfully
     * @throws ValidationException if input validation fails
     * @throws PaymentException if payment processing fails
     * @throws BookingException if booking not found
     * @throws DatabaseException if database operation fails
     */
    @Override
    public boolean processPayment(String bookingId, double amount, String paymentMethod) 
            throws ValidationException, PaymentException, BookingException, DatabaseException {
        // Validate inputs
        try {
            InputValidator.validateId(bookingId, "Booking ID");
            InputValidator.validateAmount(amount);
            InputValidator.validateNotNullOrEmpty(paymentMethod, "Payment method");
        } catch (ValidationException e) {
            throw new ValidationException("Process payment validation failed: " + e.getMessage(), e);
        }

        System.out.println("\n--- Processing Payment ---");

        try {
            // Fetch booking details to get userId and centerId
            com.flipfit.dao.GymCustomerDAO customerDAO = new com.flipfit.dao.impl.GymCustomerDAOImpl();
            Booking booking = customerDAO.getBookingById(bookingId);

            if (booking == null) {
                throw new BookingException("Booking not found: " + bookingId);
            }

            // Get slot details to find centerId
            com.flipfit.dao.GymOwnerDAO ownerDAO = new com.flipfit.dao.impl.GymOwnerDAOImpl();
            SlotMaster slot = ownerDAO.getSlotById(booking.getScheduleId());

            if (slot == null) {
                throw new BookingException("Slot not found for booking: " + bookingId);
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
        } catch (BookingException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to process payment: " + e.getMessage(), e);
        }
    }

    /**
     * Calculates the pending payment amount for a booking.
     * 
     * @param bookingId the ID of the booking
     * @return the pending amount, or 0.0 if booking or slot not found
     * @throws ValidationException if bookingId is invalid
     * @throws DatabaseException if database operation fails
     */
    @Override
    public double getPendingAmount(String bookingId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(bookingId, "Booking ID");
            com.flipfit.dao.GymCustomerDAO customerDAO = new com.flipfit.dao.impl.GymCustomerDAOImpl();
            Booking booking = customerDAO.getBookingById(bookingId);
            if (booking == null)
                return 0.0;

            com.flipfit.dao.GymOwnerDAO ownerDAO = new com.flipfit.dao.impl.GymOwnerDAOImpl();
            SlotMaster slot = ownerDAO.getSlotById(booking.getScheduleId());
            if (slot == null)
                return 0.0;

            return slot.getPrice() - getPaidAmount(bookingId);
        } catch (ValidationException e) {
            throw new ValidationException("Get pending amount validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to calculate pending amount: " + e.getMessage(), e);
        }
    }

    /**
     * Processes a refund for a booking.
     * 
     * @param bookingId the ID of the booking
     * @param amount the refund amount
     * @return true if refund initiated successfully
     * @throws ValidationException if input validation fails
     * @throws PaymentException if refund processing fails
     * @throws DatabaseException if database operation fails
     */
    @Override
    public boolean processRefund(String bookingId, double amount) 
            throws ValidationException, PaymentException, DatabaseException {
        try {
            InputValidator.validateId(bookingId, "Booking ID");
            InputValidator.validateAmount(amount);
            System.out.println("[SUCCESS] Refund of ₹" + amount + " initiated for Booking: " + bookingId);
            return true;
        } catch (ValidationException e) {
            throw new ValidationException("Process refund validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to process refund: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if a booking has a completed payment.
     * 
     * @param bookingId the ID of the booking
     * @return true if payment exists and is completed
     * @throws ValidationException if bookingId is invalid
     * @throws DatabaseException if database operation fails
     */
    @Override
    public boolean hasPayment(String bookingId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(bookingId, "Booking ID");
            return paymentDAO.getPaymentsByBookingId(bookingId).stream()
                    .anyMatch(p -> p.getStatus() == com.flipfit.bean.PaymentStatus.COMPLETED);
        } catch (ValidationException e) {
            throw new ValidationException("Has payment validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to check payment status: " + e.getMessage(), e);
        }
    }

    /**
     * Calculates the total paid amount for a booking.
     * 
     * @param bookingId the ID of the booking
     * @return the total paid amount
     * @throws ValidationException if bookingId is invalid
     * @throws DatabaseException if database operation fails
     */
    @Override
    public double getPaidAmount(String bookingId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(bookingId, "Booking ID");
            return paymentDAO.getPaymentsByBookingId(bookingId).stream()
                    .filter(p -> p.getStatus() == com.flipfit.bean.PaymentStatus.COMPLETED)
                    .mapToDouble(PaymentRecord::getAmount)
                    .sum();
        } catch (ValidationException e) {
            throw new ValidationException("Get paid amount validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to calculate paid amount: " + e.getMessage(), e);
        }
    }

    /**
     * Processes payment interactively with user input.
     * 
     * @param sc the Scanner for user input
     * @param bookingId the ID of the booking
     * @param amount the payment amount
     * @return true if payment processed successfully
     * @throws ValidationException if input validation fails
     * @throws PaymentException if payment processing fails
     * @throws BookingException if booking not found
     * @throws DatabaseException if database operation fails
     */
    public boolean processPaymentInteractive(Scanner sc, String bookingId, double amount) 
            throws ValidationException, PaymentException, BookingException, DatabaseException {
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
     * Retrieves payment history for a customer.
     * 
     * @param userId the ID of the customer
     * @return a list of payment records
     * @throws ValidationException if userId is invalid
     * @throws DatabaseException if database operation fails
     */
    public List<PaymentRecord> getCustomerHistory(String userId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(userId, "User ID");
            return paymentDAO.getPaymentHistory(userId);
        } catch (ValidationException e) {
            throw new ValidationException("Get customer history validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve customer history: " + e.getMessage(), e);
        }
    }

    /**
     * Displays revenue for a gym center (compatibility overload).
     * 
     * @param centerId the ID of the center
     * @param ownerId the ID of the owner
     * @throws ValidationException if input validation fails
     * @throws DatabaseException if database operation fails
     */
    public void displayGymRevenue(String centerId, String ownerId) 
            throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(centerId, "Center ID");
            InputValidator.validateId(ownerId, "Owner ID");
            // Compatibility overload
            List<PaymentRecord> list = paymentDAO.getCenterRevenue(centerId);
            double total = list.stream().mapToDouble(PaymentRecord::getAmount).sum();
            System.out.println("Total Revenue for " + centerId + ": ₹" + total);
        } catch (ValidationException e) {
            throw new ValidationException("Display gym revenue validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to display gym revenue: " + e.getMessage(), e);
        }
    }

    /**
     * Displays revenue for a gym center with authorization check.
     * 
     * @param centerId the ID of the center
     * @param ownerId the ID of the owner
     * @param user the user requesting the revenue information
     * @throws ValidationException if input validation fails
     * @throws AuthorizationException if user is not authorized
     * @throws DatabaseException if database operation fails
     */
    public void displayGymRevenue(String centerId, String ownerId, User user) 
            throws ValidationException, AuthorizationException, DatabaseException {
        try {
            InputValidator.validateId(centerId, "Center ID");
            InputValidator.validateId(ownerId, "Owner ID");
            
            if (user == null) {
                throw new AuthorizationException("Unauthorized: User context missing.");
            }

            if (user.getRole() == Role.ADMIN || (user.getRole() == Role.GYM_OWNER && user.getUserId().equals(ownerId))) {
                List<PaymentRecord> list = paymentDAO.getCenterRevenue(centerId);
                double total = list.stream().mapToDouble(PaymentRecord::getAmount).sum();
                System.out.println("\n--- Revenue Details for " + centerId + " ---");
                list.forEach(p -> System.out.println("  - TXN: " + p.getTransactionId() + " | Amount: ₹" + p.getAmount()
                        + " | Status: " + p.getStatus()));
                System.out.println("Total Revenue: ₹" + total);
            } else {
                throw new AuthorizationException("Access Denied: You are not authorized to view this revenue.");
            }
        } catch (ValidationException | AuthorizationException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to display gym revenue: " + e.getMessage(), e);
        }
    }
}
