package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.SlotMaster;
import java.util.List;
import java.time.LocalTime;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Interface GymOwnerInterface.
 *
 * @author Zeta
 * @ClassName  "GymOwnerInterface"
 */
public interface GymOwnerInterface {
    
    /**
     * Manage slots.
     *
     * @param centerId the center id
     */
    void manageSlots(String centerId);

    /**
     * Update slot capacity.
     *
     * @param scheduleId the schedule id
     * @param newCapacity the new capacity
     */
    void updateSlotCapacity(String scheduleId, int newCapacity);

    /**
     * Onboard gym owner.
     *
     * @param username the username
     * @param password the password
     * @param pan the pan
     * @param gst the gst
     * @param aadhar the aadhar
     * @param location the location
     */
    void onboardGymOwner(String username, String password, String pan, String gst, String aadhar, String location);

    /**
     * Adds the gym center.
     *
     * @param ownerId the owner id
     * @param centerName the center name
     * @param location the location
     */
    void addGymCenter(String ownerId, String centerName, String location);

    /**
     * View my centers.
     *
     * @param ownerId the owner id
     * @return the list
     */
    List<GymCenter> viewMyCenters(String ownerId);

    /**
     * Adds the slot.
     *
     * @param centerId the center id
     * @param startTime the start time
     * @param endTime the end time
     * @param capacity the capacity
     * @return true, if successful
     */
    boolean addSlot(String centerId, LocalTime startTime, LocalTime endTime, int capacity);

    /**
     * View slots.
     *
     * @param centerId the center id
     * @return the list
     */
    List<SlotMaster> viewSlots(String centerId);

    /**
     * Gets the all centers.
     *
     * @return the all centers
     */
    List<GymCenter> getAllCenters();
}