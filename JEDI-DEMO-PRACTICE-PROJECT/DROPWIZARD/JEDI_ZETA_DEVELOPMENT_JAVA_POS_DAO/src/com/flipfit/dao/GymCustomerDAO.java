package com.flipfit.dao;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.SlotMaster;
import java.util.List;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Interface GymCustomerDAO.
 *
 * @author Zeta
 * @ClassName  "GymCustomerDAO"
 */
public interface GymCustomerDAO {
    
    /**
     * View centers.
     *
     * @return the list
     */
    public List<GymCenter> viewCenters();

    /**
     * View slots.
     *
     * @param centerId the center id
     * @return the list
     */
    public List<SlotMaster> viewSlots(String centerId);

    /**
     * Book slot.
     *
     * @param userId the user id
     * @param slotId the slot id
     * @param date the date
     * @return true, if successful
     */
    public boolean bookSlot(String userId, String slotId, String date);

    /**
     * View my bookings.
     *
     * @param userId the user id
     * @return the list
     */
    public List<Booking> viewMyBookings(String userId);

    /**
     * Cancel booking.
     *
     * @param bookingId the booking id
     */
    public void cancelBooking(String bookingId);

    /**
     * Gets the booking by id.
     *
     * @param bookingId the booking id
     * @return the booking by id
     */
    public Booking getBookingById(String bookingId);

    /**
     * Update booking status.
     *
     * @param bookingId the booking id
     * @param status the status
     */
    public void updateBookingStatus(String bookingId, String status);
}