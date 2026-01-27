package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.dao.impl.GymAdminDAOImpl;

import java.util.List;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class AdminService.
 *
 * @author Zeta
 * @ClassName  "AdminService"
 */
public class AdminService implements AdminInterface {
    
    /** The admin DAO. */
    private GymAdminDAO adminDAO = new GymAdminDAOImpl();

    /**
     * Approve gym owner.
     *
     * @param ownerId the owner id
     */
    @Override
    public void approveGymOwner(String ownerId) {
        adminDAO.approveGymOwner(ownerId);
        System.out.println("[ADMIN] Gym Owner " + ownerId + " has been approved.");
    }

    /**
     * Onboard center.
     *
     * @param center the center
     */
    @Override
    public void onboardCenter(GymCenter center) {
        System.out.println("[ADMIN] Gym Center " + center.getName() + " onboarded successfully.");
    }
    
    /**
     * Approve gym center.
     *
     * @param centerId the center id
     */
    @Override
    public void approveGymCenter(String centerId) {
        adminDAO.approveGymCenter(centerId);
        System.out.println("[ADMIN] Gym Center " + centerId + " has been approved.");
    }

    /**
     * View pending gym owners.
     *
     * @return the list
     */
    @Override
    public List<GymOwner> viewPendingGymOwners() {
        return adminDAO.viewPendingGymOwners();
    }

    /**
     * View pending gym centers.
     *
     * @return the list
     */
    @Override
    public List<GymCenter> viewPendingGymCenters() {
        return adminDAO.viewPendingGymCenters();
    }

    /**
     * Approve slot.
     *
     * @param slotId the slot id
     */
    @Override
    public void approveSlot(String slotId) {
        adminDAO.approveSlot(slotId);
        System.out.println("[ADMIN] Slot " + slotId + " has been approved.");
    }

    /**
     * View pending slots.
     *
     * @return the list
     */
    @Override
    public List<SlotMaster> viewPendingSlots() {
        return adminDAO.viewPendingSlots();
    }
}