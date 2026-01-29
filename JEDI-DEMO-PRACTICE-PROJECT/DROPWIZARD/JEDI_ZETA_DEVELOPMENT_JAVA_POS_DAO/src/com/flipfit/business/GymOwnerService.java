package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Role;
import com.flipfit.bean.SlotMaster;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.impl.GymAdminDAOImpl;
import com.flipfit.dao.impl.GymOwnerDAOImpl;
import com.flipfit.utils.PasswordHashUtil;

import java.time.LocalTime;
import java.util.List;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class GymOwnerService.
 *
 * @author Zeta
 * @ClassName "GymOwnerService"
 */
public class GymOwnerService implements GymOwnerInterface {

    /** The owner DAO. */
    private GymOwnerDAO ownerDAO = new GymOwnerDAOImpl();

    /**
     * Gets the all centers.
     *
     * @return the all centers
     */
    @Override
    public List<GymCenter> getAllCenters() {
        return new com.flipfit.dao.impl.GymCustomerDAOImpl().viewCenters();
    }

    /**
     * Manage slots.
     *
     * @param centerId the center id
     */
    @Override
    public void manageSlots(String centerId) {
        System.out.println("[SYSTEM] Managing slots for Center: " + centerId);
    }

    /**
     * Update slot capacity.
     *
     * @param slotId      the slot id
     * @param newCapacity the new capacity
     */
    @Override
    public void updateSlotCapacity(String slotId, int newCapacity) {
        ownerDAO.updateSlotCapacity(slotId, newCapacity);
        System.out.println("[SYSTEM] Slot " + slotId + " capacity updated to " + newCapacity);
    }

    /**
     * Onboard gym owner.
     *
     * @param username the username
     * @param password the password
     * @param pan      the pan
     * @param gst      the gst
     * @param aadhar   the aadhar
     * @param location the location
     */
    @Override
    public void onboardGymOwner(String username, String password, String pan, String gst, String aadhar,
            String location) {
        GymOwner newOwner = new GymOwner();
        newOwner.setUserId(username);
        newOwner.setName(username);
        newOwner.setEmail(username + "@flipfit.com");
        newOwner.setPassword(PasswordHashUtil.hashPassword(password, username));
        newOwner.setPanNumber(pan);
        newOwner.setGstNumber(gst);
        newOwner.setAadharNumber(aadhar);
        newOwner.setLocation(location);
        newOwner.setRole(Role.GYM_OWNER);
        newOwner.setApproved(false);

        com.flipfit.dao.GymUserDAO userDAO = new com.flipfit.dao.impl.GymUserDAOImpl();
        userDAO.registerUser(newOwner);
        System.out.println(
                "[SYSTEM] Gym Owner registration successful for " + username + ". Waiting for admin approval.");
    }

    /**
     * Adds the gym center.
     *
     * @param ownerId    the owner id
     * @param centerName the center name
     * @param location   the location
     */
    @Override
    public void addGymCenter(String ownerId, String centerName, String location) {
        GymCenter center = new GymCenter();
        center.setCenterId("CENT" + System.currentTimeMillis() % 10000);
        center.setName(centerName);
        center.setCity(location);
        center.setAddress(location);
        center.setOwnerId(ownerId);
        center.setApproved(false);
        ownerDAO.addGymCenter(center);
        System.out.println("[SYSTEM] Center added successfully. Pending admin approval.");
    }

    /**
     * View my centers.
     *
     * @param ownerId the owner id
     * @return the list
     */
    @Override
    public List<GymCenter> viewMyCenters(String ownerId) {
        return ownerDAO.viewMyCenters(ownerId);
    }

    /**
     * Adds the slot.
     *
     * @param centerId  the center id
     * @param startTime the start time
     * @param endTime   the end time
     * @param capacity  the capacity
     * @return true, if successful
     */
    @Override
    public boolean addSlot(String centerId, LocalTime startTime, LocalTime endTime, int capacity) {
        SlotMaster newSlot = new SlotMaster();
        newSlot.setSlotId("SLOT" + System.currentTimeMillis() % 10000);
        newSlot.setCenterId(centerId);
        newSlot.setStartTime(startTime);
        newSlot.setEndTime(endTime);
        newSlot.setCapacity(capacity);
        newSlot.setAvailableSeats(capacity);
        newSlot.setApproved(false);
        newSlot.setPrice(500.0);

        return ownerDAO.addSlot(newSlot);
    }

    /**
     * View slots.
     *
     * @param centerId the center id
     * @return the list
     */
    @Override
    public List<SlotMaster> viewSlots(String centerId) {
        return ownerDAO.viewSlots(centerId);
    }

    /**
     * Gets the slot.
     *
     * @param slotId the slot id
     * @return the slot
     */
    public static SlotMaster getSlot(String slotId) {
        return new GymOwnerDAOImpl().getSlotById(slotId);
    }

    /**
     * Update availability.
     *
     * @param slotId the slot id
     * @param delta  the delta
     */
    public static void updateAvailability(String slotId, int delta) {
        GymOwnerDAO ownerDAO = new GymOwnerDAOImpl();
        ownerDAO.updateAvailableSeats(slotId, delta);
    }

    /**
     * Gets the center by id.
     *
     * @param centerId the center id
     * @return the center
     */
    public static GymCenter getCenterById(String centerId) {
        return new GymAdminDAOImpl().getCenterById(centerId);
    }

    /**
     * Gets the pending owners.
     *
     * @return the list
     */
    public static List<GymOwner> getPendingOwners() {
        return new GymAdminDAOImpl().viewPendingGymOwners();
    }

    /**
     * Approve owner.
     *
     * @param ownerId the owner id
     */
    public static void approveOwner(String ownerId) {
        new GymAdminDAOImpl().approveGymOwner(ownerId);
    }

    /**
     * Approve slot.
     *
     * @param slotId the slot id
     */
    public static void approveSlot(String slotId) {
        new GymAdminDAOImpl().approveSlot(slotId);
    }

    /**
     * Gets the all slots.
     *
     * @return the list
     */
    public static List<SlotMaster> getAllSlots() {
        return new GymAdminDAOImpl().viewPendingSlots();
    }
}