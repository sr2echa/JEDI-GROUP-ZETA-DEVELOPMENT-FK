package com.flipfit.dao.impl;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class GymAdminDAOImpl.
 * Implementation of GymAdminDAO interface for administrative data operations.
 *
 * @author Zeta
 * @ClassName  "GymAdminDAOImpl"
 */
public class GymAdminDAOImpl implements GymAdminDAO {

    /**
     * Approve gym owner.
     * Approves a pending gym owner registration.
     *
     * @param ownerId the owner id to approve
     */
    @Override
    public void approveGymOwner(String ownerId) {
        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE GymOwner SET isApproved = true WHERE userId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ownerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Approve gym center.
     * Approves a pending gym center registration.
     *
     * @param centerId the center id to approve
     */
    @Override
    public void approveGymCenter(String centerId) {
        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE GymCenter SET isApproved = true WHERE centerId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, centerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Approve slot.
     * Approves a pending slot registration.
     *
     * @param slotId the slot id to approve
     */
    @Override
    public void approveSlot(String slotId) {
        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE Slot SET isApproved = true WHERE slotId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, slotId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * View pending gym owners.
     * Retrieves all gym owners awaiting admin approval.
     *
     * @return the list of pending gym owners
     */
    @Override
    public List<GymOwner> viewPendingGymOwners() {
        List<GymOwner> owners = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM User u JOIN GymOwner g ON u.userId = g.userId WHERE g.isApproved = false";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                GymOwner owner = new GymOwner();
                owner.setUserId(rs.getString("userId"));
                owner.setName(rs.getString("name"));
                owner.setEmail(rs.getString("email"));
                owner.setPanNumber(rs.getString("panNumber"));
                owner.setApproved(false);
                owners.add(owner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owners;
    }

    /**
     * View pending gym centers.
     * Retrieves all gym centers awaiting admin approval.
     *
     * @return the list of pending gym centers
     */
    @Override
    public List<GymCenter> viewPendingGymCenters() {
        List<GymCenter> centers = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM GymCenter WHERE isApproved = false";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                GymCenter center = new GymCenter();
                center.setCenterId(rs.getString("centerId"));
                center.setName(rs.getString("name"));
                center.setCity(rs.getString("city"));
                center.setAddress(rs.getString("address"));
                center.setOwnerId(rs.getString("ownerId"));
                center.setApproved(false);
                centers.add(center);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return centers;
    }

    /**
     * View pending slots.
     * Retrieves all slots awaiting admin approval.
     *
     * @return the list of pending slots
     */
    @Override
    public List<SlotMaster> viewPendingSlots() {
        List<SlotMaster> slots = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Slot WHERE isApproved = false";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                SlotMaster slot = new SlotMaster();
                slot.setSlotId(rs.getString("slotId"));
                slot.setCenterId(rs.getString("centerId"));
                slot.setStartTime(rs.getTime("startTime").toLocalTime());
                slot.setEndTime(rs.getTime("endTime").toLocalTime());
                slot.setCapacity(rs.getInt("capacity"));
                slot.setAvailableSeats(rs.getInt("availableSeats"));
                slot.setPrice(rs.getDouble("price"));
                slot.setApproved(false);
                slots.add(slot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }
    
    /**
     * Gets the center by id.
     * Retrieves gym center details by center ID.
     *
     * @param centerId the center id
     * @return the gym center object, or null if not found
     */
    @Override
    public GymCenter getCenterById(String centerId) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM GymCenter WHERE centerId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, centerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    GymCenter center = new GymCenter();
                    center.setCenterId(rs.getString("centerId"));
                    center.setName(rs.getString("name"));
                    center.setCity(rs.getString("city"));
                    center.setAddress(rs.getString("address"));
                    center.setOwnerId(rs.getString("ownerId"));
                    center.setApproved(rs.getBoolean("isApproved"));
                    return center;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
