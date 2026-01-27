package com.flipfit.client;

import com.flipfit.business.AdminInterface;
import com.flipfit.business.AdminService;
import com.flipfit.business.PaymentService;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.UserService;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;
import com.flipfit.bean.User;

import java.util.List;
import java.util.Scanner;

public class AdminFlipFitMenu {
    AdminInterface adminService = new AdminService();
    PaymentService paymentService = new PaymentService();

    public void displayMenu(Scanner sc) {
        displayMenu(sc, null);
    }

    public void displayMenu(Scanner sc, User adminUser) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. View Pending Gym Owners");
            System.out.println("2. Approve Gym Owner");
            System.out.println("3. View Pending Centers");
            System.out.println("4. Back to Main Menu");
            System.out.println("5. View Pending Slots");
            System.out.println("6. Approve Slot");
            System.out.println("7. View Center Revenue & History");
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
                        System.out.println("Pending Slots:");
                        pendingSlots.forEach(s -> {
                            GymCenter center = GymOwnerService.getCenter(s.getCenterId());
                            String ownerName = "Unknown";
                            if (center != null) {
                                User owner = UserService.getUser(center.getOwnerId());
                                if (owner != null) {
                                    ownerName = owner.getName();
                                }
                            }
                            System.out.println(" - Slot ID: " + s.getSlotId() + 
                                    ", Center: " + s.getCenterId() + 
                                    ", Time: " + s.getStartTime() + "-" + s.getEndTime() + 
                                    ", Capacity: " + s.getCapacity() + 
                                    ", Owner: " + ownerName);
                        });
                    }
                    break;
                case 6: // Approve Slot
                    System.out.print("Enter Slot ID to approve: ");
                    adminService.approveSlot(sc.next());
                    break;
                case 7: // View Center Revenue
                    System.out.print("Enter Center ID to view revenue: ");
                    String centerId = sc.next();
                    // Admins can view any center's revenue
                    if (adminUser != null) {
                        paymentService.displayGymRevenue(centerId, adminUser.getUserId(), adminUser);
                    } else {
                        System.out.println("[ERROR] Admin user information not available.");
                    }
                    break;
                default:
                    System.out.println("Invalid Selection.");
            }
        }
    }
}