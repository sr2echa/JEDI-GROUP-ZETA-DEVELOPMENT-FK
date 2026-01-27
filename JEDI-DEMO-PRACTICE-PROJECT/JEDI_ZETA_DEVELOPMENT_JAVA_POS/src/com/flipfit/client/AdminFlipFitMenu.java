package com.flipfit.client;

import com.flipfit.business.AdminInterface;
import com.flipfit.business.AdminService;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;

import java.util.List;
import java.util.Scanner;

public class AdminFlipFitMenu {
    AdminInterface adminService = new AdminService();

    public void displayMenu(Scanner sc) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. View Pending Gym Owners");
            System.out.println("2. Approve Gym Owner");
            System.out.println("3. View Pending Centers");
            System.out.println("4. Back to Main Menu");
            System.out.println("5. View Pending Slots");
            System.out.println("6. Approve Slot");
            System.out.print("Choice: ");

            int choice = 0;
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            } else {
                sc.next();
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1:
                    List<GymOwner> pending = adminService.viewPendingGymOwners();
                    if (pending.isEmpty()) {
                        System.out.println("No pending gym owners.");
                    } else {
                        System.out.println("Pending Gym Owners:");
                        pending.forEach(o -> System.out.println(
                                " - ID: " + o.getUserId() + ", Name: " + o.getName() + ", PAN: " + o.getPanNumber()));
                    }
                    break;
                case 2:
                    System.out.print("Enter Owner ID to approve: ");
                    adminService.approveGymOwner(sc.next());
                    break;
                case 3:
                    System.out.println("[SYSTEM] Fetching pending centers...");
                    // Logic for pending centers
                    break;
                case 4:
                    back = true;
                    break;
                case 5: // View Pending Slots
                    List<SlotMaster> pendingSlots = adminService.viewPendingSlots();
                    if (pendingSlots.isEmpty()) {
                        System.out.println("No pending slots.");
                    } else {
                        pendingSlots.forEach(s -> System.out.println("Slot ID: " + s.getSlotId() + " | Center: " + s.getCenterId()));
                    }
                    break;
                case 6: // Approve Slot
                    System.out.print("Enter Slot ID to approve: ");
                    adminService.approveSlot(sc.next());
                    break;
                default:
                    System.out.println("Invalid Selection.");
            }
        }
    }
}