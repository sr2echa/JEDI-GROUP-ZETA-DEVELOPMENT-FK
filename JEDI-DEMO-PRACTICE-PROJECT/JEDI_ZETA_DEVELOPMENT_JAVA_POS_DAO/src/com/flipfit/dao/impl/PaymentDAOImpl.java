package com.flipfit.dao.impl;

import com.flipfit.bean.PaymentRecord;
import com.flipfit.bean.PaymentStatus;
import com.flipfit.dao.PaymentDAO;
import com.flipfit.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public void savePayment(PaymentRecord payment) {
        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO Payment (transactionId, bookingId, userId, centerId, amount, method, timestamp, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, payment.getTransactionId());
            pstmt.setString(2, payment.getBookingId());
            pstmt.setString(3, payment.getUserId());
            pstmt.setString(4, payment.getCenterId());
            pstmt.setDouble(5, payment.getAmount());
            pstmt.setString(6, payment.getMethod());
            pstmt.setTimestamp(7, Timestamp.valueOf(payment.getTimestamp()));
            pstmt.setString(8, payment.getStatus().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PaymentRecord> getPaymentHistory(String userId) {
        List<PaymentRecord> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Payment WHERE userId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PaymentRecord p = new PaymentRecord();
                    p.setTransactionId(rs.getString("transactionId"));
                    p.setBookingId(rs.getString("bookingId"));
                    p.setUserId(rs.getString("userId"));
                    p.setCenterId(rs.getString("centerId"));
                    p.setAmount(rs.getDouble("amount"));
                    p.setMethod(rs.getString("method"));
                    p.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    p.setStatus(PaymentStatus.valueOf(rs.getString("status")));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<PaymentRecord> getCenterRevenue(String centerId) {
        List<PaymentRecord> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Payment WHERE centerId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, centerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PaymentRecord p = new PaymentRecord();
                    p.setTransactionId(rs.getString("transactionId"));
                    p.setBookingId(rs.getString("bookingId"));
                    p.setUserId(rs.getString("userId"));
                    p.setCenterId(rs.getString("centerId"));
                    p.setAmount(rs.getDouble("amount"));
                    p.setMethod(rs.getString("method"));
                    p.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    p.setStatus(PaymentStatus.valueOf(rs.getString("status")));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
