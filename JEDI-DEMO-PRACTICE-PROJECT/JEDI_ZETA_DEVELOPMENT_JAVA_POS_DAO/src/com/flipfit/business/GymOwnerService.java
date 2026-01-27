package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Role;
import com.flipfit.bean.SlotMaster;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.impl.GymAdminDAOImpl;
import com.flipfit.dao.impl.GymOwnerDAOImpl;
import com.flipfit.exception.*;
import com.flipfit.utils.InputValidator;
import com.flipfit.utils.PasswordUtil;

import java.time.LocalTime;
import java.util.List;

/**
 * Service class for gym owner-related operations including center management, slot management, and owner registration.
 * This service handles input validation, password hashing, and exception handling.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class GymOwnerService implements GymOwnerInterface {
    private GymOwnerDAO ownerDAO = new GymOwnerDAOImpl();

    /**
     * Retrieves all approved gym centers.
     * This is typically used by customers, but available here too.
     * 
     * @return a list of all approved gym centers
     * @throws DatabaseException if database operation fails
     */
    @Override
    public List<GymCenter> getAllCenters() throws DatabaseException {
        try {
            // For simplicity, returning all approved centers
            return new com.flipfit.dao.impl.GymCustomerDAOImpl().viewCenters();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve all centers: " + e.getMessage(), e);
        }
    }

    /**
     * Manages slots for a specific center.
     * 
     * @param centerId the ID of the center
     * @throws ValidationException if centerId is invalid
     * @throws DatabaseException if database operation fails
     */
    @Override
    public void manageSlots(String centerId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(centerId, "Center ID");
            System.out.println("[SYSTEM] Managing slots for Center: " + centerId);
        } catch (ValidationException e) {
            throw new ValidationException("Manage slots validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to manage slots: " + e.getMessage(), e);
        }
    }

    /**
     * Updates the capacity of a slot.
     * 
     * @param slotId the ID of the slot
     * @param newCapacity the new capacity value
     * @throws ValidationException if input validation fails
     * @throws DatabaseException if database operation fails
     */
    @Override
    public void updateSlotCapacity(String slotId, int newCapacity) 
            throws ValidationException, DatabaseException {
        // Validate inputs
        try {
            InputValidator.validateId(slotId, "Slot ID");
            InputValidator.validateCapacity(newCapacity);
        } catch (ValidationException e) {
            throw new ValidationException("Update slot capacity validation failed: " + e.getMessage(), e);
        }

        try {
            ownerDAO.updateSlotCapacity(slotId, newCapacity);
            System.out.println("[SYSTEM] Slot " + slotId + " capacity updated to " + newCapacity);
        } catch (Exception e) {
            throw new DatabaseException("Failed to update slot capacity: " + e.getMessage(), e);
        }
    }

    /**
     * Onboards a new gym owner into the system.
     * Validates all inputs, hashes password, and creates owner record pending approval.
     * 
     * @param username the username for the new owner
     * @param password the plain text password (will be hashed)
     * @param pan the PAN number
     * @param gst the GST number
     * @param aadhar the Aadhar number
     * @param location the location of the gym
     * @throws ValidationException if input validation fails
     * @throws DatabaseException if database operation fails
     */
    @Override
    public void onboardGymOwner(String username, String password, String pan, String gst, String aadhar,
            String location) throws ValidationException, DatabaseException {
        // Validate all inputs
        try {
            InputValidator.validateUsername(username);
            InputValidator.validatePassword(password);
            InputValidator.validatePAN(pan);
            InputValidator.validateGST(gst);
            InputValidator.validateAadhar(aadhar);
            InputValidator.validateLocation(location);
        } catch (ValidationException e) {
            throw new ValidationException("Gym owner onboarding validation failed: " + e.getMessage(), e);
        }

        try {
            GymOwner newOwner = new GymOwner();
            newOwner.setUserId(username);
            newOwner.setName(username);
            newOwner.setEmail(username + "@flipfit.com");
            
            // Hash password before storing
            String hashedPassword = PasswordUtil.hashPassword(password);
            newOwner.setPassword(hashedPassword);
            
            newOwner.setPanNumber(pan.toUpperCase());
            newOwner.setGstNumber(gst.toUpperCase());
            newOwner.setAadharNumber(aadhar);
            newOwner.setLocation(location);
            newOwner.setRole(Role.GYM_OWNER);
            newOwner.setApproved(false);

            // Register using the populated GymOwner instance
            com.flipfit.dao.GymUserDAO userDAO = new com.flipfit.dao.impl.GymUserDAOImpl();
            boolean success = userDAO.registerUser(newOwner);
            if (success) {
                System.out.println(
                        "[SYSTEM] Gym Owner registration successful for " + username + ". Waiting for admin approval.");
            } else {
                throw new DatabaseException("Failed to register gym owner");
            }
        } catch (DatabaseException e) {
            // Re-throw database exceptions
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to onboard gym owner: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a new gym center for an owner.
     * 
     * @param ownerId the ID of the owner
     * @param centerName the name of the center
     * @param location the location of the center
     * @throws ValidationException if input validation fails
     * @throws DatabaseException if database operation fails
     */
    @Override
    public void addGymCenter(String ownerId, String centerName, String location) 
            throws ValidationException, DatabaseException {
        // Validate inputs
        try {
            InputValidator.validateId(ownerId, "Owner ID");
            InputValidator.validateName(centerName, "Center name");
            InputValidator.validateLocation(location);
        } catch (ValidationException e) {
            throw new ValidationException("Add gym center validation failed: " + e.getMessage(), e);
        }

        try {
            GymCenter center = new GymCenter();
            center.setCenterId("CENT" + System.currentTimeMillis() % 10000);
            center.setName(centerName);
            center.setCity(location);
            center.setAddress(location);
            center.setOwnerId(ownerId);
            center.setApproved(false);
            ownerDAO.addGymCenter(center);
            System.out.println("[SYSTEM] Center added successfully. Pending admin approval.");
        } catch (Exception e) {
            throw new DatabaseException("Failed to add gym center: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all centers owned by a specific owner.
     * 
     * @param ownerId the ID of the owner
     * @return a list of centers owned by the owner
     * @throws ValidationException if ownerId is invalid
     * @throws DatabaseException if database operation fails
     */
    @Override
    public List<GymCenter> viewMyCenters(String ownerId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(ownerId, "Owner ID");
            return ownerDAO.viewMyCenters(ownerId);
        } catch (ValidationException e) {
            throw new ValidationException("View my centers validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve owner centers: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a new slot to a center.
     * 
     * @param centerId the ID of the center
     * @param startTime the start time of the slot
     * @param endTime the end time of the slot
     * @param capacity the capacity of the slot
     * @return true if slot added successfully
     * @throws ValidationException if input validation fails
     * @throws DatabaseException if database operation fails
     */
    @Override
    public boolean addSlot(String centerId, LocalTime startTime, LocalTime endTime, int capacity) 
            throws ValidationException, DatabaseException {
        // Validate inputs
        try {
            InputValidator.validateId(centerId, "Center ID");
            InputValidator.validateTimeRange(startTime, endTime);
            InputValidator.validateCapacity(capacity);
        } catch (ValidationException e) {
            throw new ValidationException("Add slot validation failed: " + e.getMessage(), e);
        }

        try {
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
        } catch (Exception e) {
            throw new DatabaseException("Failed to add slot: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all slots for a specific center.
     * 
     * @param centerId the ID of the center
     * @return a list of slots for the center
     * @throws ValidationException if centerId is invalid
     * @throws DatabaseException if database operation fails
     */
    @Override
    public List<SlotMaster> viewSlots(String centerId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(centerId, "Center ID");
            return ownerDAO.viewSlots(centerId);
        } catch (ValidationException e) {
            throw new ValidationException("View slots validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve slots: " + e.getMessage(), e);
        }
    }

    // These static methods are used by AdminService
    
    /**
     * Retrieves a slot by its ID.
     * 
     * @param slotId the ID of the slot
     * @return the SlotMaster object if found, null otherwise
     * @throws ValidationException if slotId is invalid
     * @throws DatabaseException if database operation fails
     */
    public static SlotMaster getSlot(String slotId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(slotId, "Slot ID");
            // Use the new getSlotById method from DAO
            return new GymOwnerDAOImpl().getSlotById(slotId);
        } catch (ValidationException e) {
            throw new ValidationException("Get slot validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve slot: " + e.getMessage(), e);
        }
    }

    /**
     * Updates the availability of a slot.
     * 
     * @param slotId the ID of the slot
     * @param delta the change in available seats (positive to increase, negative to decrease)
     * @throws ValidationException if slotId is invalid
     * @throws DatabaseException if database operation fails
     */
    public static void updateAvailability(String slotId, int delta) 
            throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(slotId, "Slot ID");
            // Delegate to DAO layer
            GymOwnerDAO ownerDAO = new GymOwnerDAOImpl();
            ownerDAO.updateAvailableSeats(slotId, delta);
        } catch (ValidationException e) {
            throw new ValidationException("Update availability validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to update availability: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a center by its ID.
     * 
     * @param centerId the ID of the center
     * @return the GymCenter object if found, null otherwise
     * @throws ValidationException if centerId is invalid
     * @throws DatabaseException if database operation fails
     */
    public static GymCenter getCenterById(String centerId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(centerId, "Center ID");
            // Use admin DAO to fetch center by ID without relying on customer-approved filtering
            return new GymAdminDAOImpl().getCenterById(centerId);
        } catch (ValidationException e) {
            throw new ValidationException("Get center validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve center: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all pending gym owners awaiting approval.
     * 
     * @return a list of pending gym owners
     * @throws DatabaseException if database operation fails
     */
    public static List<GymOwner> getPendingOwners() throws DatabaseException {
        try {
            return new GymAdminDAOImpl().viewPendingGymOwners();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve pending owners: " + e.getMessage(), e);
        }
    }

    /**
     * Approves a gym owner.
     * 
     * @param ownerId the ID of the owner to approve
     * @throws ValidationException if ownerId is invalid
     * @throws DatabaseException if database operation fails
     */
    public static void approveOwner(String ownerId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(ownerId, "Owner ID");
            new GymAdminDAOImpl().approveGymOwner(ownerId);
        } catch (ValidationException e) {
            throw new ValidationException("Approve owner validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to approve owner: " + e.getMessage(), e);
        }
    }

    /**
     * Approves a slot.
     * 
     * @param slotId the ID of the slot to approve
     * @throws ValidationException if slotId is invalid
     * @throws DatabaseException if database operation fails
     */
    public static void approveSlot(String slotId) throws ValidationException, DatabaseException {
        try {
            InputValidator.validateId(slotId, "Slot ID");
            new GymAdminDAOImpl().approveSlot(slotId);
        } catch (ValidationException e) {
            throw new ValidationException("Approve slot validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DatabaseException("Failed to approve slot: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all slots (typically pending slots).
     * 
     * @return a list of slots
     * @throws DatabaseException if database operation fails
     */
    public static List<SlotMaster> getAllSlots() throws DatabaseException {
        try {
            // This is a bit inefficient but for compatibility:
            return new GymAdminDAOImpl().viewPendingSlots();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve all slots: " + e.getMessage(), e);
        }
    }
}
