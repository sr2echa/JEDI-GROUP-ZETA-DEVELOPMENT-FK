package com.flipfit.bean;

import java.time.LocalDateTime;

/**
 * The Class Notification.
 * Represents a notification sent to a user.
 */
public class Notification {
    private String notificationId;
    private String userId;
    private String message;
    private NotificationType type;
    private LocalDateTime timestamp;

    /**
     * Default constructor for Notification.
     */
    public Notification() {
    }

    /**
     * Gets the notification ID.
     * 
     * @return the notification ID
     */
    public String getNotificationId() {
        return notificationId;
    }

    /**
     * Sets the notification ID.
     * 
     * @param notificationId the new notification ID
     */
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    /**
     * Gets the user ID.
     * 
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     * 
     * @param userId the new user ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the notification message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the notification message.
     * 
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the notification type.
     * 
     * @return the type
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * Sets the notification type.
     * 
     * @param type the new type
     */
    public void setType(NotificationType type) {
        this.type = type;
    }

    /**
     * Gets the timestamp.
     * 
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp.
     * 
     * @param timestamp the new timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
