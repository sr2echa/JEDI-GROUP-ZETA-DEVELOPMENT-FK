package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Role;
import com.flipfit.bean.SlotMaster;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.impl.GymAdminDAOImpl;
import com.flipfit.dao.impl.GymOwnerDAOImpl;

import java.time.LocalTime;
import java.util.List;

public class GymOwnerService implements GymOwnerInterface {
    private GymOwnerDAO ownerDAO = new GymOwnerDAOImpl();

    @Override
    public List<GymCenter> getAllCenters() {
        // This is typically used by customers, but available here too
        // For simplicity, returning all approved centers
        return new com.flipfit.dao.impl.GymCustomerDAOImpl().viewCenters();
    }

    @Override
    public void manageSlots(String centerId) {
        System.out.println("[SYSTEM] Managing slots for Center: " + centerId);
    }

    @Override
    public void updateSlotCapacity(String slotId, int newCapacity) {
        ownerDAO.updateSlotCapacity(slotId, newCapacity);
        System.out.println("[SYSTEM] Slot " + slotId + " capacity updated to " + newCapacity);
    }

    @Override
    public void onboardGymOwner(String username, String password, String pan, String gst, String aadhar,
            String location) {
        GymOwner newOwner = new GymOwner();
        newOwner.setUserId(username);
        newOwner.setName(username);
        newOwner.setEmail(username + "@flipfit.com");
        newOwner.setPassword(password);
        newOwner.setPanNumber(pan);
        newOwner.setGstNumber(gst);
        newOwner.setAadharNumber(aadhar);
        newOwner.setLocation(location);
        newOwner.setRole(Role.GYM_OWNER);
        newOwner.setApproved(false);

        // Register using the populated GymOwner instance
        com.flipfit.dao.GymUserDAO userDAO = new com.flipfit.dao.impl.GymUserDAOImpl();
        userDAO.registerUser(newOwner);
        System.out.println(
                "[SYSTEM] Gym Owner registration successful for " + username + ". Waiting for admin approval.");
    }

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

    @Override
    public List<GymCenter> viewMyCenters(String ownerId) {
        return ownerDAO.viewMyCenters(ownerId);
    }

    @Override
    public boolean addSlot(String centerId, LocalTime startTime, LocalTime endTime, int capacity) {
        // Simple overlap check can be added here or in DAO
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

    @Override
    public List<SlotMaster> viewSlots(String centerId) {
        return ownerDAO.viewSlots(centerId);
    }

    // These static methods are used by AdminService
    public static SlotMaster getSlot(String slotId) {
        // Use the new getSlotById method from DAO
        return new GymOwnerDAOImpl().getSlotById(slotId);
    }

    public static void updateAvailability(String slotId, int delta) {
        // Delegate to DAO layer
        GymOwnerDAO ownerDAO = new GymOwnerDAOImpl();
        ownerDAO.updateAvailableSeats(slotId, delta);
    }

    public static GymCenter getCenterById(String centerId) {
        // Use admin DAO to fetch center by ID without relying on customer-approved
        // filtering
        return new GymAdminDAOImpl().getCenterById(centerId);
    }

    public static List<GymOwner> getPendingOwners() {
        return new GymAdminDAOImpl().viewPendingGymOwners();
    }

    public static void approveOwner(String ownerId) {
        new GymAdminDAOImpl().approveGymOwner(ownerId);
    }

    public static void approveSlot(String slotId) {
        new GymAdminDAOImpl().approveSlot(slotId);
    }

    public static List<SlotMaster> getAllSlots() {
        // This is a bit inefficient but for compatibility:
        return new GymAdminDAOImpl().viewPendingSlots();
    }
}
