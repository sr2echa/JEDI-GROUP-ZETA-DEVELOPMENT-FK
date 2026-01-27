package com.flipfit.dao.impl;

import com.flipfit.bean.Booking;
import com.flipfit.bean.BookingStatus;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.SlotMaster;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GymCustomerDAOImpl implements GymCustomerDAO {

    @Override
    public List<GymCenter> viewCenters() {
        List<GymCenter> centers = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM GymCenter WHERE isApproved = true";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                GymCenter center = new GymCenter();
                center.setCenterId(rs.getString("centerId"));
                center.setName(rs.getString("name"));
                center.setCity(rs.getString("city"));
                center.setAddress(rs.getString("address"));
                center.setOwnerId(rs.getString("ownerId"));
                center.setApproved(true);
                centers.add(center);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return centers;
    }

    @Override
    public List<SlotMaster> viewSlots(String centerId) {
        List<SlotMaster> slots = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Slot WHERE centerId = ? AND isApproved = true";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, centerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SlotMaster slot = new SlotMaster();
                    slot.setSlotId(rs.getString("slotId"));
                    slot.setCenterId(rs.getString("centerId"));
                    slot.setStartTime(rs.getTime("startTime").toLocalTime());
                    slot.setEndTime(rs.getTime("endTime").toLocalTime());
                    slot.setCapacity(rs.getInt("capacity"));
                    slot.setAvailableSeats(rs.getInt("availableSeats"));
                    slot.setPrice(rs.getDouble("price"));
                    slot.setApproved(true);
                    slots.add(slot);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    @Override
    public boolean bookSlot(String userId, String slotId, String date) {
        Connection conn = DBConnection.getConnection();
        String checkSql = "SELECT availableSeats FROM Slot WHERE slotId = ?";
        String updateSql = "UPDATE Slot SET availableSeats = availableSeats - 1 WHERE slotId = ? AND availableSeats > 0";
        String insertSql = "INSERT INTO Booking (bookingId, slotId, userId, status, createdAt) VALUES (?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, slotId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt("availableSeats") > 0) {
                        // Update seats
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, slotId);
                            int updated = updateStmt.executeUpdate();

                            if (updated > 0) {
                                // Create booking
                                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                                    String bookingId = "BKS" + UUID.randomUUID().toString().substring(0, 8);
                                    insertStmt.setString(1, bookingId);
                                    insertStmt.setString(2, slotId);
                                    insertStmt.setString(3, userId);
                                    insertStmt.setString(4, BookingStatus.CONFIRMED.toString());
                                    insertStmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                                    insertStmt.executeUpdate();

                                    conn.commit();
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            conn.rollback();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<Booking> viewMyBookings(String userId) {
        List<Booking> bookings = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Booking WHERE userId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getString("bookingId"));
                    booking.setScheduleId(rs.getString("slotId")); // Mapping slotId to scheduleId as per bean
                    booking.setUserId(rs.getString("userId"));
                    booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
                    booking.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public void cancelBooking(String bookingId) {
        Connection conn = DBConnection.getConnection();
        String getBookingSql = "SELECT slotId, status FROM Booking WHERE bookingId = ?";
        String updateSlotSql = "UPDATE Slot SET availableSeats = availableSeats + 1 WHERE slotId = ?";
        String updateBookingSql = "UPDATE Booking SET status = ? WHERE bookingId = ?";

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement getBookingStmt = conn.prepareStatement(getBookingSql)) {
                getBookingStmt.setString(1, bookingId);
                try (ResultSet rs = getBookingStmt.executeQuery()) {
                    if (rs.next()) {
                        String slotId = rs.getString("slotId");
                        String currentStatus = rs.getString("status");
                        
                        // Only cancel if not already cancelled
                        if (!BookingStatus.CANCELLED.toString().equals(currentStatus)) {
                            // Update Slot
                            try (PreparedStatement updateSlotStmt = conn.prepareStatement(updateSlotSql)) {
                                updateSlotStmt.setString(1, slotId);
                                updateSlotStmt.executeUpdate();
                            }

                            // Update Booking
                            try (PreparedStatement updateBookingStmt = conn.prepareStatement(updateBookingSql)) {
                                updateBookingStmt.setString(1, BookingStatus.CANCELLED.toString());
                                updateBookingStmt.setString(2, bookingId);
                                updateBookingStmt.executeUpdate();
                            }

                            conn.commit();
                        } else {
                            conn.rollback();
                        }
                    } else {
                        conn.rollback();
                    }
                }
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
