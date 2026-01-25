package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import java.util.List;

public interface GymOwnerInterface {
    void manageSlots(String centerId);

    void updateSlotCapacity(String scheduleId, int newCapacity);

    void onboardGymOwner(String username, String password, String pan);

    void addGymCenter(String ownerId, String centerName, String location);

    List<GymCenter> viewMyCenters(String ownerId);
}
