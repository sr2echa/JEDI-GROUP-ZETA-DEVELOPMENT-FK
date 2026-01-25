package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GymOwnerService implements GymOwnerInterface {
    private static List<GymCenter> centers = new ArrayList<>();
    private static List<GymOwner> owners = new ArrayList<>();

    static {
        // Hardcoded data
        GymOwner owner1 = new GymOwner();
        owner1.setUserId("owner1");
        owner1.setName("Gym Master");
        owner1.setPanNumber("ABCDE1234F");
        owner1.setApproved(true);
        owners.add(owner1);

        GymCenter center1 = new GymCenter();
        center1.setCenterId("CENT1");
        center1.setName("Elite Fitness");
        center1.setCity("Bangalore");
        center1.setAddress("Indiranagar");
        center1.setOwnerId("owner1");
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
        newOwner.setApproved(false);
        owners.add(newOwner);
        System.out.println("[SYSTEM] Gym Owner registration successful. Waiting for admin approval.");
    }

    @Override
    public void addGymCenter(String ownerId, String centerName, String location) {
        GymCenter center = new GymCenter();
        center.setCenterId("CENT" + (centers.size() + 1));
        center.setName(centerName);
        center.setCity(location);
        center.setAddress(location);
        center.setOwnerId(ownerId);
        center.setApproved(false);
        centers.add(center);
        System.out.println("[SYSTEM] Center added successfully. Pending admin approval.");
    }

    @Override
    public List<GymCenter> viewMyCenters(String ownerId) {
        return centers.stream()
                .filter(c -> c.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    public static List<GymOwner> getPendingOwners() {
        return owners.stream().filter(o -> !o.isApproved()).collect(Collectors.toList());
    }

    public static void approveOwner(String ownerId) {
        owners.stream()
                .filter(o -> o.getUserId().equals(ownerId))
                .forEach(o -> o.setApproved(true));
    }
}
