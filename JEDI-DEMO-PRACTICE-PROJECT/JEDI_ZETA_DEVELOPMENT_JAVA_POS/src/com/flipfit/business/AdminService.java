package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import java.util.List;

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
}
