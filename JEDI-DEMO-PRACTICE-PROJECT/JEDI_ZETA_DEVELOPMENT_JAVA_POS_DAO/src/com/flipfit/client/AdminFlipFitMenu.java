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

/**
 * The Class AdminFlipFitMenu.
 *
 * @author Zeta
 * @ClassName "AdminFlipFitMenu"
 */
public class AdminFlipFitMenu {

    /** The admin service. */
    AdminInterface adminService = new AdminService();

    /** The payment service. */
    PaymentService paymentService = new PaymentService();

    /** The user service. */
    UserService userService = new UserService();

    /**
     * Display menu.
     *
     * @param sc the sc
     */
    public void displayMenu(Scanner sc) {
        displayMenu(sc, null);
    }

    /**
     * Display menu.
     *
     * @param sc        the sc
     * @param adminUser the admin user
     */
    public void displayMenu(Scanner sc, User adminUser) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. View Pending Gym Owners");
            System.out.println("2. Approve Gym Owner");
            System.out.println("3. View Pending Centers");
            System.out.println("4. Approve Gym Center");
            System.out.println("5. View Pending Slots");
            System.out.println("6. Approve Slot");
            System.out.println("7. View Center Revenue & History");
            System.out.println("8. View Gym Owners (Filtered by Status)");
            System.out.println("9. View Gym Centers (Filtered by Status)");
            System.out.println("10. Back to Main Menu");
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
                    List<GymCenter> pendingCenters = adminService.viewPendingGymCenters();
                    if (pendingCenters.isEmpty()) {
                        System.out.println("No pending gym centers.");
                    } else {
                        System.out.println("Pending Gym Centers:");
                        pendingCenters.forEach(c -> {
                            User owner = userService.getUser(c.getOwnerId());
                            String ownerName = owner != null ? owner.getName() : "Unknown";
                            System.out.println(" - Center ID: " + c.getCenterId() +
                                    ", Name: " + c.getName() +
                                    ", City: " + c.getCity() +
                                    ", Owner: " + ownerName + " (" + c.getOwnerId() + ")");
                        });
                    }
                    break;
                case 4:
                    System.out.print("Enter Center ID to approve: ");
                    adminService.approveGymCenter(sc.next());
                    break;
                case 5:
                    List<SlotMaster> pendingSlots = adminService.viewPendingSlots();
                    if (pendingSlots.isEmpty()) {
                        System.out.println("No pending slots.");
                    } else {
                        System.out.println("Pending Slots:");
                        pendingSlots.forEach(s -> {
                            GymCenter center = GymOwnerService.getCenterById(s.getCenterId());
                            String ownerName = "Unknown";
                            if (center != null) {
                                User owner = userService.getUser(center.getOwnerId());
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
                case 6:
                    System.out.print("Enter Slot ID to approve: ");
                    adminService.approveSlot(sc.next());
                    break;
                case 7:
                    System.out.print("Enter Center ID to view revenue: ");
                    String centerId = sc.next();
                    if (adminUser != null) {
                        paymentService.displayGymRevenue(centerId, adminUser.getUserId(), adminUser);
                    } else {
                        System.out.println("[ERROR] Admin user information not available.");
                    }
                    break;
                case 8:
                    System.out.print("View (1) Approved or (2) Pending Owners? ");
                    boolean ownerStatus = sc.nextInt() == 1;
                    List<GymOwner> owners = adminService.viewGymOwnersByStatus(ownerStatus);
                    System.out.println(ownerStatus ? "Approved Gym Owners:" : "Pending Gym Owners:");
                    owners.forEach(o -> System.out.println(
                            " - ID: " + o.getUserId() + ", Name: " + o.getName() + ", Approved: " + o.isApproved()));
                    break;
                case 9:
                    System.out.print("View (1) Approved or (2) Pending Centers? ");
                    boolean centerStatus = sc.nextInt() == 1;
                    List<GymCenter> centers = adminService.viewGymCentersByStatus(centerStatus);
                    System.out.println(centerStatus ? "Approved Gym Centers:" : "Pending Gym Centers:");
                    centers.forEach(c -> System.out.println(
                            " - ID: " + c.getCenterId() + ", Name: " + c.getName() + ", Approved: " + c.isApproved()));
                    break;
                case 10:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid Selection.");
            }
        }
    }
}