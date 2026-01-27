package com.flipfit.dao;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.SlotMaster;
import java.util.List;

public interface GymCustomerDAO {
    public List<GymCenter> viewCenters();

    public List<SlotMaster> viewSlots(String centerId);

    public boolean bookSlot(String userId, String slotId, String date);

    public List<Booking> viewMyBookings(String userId);

    public void cancelBooking(String bookingId);

    public Booking getBookingById(String bookingId);

    public void updateBookingStatus(String bookingId, String status);
}
