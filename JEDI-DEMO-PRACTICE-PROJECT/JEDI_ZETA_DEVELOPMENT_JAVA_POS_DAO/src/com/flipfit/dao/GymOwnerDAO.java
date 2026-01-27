package com.flipfit.dao;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.SlotMaster;
import java.util.List;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Interface GymOwnerDAO.
 *
 * @author Zeta
 * @ClassName  "GymOwnerDAO"
 */
public interface GymOwnerDAO {
    
    /**
     * Adds the gym center.
     *
     * @param center the center
     */
    public void addGymCenter(GymCenter center);

    /**
     * View my centers.
     *
     * @param ownerId the owner id
     * @return the list
     */
    public List<GymCenter> viewMyCenters(String ownerId);

    /**
     * Adds the slot.
     *
     * @param slot the slot
     * @return true, if successful
     */
    public boolean addSlot(SlotMaster slot);

    /**
     * View slots.
     *
     * @param centerId the center id
     * @return the list
     */
    public List<SlotMaster> viewSlots(String centerId);

    /**
     * Update slot capacity.
     *
     * @param slotId the slot id
     * @param newCapacity the new capacity
     */
    public void updateSlotCapacity(String slotId, int newCapacity);
    
    /**
     * Gets the slot by id.
     *
     * @param slotId the slot id
     * @return the slot by id
     */
    public SlotMaster getSlotById(String slotId);
    
    /**
     * Update available seats.
     *
     * @param slotId the slot id
     * @param delta the delta
     */
    public void updateAvailableSeats(String slotId, int delta);
}