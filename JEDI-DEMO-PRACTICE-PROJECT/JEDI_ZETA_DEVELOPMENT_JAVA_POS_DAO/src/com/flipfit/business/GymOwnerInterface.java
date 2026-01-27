package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.SlotMaster;
import java.util.List;
import java.time.LocalTime;

public interface GymOwnerInterface {
    void manageSlots(String centerId);

    void updateSlotCapacity(String scheduleId, int newCapacity);

    void onboardGymOwner(String username, String password, String pan, String gst, String aadhar, String location);

    void addGymCenter(String ownerId, String centerName, String location);

    List<GymCenter> viewMyCenters(String ownerId);

    // Updated method for adding slots with capacity
    boolean addSlot(String centerId, LocalTime startTime, LocalTime endTime, int capacity);

    List<SlotMaster> viewSlots(String centerId);

    // Global lookup for customers
    List<GymCenter> getAllCenters();
}
