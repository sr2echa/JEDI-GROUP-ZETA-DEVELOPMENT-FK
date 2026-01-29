package com.flipfit.dao.impl;

import com.flipfit.bean.Notification;
import com.flipfit.bean.NotificationType;
import com.flipfit.dao.NotificationDAO;
import com.flipfit.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of NotificationDAO.
 */
public class NotificationDAOImpl implements NotificationDAO {

    @Override
    public void sendNotification(Notification notification) {
        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO Notification (notificationId, userId, message, type, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, notification.getNotificationId());
            pstmt.setString(2, notification.getUserId());
            pstmt.setString(3, notification.getMessage());
            pstmt.setString(4, notification.getType().toString());
            pstmt.setTimestamp(5, Timestamp.valueOf(notification.getTimestamp()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Notification> getNotificationsByUser(String userId) {
        List<Notification> notifications = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Notification WHERE userId = ? ORDER BY timestamp DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Notification n = new Notification();
                    n.setNotificationId(rs.getString("notificationId"));
                    n.setUserId(rs.getString("userId"));
                    n.setMessage(rs.getString("message"));
                    n.setType(NotificationType.valueOf(rs.getString("type")));
                    n.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    notifications.add(n);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
}
