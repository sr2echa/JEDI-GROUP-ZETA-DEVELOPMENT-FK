package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;
import com.flipfit.exception.*;
import java.util.List;

/**
 * Interface for admin-related operations including approval of gym owners, centers, and slots.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public interface AdminInterface {
    /**
     * Approves a gym owner, allowing them to use the system.
     * 
     * @param ownerId the ID of the owner to approve
     * @throws ValidationException if ownerId is invalid
     * @throws AuthorizationException if operation is not authorized
     * @throws DatabaseException if database operation fails
     */
    void approveGymOwner(String ownerId) 
            throws ValidationException, AuthorizationException, DatabaseException;

    /**
     * Onboards a gym center (admin-initiated).
     * 
     * @param center the GymCenter object to onboard
     * @throws ValidationException if center data is invalid
     * @throws DatabaseException if database operation fails
     */
    void onboardCenter(GymCenter center) throws ValidationException, DatabaseException;
    
    /**
     * Approves a gym center, making it available for bookings.
     * 
     * @param centerId the ID of the center to approve
     * @throws ValidationException if centerId is invalid
     * @throws AuthorizationException if operation is not authorized
     * @throws DatabaseException if database operation fails
     */
    void approveGymCenter(String centerId) 
            throws ValidationException, AuthorizationException, DatabaseException;

    /**
     * Approves a slot, making it available for bookings.
     * 
     * @param slotId the ID of the slot to approve
     * @throws ValidationException if slotId is invalid
     * @throws AuthorizationException if operation is not authorized
     * @throws DatabaseException if database operation fails
     */
    void approveSlot(String slotId) 
            throws ValidationException, AuthorizationException, DatabaseException;
    
    /**
     * Retrieves all pending slots awaiting approval.
     * 
     * @return a list of pending slots
     * @throws DatabaseException if database operation fails
     */
    List<SlotMaster> viewPendingSlots() throws DatabaseException;
    
    /**
     * Retrieves all pending gym owners awaiting approval.
     * 
     * @return a list of pending gym owners
     * @throws DatabaseException if database operation fails
     */
    List<GymOwner> viewPendingGymOwners() throws DatabaseException;

    /**
     * Retrieves all pending gym centers awaiting approval.
     * 
     * @return a list of pending gym centers
     * @throws DatabaseException if database operation fails
     */
    List<GymCenter> viewPendingGymCenters() throws DatabaseException;
}
