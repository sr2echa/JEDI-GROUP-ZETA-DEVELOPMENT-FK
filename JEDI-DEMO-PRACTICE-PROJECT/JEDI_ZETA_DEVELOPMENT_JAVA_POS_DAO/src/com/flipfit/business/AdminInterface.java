package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;

import java.util.List;

public interface AdminInterface {
    void approveGymOwner(String ownerId);

    void onboardCenter(GymCenter center);

    void approveSlot(String slotId);
    
    List<SlotMaster> viewPendingSlots();
    
    List<GymOwner> viewPendingGymOwners();

    List<GymCenter> viewPendingGymCenters();
}
