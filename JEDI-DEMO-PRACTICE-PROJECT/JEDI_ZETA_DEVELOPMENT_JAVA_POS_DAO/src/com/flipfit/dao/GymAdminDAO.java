package com.flipfit.dao;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;
import java.util.List;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Interface GymAdminDAO.
 *
 * @author Zeta
 * @ClassName  "GymAdminDAO"
 */
public interface GymAdminDAO {
    
    /**
     * Approve gym owner.
     *
     * @param ownerId the owner id
     */
    public void approveGymOwner(String ownerId);

    /**
     * Approve gym center.
     *
     * @param centerId the center id
     */
    public void approveGymCenter(String centerId);

    /**
     * Approve slot.
     *
     * @param slotId the slot id
     */
    public void approveSlot(String slotId);

    /**
     * View pending gym owners.
     *
     * @return the list
     */
    public List<GymOwner> viewPendingGymOwners();

    /**
     * View pending gym centers.
     *
     * @return the list
     */
    public List<GymCenter> viewPendingGymCenters();

    /**
     * View pending slots.
     *
     * @return the list
     */
    public List<SlotMaster> viewPendingSlots();
    
    /**
     * Gets the center by id.
     *
     * @param centerId the center id
     * @return the center by id
     */
    public GymCenter getCenterById(String centerId);
}