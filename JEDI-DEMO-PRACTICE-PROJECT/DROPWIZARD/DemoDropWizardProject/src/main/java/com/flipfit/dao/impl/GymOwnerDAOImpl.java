package com.flipfit.dao.impl;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.SlotMaster;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class GymOwnerDAOImpl.
 * Implementation of GymOwnerDAO interface for gym owner data operations.
 *
 * @author Zeta
 * @ClassName  "GymOwnerDAOImpl"
 */
public class GymOwnerDAOImpl implements GymOwnerDAO {

    /**
     * Adds the gym center.
     * Inserts a new gym center into the database.
     *
     * @param center the gym center to add
     */
    @Override
    public void addGymCenter(GymCenter center) {
        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO GymCenter (centerId, name, city, address, ownerId, isApproved) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, center.getCenterId());
            pstmt.setString(2, center.getName());
            pstmt.setString(3, center.getCity());
            pstmt.setString(4, center.getAddress());
            pstmt.setString(5, center.getOwnerId());
            pstmt.setBoolean(6, center.isApproved());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * View my centers.
     * Retrieves all gym centers owned by a specific gym owner.
     *
     * @param ownerId the owner id
     * @return the list of gym centers owned by the owner
     */
    @Override
    public List<GymCenter> viewMyCenters(String ownerId) {
        List<GymCenter> centers = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM GymCenter WHERE ownerId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ownerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GymCenter center = new GymCenter();
                    center.setCenterId(rs.getString("centerId"));
                    center.setName(rs.getString("name"));
                    center.setCity(rs.getString("city"));
                    center.setAddress(rs.getString("address"));
                    center.setOwnerId(rs.getString("ownerId"));
                    center.setApproved(rs.getBoolean("isApproved"));
                    centers.add(center);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return centers;
    }

    /**
     * Adds the slot.
     * Inserts a new slot into the database for a gym center.
     *
     * @param slot the slot to add
     * @return true, if successful
     */
    @Override
    public boolean addSlot(SlotMaster slot) {
        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO Slot (slotId, centerId, startTime, endTime, capacity, availableSeats, price, isApproved) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, slot.getSlotId());
            pstmt.setString(2, slot.getCenterId());
            pstmt.setTime(3, Time.valueOf(slot.getStartTime()));
            pstmt.setTime(4, Time.valueOf(slot.getEndTime()));
            pstmt.setInt(5, slot.getCapacity());
            pstmt.setInt(6, slot.getAvailableSeats());
            pstmt.setDouble(7, slot.getPrice());
            pstmt.setBoolean(8, slot.isApproved());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * View slots.
     * Retrieves all slots for a specific gym center.
     *
     * @param centerId the center id
     * @return the list of slots for the center
     */
    @Override
    public List<SlotMaster> viewSlots(String centerId) {
        List<SlotMaster> slots = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Slot WHERE centerId = ?";
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
                    slot.setApproved(rs.getBoolean("isApproved"));
                    slots.add(slot);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    /**
     * Update slot capacity.
     * Updates the capacity of a slot and adjusts available seats accordingly.
     *
     * @param slotId the slot id
     * @param newCapacity the new capacity
     */
    @Override
    public void updateSlotCapacity(String slotId, int newCapacity) {
        Connection conn = DBConnection.getConnection();
        // availableSeats = availableSeats + (newCapacity - oldCapacity)
        String sql = "UPDATE Slot SET availableSeats = availableSeats + (? - capacity), capacity = ? WHERE slotId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newCapacity);
            pstmt.setInt(2, newCapacity);
            pstmt.setString(3, slotId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update slot capacity", e);
        }
    }

    /**
     * Gets the slot by id.
     * Retrieves slot details by slot ID.
     *
     * @param slotId the slot id
     * @return the slot object, or null if not found
     */
    @Override
    public SlotMaster getSlotById(String slotId) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Slot WHERE slotId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, slotId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    SlotMaster slot = new SlotMaster();
                    slot.setSlotId(rs.getString("slotId"));
                    slot.setCenterId(rs.getString("centerId"));
                    slot.setStartTime(rs.getTime("startTime").toLocalTime());
                    slot.setEndTime(rs.getTime("endTime").toLocalTime());
                    slot.setCapacity(rs.getInt("capacity"));
                    slot.setAvailableSeats(rs.getInt("availableSeats"));
                    slot.setPrice(rs.getDouble("price"));
                    slot.setApproved(rs.getBoolean("isApproved"));
                    return slot;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update available seats.
     * Increments or decrements the available seats for a slot.
     *
     * @param slotId the slot id
     * @param delta the change in available seats (positive to add, negative to subtract)
     */
    @Override
    public void updateAvailableSeats(String slotId, int delta) {
        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE Slot SET availableSeats = availableSeats + ? WHERE slotId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, delta);
            pstmt.setString(2, slotId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
