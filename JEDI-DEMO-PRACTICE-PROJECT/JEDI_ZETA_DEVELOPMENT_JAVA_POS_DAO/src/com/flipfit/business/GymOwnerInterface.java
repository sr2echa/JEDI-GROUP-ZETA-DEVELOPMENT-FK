package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.SlotMaster;
import com.flipfit.exception.*;
import java.util.List;
import java.time.LocalTime;

/**
 * Interface for gym owner-related operations including center management, slot management, and owner registration.
 * 
 * @author FlipFit Development Team
 * @version 1.0
 */
public interface GymOwnerInterface {
    /**
     * Manages slots for a specific center.
     * 
     * @param centerId the ID of the center
     * @throws ValidationException if centerId is invalid
     * @throws DatabaseException if database operation fails
     */
    void manageSlots(String centerId) throws ValidationException, DatabaseException;

    /**
     * Updates the capacity of a slot.
     * 
     * @param scheduleId the ID of the slot
     * @param newCapacity the new capacity value
     * @throws ValidationException if input validation fails
     * @throws DatabaseException if database operation fails
     */
    void updateSlotCapacity(String scheduleId, int newCapacity) 
            throws ValidationException, DatabaseException;

    /**
     * Onboards a new gym owner into the system.
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
    void onboardGymOwner(String username, String password, String pan, String gst, String aadhar, String location) 
            throws ValidationException, DatabaseException;

    /**
     * Adds a new gym center for an owner.
     * 
     * @param ownerId the ID of the owner
     * @param centerName the name of the center
     * @param location the location of the center
     * @throws ValidationException if input validation fails
     * @throws DatabaseException if database operation fails
     */
    void addGymCenter(String ownerId, String centerName, String location) 
            throws ValidationException, DatabaseException;

    /**
     * Retrieves all centers owned by a specific owner.
     * 
     * @param ownerId the ID of the owner
     * @return a list of centers owned by the owner
     * @throws ValidationException if ownerId is invalid
     * @throws DatabaseException if database operation fails
     */
    List<GymCenter> viewMyCenters(String ownerId) throws ValidationException, DatabaseException;

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
    boolean addSlot(String centerId, LocalTime startTime, LocalTime endTime, int capacity) 
            throws ValidationException, DatabaseException;

    /**
     * Retrieves all slots for a specific center.
     * 
     * @param centerId the ID of the center
     * @return a list of slots for the center
     * @throws ValidationException if centerId is invalid
     * @throws DatabaseException if database operation fails
     */
    List<SlotMaster> viewSlots(String centerId) throws ValidationException, DatabaseException;

    /**
     * Retrieves all approved gym centers.
     * 
     * @return a list of all approved gym centers
     * @throws DatabaseException if database operation fails
     */
    List<GymCenter> getAllCenters() throws DatabaseException;
}
