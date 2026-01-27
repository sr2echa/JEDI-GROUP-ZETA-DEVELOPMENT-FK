package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;

import java.util.List;
import java.util.stream.Collectors;

public class AdminService implements AdminInterface {

    @Override
    public void approveGymOwner(String ownerId) {
        GymOwnerService.approveOwner(ownerId);
        System.out.println("[ADMIN] Gym Owner " + ownerId + " has been approved.");
    }

    @Override
    public void onboardCenter(GymCenter center) {
        System.out.println("[ADMIN] Gym Center " + center.getName() + " onboarded successfully.");
    }

    @Override
    public List<GymOwner> viewPendingGymOwners() {
        return GymOwnerService.getPendingOwners();
    }

    @Override
    public List<GymCenter> viewPendingGymCenters() {
        // Mocking for now, could be integrated with GymOwnerService centers list
        return java.util.Collections.emptyList();
    }

    @Override
    public void approveSlot(String slotId) {
        GymOwnerService.approveSlot(slotId);
        System.out.println("[ADMIN] Slot " + slotId + " has been approved.");
    }

    @Override
    public List<SlotMaster> viewPendingSlots() {
        // This requires access to the allSlots list in GymOwnerService
        // You may need to add a getter in GymOwnerService for allSlots
        return GymOwnerService.getAllSlots().stream()
                .filter(s -> !s.isApproved())
                .collect(Collectors.toList());
    }
}
