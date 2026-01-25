package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Role;
import com.flipfit.bean.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GymOwnerService implements GymOwnerInterface {
    // Centers are specific to this service
    private static List<GymCenter> centers = new ArrayList<>();

    static {
        // Hardcoded data for a center
        GymCenter center1 = new GymCenter();
        center1.setCenterId("CENT1");
        center1.setName("Elite Fitness");
        center1.setCity("Bangalore");
        center1.setAddress("Indiranagar");
        center1.setOwnerId("owner1"); // owner1 is hardcoded in UserService
        center1.setApproved(true);
        centers.add(center1);
    }

    @Override
    public void manageSlots(String centerId) {
        System.out.println("[SYSTEM] Managing slots for Center: " + centerId);
    }

    @Override
    public void updateSlotCapacity(String scheduleId, int newCapacity) {
        System.out.println("[SYSTEM] Slot " + scheduleId + " capacity updated to " + newCapacity);
    }

    @Override
    public void onboardGymOwner(String username, String password, String pan) {
        GymOwner newOwner = new GymOwner();
        newOwner.setUserId(username);
        newOwner.setName(username);
        newOwner.setPassword(password);
        newOwner.setPanNumber(pan);
        newOwner.setRole(Role.GYM_OWNER);
        newOwner.setApproved(false); // Admin must approve

        // Add to the shared user map in UserService
        UserService.addUser(newOwner);

        System.out.println(
                "[SYSTEM] Gym Owner registration successful for " + username + ". Waiting for admin approval.");
    }

    @Override
    public void addGymCenter(String ownerId, String centerName, String location) {
        GymCenter center = new GymCenter();
        center.setCenterId("CENT" + (centers.size() + 1));
        center.setName(centerName);
        center.setCity(location);
        center.setAddress(location);
        center.setOwnerId(ownerId);
        center.setApproved(false); // Admin must approve center
        centers.add(center);
        System.out.println("[SYSTEM] Center added successfully. Pending admin approval.");
    }

    @Override
    public List<GymCenter> viewMyCenters(String ownerId) {
        return centers.stream()
                .filter(c -> c.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    // Static helper for AdminService to query global user list for pending owners
    public static List<GymOwner> getPendingOwners() {
        return UserService.getAllUsers().values().stream()
                .filter(u -> u instanceof GymOwner)
                .map(u -> (GymOwner) u)
                .filter(o -> !o.isApproved())
                .collect(Collectors.toList());
    }

    // Static helper for AdminService to approve owner in the global map
    public static void approveOwner(String ownerId) {
        User user = UserService.getUser(ownerId);
        if (user instanceof GymOwner) {
            ((GymOwner) user).setApproved(true);
            System.out.println("[SYSTEM] Gym Owner " + ownerId + " has been approved.");
        }
    }
}
