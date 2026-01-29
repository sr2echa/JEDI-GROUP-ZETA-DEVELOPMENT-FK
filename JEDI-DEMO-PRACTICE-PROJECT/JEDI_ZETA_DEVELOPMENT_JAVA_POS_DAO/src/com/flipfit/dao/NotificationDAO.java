package com.flipfit.dao;

import com.flipfit.bean.Notification;
import java.util.List;

/**
 * The Interface NotificationDAO.
 * Data Access Object for handling notifications.
 */
public interface NotificationDAO {
    /**
     * Send notification.
     * 
     * @param notification the notification to save
     */
    void sendNotification(Notification notification);

    /**
     * Gets the notifications by user.
     * 
     * @param userId the user id
     * @return the list of notifications
     */
    List<Notification> getNotificationsByUser(String userId);
}
