package com.flipfit.dao;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.SlotMaster;
import java.util.List;

public interface GymOwnerDAO {
    public void addGymCenter(GymCenter center);

    public List<GymCenter> viewMyCenters(String ownerId);

    public boolean addSlot(SlotMaster slot);

    public List<SlotMaster> viewSlots(String centerId);

    public void updateSlotCapacity(String slotId, int newCapacity);
}
