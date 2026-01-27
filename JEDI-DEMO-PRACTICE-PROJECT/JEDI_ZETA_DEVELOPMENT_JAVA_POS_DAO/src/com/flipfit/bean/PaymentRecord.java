package com.flipfit.bean;
import java.time.LocalDateTime;

public class PaymentRecord {
    private String transactionId;
    private String bookingId;
    private String userId;
    private String centerId;
    private double amount;
    private String method;
    private LocalDateTime timestamp;
    private PaymentStatus status;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCenterId() { return centerId; }
    public void setCenterId(String centerId) { this.centerId = centerId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // ADDED THESE TWO METHODS:
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getBookingId() { return bookingId; }
    public void setMethod(String method) { this.method = method; }
    public String getMethod() { return method; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
}