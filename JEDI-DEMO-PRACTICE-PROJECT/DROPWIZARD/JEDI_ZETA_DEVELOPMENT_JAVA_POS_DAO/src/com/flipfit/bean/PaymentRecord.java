package com.flipfit.bean;
import java.time.LocalDateTime;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class PaymentRecord.
 *
 * @author Zeta
 * @ClassName  "PaymentRecord"
 */
public class PaymentRecord {
    private String transactionId;
    private String bookingId;
    private String userId;
    private String centerId;
    private double amount;
    private String method;
    private LocalDateTime timestamp;
    private PaymentStatus status;

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public String getUserId() { return userId; }

    /**
     * Sets the user id.
     *
     * @param userId the new user id
     */
    public void setUserId(String userId) { this.userId = userId; }

    /**
     * Gets the center id.
     *
     * @return the center id
     */
    public String getCenterId() { return centerId; }

    /**
     * Sets the center id.
     *
     * @param centerId the new center id
     */
    public void setCenterId(String centerId) { this.centerId = centerId; }

    /**
     * Gets the amount.
     *
     * @return the amount
     */
    public double getAmount() { return amount; }

    /**
     * Sets the amount.
     *
     * @param amount the new amount
     */
    public void setAmount(double amount) { this.amount = amount; }

    /**
     * Gets the transaction id.
     *
     * @return the transaction id
     */
    public String getTransactionId() { return transactionId; }

    /**
     * Sets the transaction id.
     *
     * @param transactionId the new transaction id
     */
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() { return timestamp; }

    /**
     * Sets the timestamp.
     *
     * @param timestamp the new timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    /**
     * Sets the booking id.
     *
     * @param bookingId the new booking id
     */
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    /**
     * Gets the booking id.
     *
     * @return the booking id
     */
    public String getBookingId() { return bookingId; }

    /**
     * Sets the method.
     *
     * @param method the new method
     */
    public void setMethod(String method) { this.method = method; }

    /**
     * Gets the method.
     *
     * @return the method
     */
    public String getMethod() { return method; }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public PaymentStatus getStatus() { return status; }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(PaymentStatus status) { this.status = status; }
}