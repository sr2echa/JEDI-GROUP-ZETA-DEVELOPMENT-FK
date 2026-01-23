package com.flipfit.business;

public interface GymOwnerInterface {
    void manageSlots(String centerId);
    void updateSlotCapacity(String scheduleId, int newCapacity);
}
