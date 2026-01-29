package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.NotificationType;
import com.flipfit.bean.SlotMaster;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.dao.impl.GymAdminDAOImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class AdminService.
 *
 * @author Zeta
 * @ClassName "AdminService"
 */
public class AdminService implements AdminInterface {

    /** The admin DAO. */
    private GymAdminDAO adminDAO = new GymAdminDAOImpl();

    /** The notification service. */
    private NotificationService notificationService = new NotificationService();

    /**
     * Approve gym owner.
     *
     * @param ownerId the owner id
     */
    @Override
    public void approveGymOwner(String ownerId) {
        adminDAO.approveGymOwner(ownerId);
        notificationService.sendNotification(ownerId,
                "Your Gym Owner account has been approved by the Admin.",
                NotificationType.OWNER_APPROVAL);
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
        GymCenter center = GymOwnerService.getCenterById(centerId);
        if (center != null && center.getOwnerId() != null) {
            notificationService.sendNotification(center.getOwnerId(),
                    "Your Gym Center '" + center.getName() + "' has been approved by the Admin.",
                    NotificationType.OWNER_APPROVAL);
        }
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

    /**
     * View gym owners by status.
     *
     * @param isApproved the is approved
     * @return the list
     */
    @Override
    public List<GymOwner> viewGymOwnersByStatus(boolean isApproved) {
        return adminDAO.getAllGymOwners().stream()
                .filter(owner -> owner.isApproved() == isApproved)
                .collect(Collectors.toList());
    }

    /**
     * View gym centers by status.
     *
     * @param isApproved the is approved
     * @return the list
     */
    @Override
    public List<GymCenter> viewGymCentersByStatus(boolean isApproved) {
        return adminDAO.getAllGymCenters().stream()
                .filter(center -> center.isApproved() == isApproved)
                .collect(Collectors.toList());
    }
}