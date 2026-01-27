package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.dao.impl.GymAdminDAOImpl;

import java.util.List;

public class AdminService implements AdminInterface {
    private GymAdminDAO adminDAO = new GymAdminDAOImpl();

    @Override
    public void approveGymOwner(String ownerId) {
        adminDAO.approveGymOwner(ownerId);
        System.out.println("[ADMIN] Gym Owner " + ownerId + " has been approved.");
    }

    @Override
    public void onboardCenter(GymCenter center) {
        // This is usually done by owner, admin just approves.
        // If this is meant to be admin-initiated onboarding:
        System.out.println("[ADMIN] Gym Center " + center.getName() + " onboarded successfully.");
    }

    @Override
    public List<GymOwner> viewPendingGymOwners() {
        return adminDAO.viewPendingGymOwners();
    }

    @Override
    public List<GymCenter> viewPendingGymCenters() {
        return adminDAO.viewPendingGymCenters();
    }

    @Override
    public void approveSlot(String slotId) {
        adminDAO.approveSlot(slotId);
        System.out.println("[ADMIN] Slot " + slotId + " has been approved.");
    }

    @Override
    public List<SlotMaster> viewPendingSlots() {
        return adminDAO.viewPendingSlots();
    }
}
