package com.flipfit.dao;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;
import java.util.List;

public interface GymAdminDAO {
    public void approveGymOwner(String ownerId);

    public void approveGymCenter(String centerId);

    public void approveSlot(String slotId);

    public List<GymOwner> viewPendingGymOwners();

    public List<GymCenter> viewPendingGymCenters();

    public List<SlotMaster> viewPendingSlots();
}
