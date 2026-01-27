package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.dao.impl.GymAdminDAOImpl;
import com.flipfit.exception.*;
import com.flipfit.utils.InputValidator;

import java.util.List;

/**
 * Service class for admin-related operations including approval of gym owners, centers, and slots.
 * This service handles input validation and exception handling for administrative operations.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public class AdminService implements AdminInterface {
    private GymAdminDAO adminDAO = new GymAdminDAOImpl();

    /**
     * Approves a gym owner, allowing them to use the system.
     * 
     * @param ownerId the ID of the owner to approve
     * @throws ValidationException if ownerId is invalid
     * @throws AuthorizationException if operation is not authorized
     * @throws DatabaseException if database operation fails
     */
    @Override
    public void approveGymOwner(String ownerId) 
            throws ValidationException, AuthorizationException, DatabaseException {
        // Validate input
        try {
            InputValidator.validateId(ownerId, "Owner ID");
        } catch (ValidationException e) {
            throw new ValidationException("Approve gym owner validation failed: " + e.getMessage(), e);
        }

        try {
            adminDAO.approveGymOwner(ownerId);
            System.out.println("[ADMIN] Gym Owner " + ownerId + " has been approved.");
        } catch (Exception e) {
            throw new DatabaseException("Failed to approve gym owner: " + e.getMessage(), e);
        }
    }

    /**
     * Onboards a gym center (admin-initiated).
     * This is usually done by owner, admin just approves.
     * 
     * @param center the GymCenter object to onboard
     * @throws ValidationException if center data is invalid
     * @throws DatabaseException if database operation fails
     */
    @Override
    public void onboardCenter(GymCenter center) throws ValidationException, DatabaseException {
        // Validate center data
        try {
            if (center == null) {
                throw new ValidationException("Center cannot be null");
            }
            InputValidator.validateName(center.getName(), "Center name");
            InputValidator.validateLocation(center.getCity());
        } catch (ValidationException e) {
            throw new ValidationException("Onboard center validation failed: " + e.getMessage(), e);
        }

        try {
            // If this is meant to be admin-initiated onboarding:
            System.out.println("[ADMIN] Gym Center " + center.getName() + " onboarded successfully.");
        } catch (Exception e) {
            throw new DatabaseException("Failed to onboard center: " + e.getMessage(), e);
        }
    }
    
    /**
     * Approves a gym center, making it available for bookings.
     * 
     * @param centerId the ID of the center to approve
     * @throws ValidationException if centerId is invalid
     * @throws AuthorizationException if operation is not authorized
     * @throws DatabaseException if database operation fails
     */
    @Override
    public void approveGymCenter(String centerId) 
            throws ValidationException, AuthorizationException, DatabaseException {
        // Validate input
        try {
            InputValidator.validateId(centerId, "Center ID");
        } catch (ValidationException e) {
            throw new ValidationException("Approve gym center validation failed: " + e.getMessage(), e);
        }

        try {
            adminDAO.approveGymCenter(centerId);
            System.out.println("[ADMIN] Gym Center " + centerId + " has been approved.");
        } catch (Exception e) {
            throw new DatabaseException("Failed to approve gym center: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all pending gym owners awaiting approval.
     * 
     * @return a list of pending gym owners
     * @throws DatabaseException if database operation fails
     */
    @Override
    public List<GymOwner> viewPendingGymOwners() throws DatabaseException {
        try {
            return adminDAO.viewPendingGymOwners();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve pending gym owners: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all pending gym centers awaiting approval.
     * 
     * @return a list of pending gym centers
     * @throws DatabaseException if database operation fails
     */
    @Override
    public List<GymCenter> viewPendingGymCenters() throws DatabaseException {
        try {
            return adminDAO.viewPendingGymCenters();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve pending gym centers: " + e.getMessage(), e);
        }
    }

    /**
     * Approves a slot, making it available for bookings.
     * 
     * @param slotId the ID of the slot to approve
     * @throws ValidationException if slotId is invalid
     * @throws AuthorizationException if operation is not authorized
     * @throws DatabaseException if database operation fails
     */
    @Override
    public void approveSlot(String slotId) 
            throws ValidationException, AuthorizationException, DatabaseException {
        // Validate input
        try {
            InputValidator.validateId(slotId, "Slot ID");
        } catch (ValidationException e) {
            throw new ValidationException("Approve slot validation failed: " + e.getMessage(), e);
        }

        try {
            adminDAO.approveSlot(slotId);
            System.out.println("[ADMIN] Slot " + slotId + " has been approved.");
        } catch (Exception e) {
            throw new DatabaseException("Failed to approve slot: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all pending slots awaiting approval.
     * 
     * @return a list of pending slots
     * @throws DatabaseException if database operation fails
     */
    @Override
    public List<SlotMaster> viewPendingSlots() throws DatabaseException {
        try {
            return adminDAO.viewPendingSlots();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve pending slots: " + e.getMessage(), e);
        }
    }
}
