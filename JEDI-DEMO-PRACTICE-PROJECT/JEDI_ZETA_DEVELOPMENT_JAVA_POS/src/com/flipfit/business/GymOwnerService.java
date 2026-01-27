package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Role;
import com.flipfit.bean.User;
import com.flipfit.bean.SlotMaster;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalTime;

public class GymOwnerService implements GymOwnerInterface {
    private static List<GymCenter> centers = new ArrayList<>();
    private static List<SlotMaster> allSlots = new ArrayList<>();

    static {
        // Hardcoded data for a center
        GymCenter center1 = new GymCenter();
        center1.setCenterId("CENT1");
        center1.setName("Elite Fitness");
        center1.setCity("Bangalore");
        center1.setAddress("Indiranagar");
        center1.setOwnerId("owner1");
        center1.setApproved(true);
        centers.add(center1);

        // Hardcoded slot for CENT1
        SlotMaster slot1 = new SlotMaster();
        slot1.setSlotId("SLOT101");
        slot1.setCenterId("CENT1");
        slot1.setStartTime(LocalTime.of(10, 0));
        slot1.setEndTime(LocalTime.of(11, 0));
        slot1.setCapacity(2); // Small capacity for easy waitlist testing
        allSlots.add(slot1);
    }

    @Override
    public List<GymCenter> getAllCenters() {
        return centers.stream().filter(GymCenter::isApproved).collect(Collectors.toList());
    }

    @Override
    public void manageSlots(String centerId) {
        System.out.println("[SYSTEM] Managing slots for Center: " + centerId);
    }

    @Override
    public void updateSlotCapacity(String scheduleId, int newCapacity) {
        SlotMaster slot = getSlot(scheduleId);
        if (slot != null) {
            slot.setCapacity(newCapacity);
            System.out.println("[SYSTEM] Slot " + scheduleId + " capacity updated to " + newCapacity);
        }
    }

    @Override
    public void onboardGymOwner(String username, String password, String pan, String gst, String aadhar, String location) {
        GymOwner newOwner = new GymOwner();
        newOwner.setUserId(username);
        newOwner.setName(username);
        newOwner.setPassword(password);
        newOwner.setPanNumber(pan);
        newOwner.setGstNumber(gst);      // New addition
        newOwner.setAadharNumber(aadhar); // New addition
        newOwner.setLocation(location);   // New addition
        newOwner.setRole(Role.GYM_OWNER);
        newOwner.setApproved(false);
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

    @Override
    public boolean addSlot(String centerId, LocalTime startTime, LocalTime endTime, int capacity) {
        for (SlotMaster slot : allSlots) {
            if (slot.getCenterId().equals(centerId)) {
                if (startTime.isBefore(slot.getEndTime()) && endTime.isAfter(slot.getStartTime())) {
                    System.out.println("[ERROR] Slot overlaps with existing slot: " + slot.getStartTime() + " - "
                            + slot.getEndTime());
                    return false;
                }
            }
        }

        SlotMaster newSlot = new SlotMaster();
        newSlot.setSlotId("SLOT" + (allSlots.size() + 101));
        newSlot.setCenterId(centerId);
        newSlot.setStartTime(startTime);
        newSlot.setEndTime(endTime);
        newSlot.setCapacity(capacity);
        newSlot.setApproved(false); // Set default to unapproved

        allSlots.add(newSlot);
        System.out.println("[SUCCESS] Slot added: " + newSlot.getSlotId() + " [" + startTime + " to " + endTime
                + "] Capacity: " + capacity + ". Pending Admin approval.");
        return true;
    }

    @Override
    public List<SlotMaster> viewSlots(String centerId) {
        return allSlots.stream()
                .filter(s -> s.getCenterId().equals(centerId))
                .collect(Collectors.toList());
    }

    public static List<GymOwner> getPendingOwners() {
        return UserService.getAllUsers().values().stream()
                .filter(u -> u instanceof GymOwner)
                .map(u -> (GymOwner) u)
                .filter(o -> !o.isApproved())
                .collect(Collectors.toList());
    }

    public static void approveOwner(String ownerId) {
        User user = UserService.getUser(ownerId);
        if (user instanceof GymOwner) {
            ((GymOwner) user).setApproved(true);
        }
    }

    public static SlotMaster getSlot(String slotId) {
        return allSlots.stream().filter(s -> s.getSlotId().equals(slotId)).findFirst().orElse(null);
    }

    // Static helper to update available seats
    public static void updateAvailability(String slotId, int delta) {
        SlotMaster slot = getSlot(slotId);
        if (slot != null) {
            slot.setAvailableSeats(slot.getAvailableSeats() + delta);
        }
    }
       
    public static void approveSlot(String slotId) {
        SlotMaster slot = getSlot(slotId);
        if (slot != null) {
            slot.setApproved(true);
            System.out.println("Slot with ID " + slotId + " has been approved.");
        } else {
            System.out.println("Unable to approve slot: no slot found with ID " + slotId + ".");
        }
    }

    public static List<SlotMaster> getAllSlots() {
        return allSlots;
    }

    public static GymCenter getCenterById(String centerId) {
        if (centerId == null) {
            return null;
        }
        return centers.stream()
                .filter(c -> c.getCenterId().equals(centerId))
                .findFirst()
                .orElse(null);
    }
}
