package com.flipfit.bean;
import java.time.LocalDateTime;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class Booking.
 *
 * @author Zeta
 * @ClassName  "Booking"
 */
public class Booking {
    private String bookingId;
    private String scheduleId;
    private String userId;
    private BookingStatus status;
    private LocalDateTime createdAt;

    /**
     * Gets the booking id.
     *
     * @return the booking id
     */
    public String getBookingId() { return bookingId; }
    
    /**
     * Sets the booking id.
     *
     * @param bookingId the new booking id
     */
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    
    /**
     * Gets the schedule id.
     *
     * @return the schedule id
     */
    public String getScheduleId() { return scheduleId; }
    
    /**
     * Sets the schedule id.
     *
     * @param scheduleId the new schedule id
     */
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }
    
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
     * Gets the status.
     *
     * @return the status
     */
    public BookingStatus getStatus() { return status; }
    
    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(BookingStatus status) { this.status = status; }
    
    /**
     * Gets the created at.
     *
     * @return the created at
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    /**
     * Sets the created at.
     *
     * @param createdAt the new created at
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}