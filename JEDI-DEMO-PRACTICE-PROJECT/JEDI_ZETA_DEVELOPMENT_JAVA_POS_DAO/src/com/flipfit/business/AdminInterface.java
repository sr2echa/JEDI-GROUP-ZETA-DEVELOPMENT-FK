package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;

import java.util.List;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Interface AdminInterface.
 *
 * @author Zeta
 * @ClassName  "AdminInterface"
 */
public interface AdminInterface {
    
    /**
     * Approve gym owner.
     *
     * @param ownerId the owner id
     */
    void approveGymOwner(String ownerId);

    /**
     * Onboard center.
     *
     * @param center the center
     */
    void onboardCenter(GymCenter center);
    
    /**
     * Approve gym center.
     *
     * @param centerId the center id
     */
    void approveGymCenter(String centerId);

    /**
     * Approve slot.
     *
     * @param slotId the slot id
     */
    void approveSlot(String slotId);
    
    /**
     * View pending slots.
     *
     * @return the list
     */
    List<SlotMaster> viewPendingSlots();
    
    /**
     * View pending gym owners.
     *
     * @return the list
     */
    List<GymOwner> viewPendingGymOwners();

    /**
     * View pending gym centers.
     *
     * @return the list
     */
    List<GymCenter> viewPendingGymCenters();
}