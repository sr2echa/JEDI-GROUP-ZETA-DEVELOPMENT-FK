package com.flipfit.business;

import com.flipfit.bean.Notification;
import com.flipfit.bean.NotificationType;
import com.flipfit.dao.NotificationDAO;
import com.flipfit.dao.impl.NotificationDAOImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * The Class NotificationService.
 * Service for sending and viewing notifications.
 */
public class NotificationService {

    private NotificationDAO notificationDAO = new NotificationDAOImpl();

    /**
     * Send notification.
     * 
     * @param userId  the user id
     * @param message the message
     * @param type    the notification type
     */
    public void sendNotification(String userId, String message, NotificationType type) {
        Notification notification = new Notification();
        notification.setNotificationId("NOTIF" + UUID.randomUUID().toString().substring(0, 8));
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setType(type);
        notification.setTimestamp(LocalDateTime.now());

        notificationDAO.sendNotification(notification);
        System.out.println("[NOTIFICATION] for " + userId + ": " + message);
    }

    /**
     * Gets the notifications.
     * 
     * @param userId the user id
     * @return the list of notifications
     */
    public List<Notification> getNotifications(String userId) {
        return notificationDAO.getNotificationsByUser(userId);
    }
}
