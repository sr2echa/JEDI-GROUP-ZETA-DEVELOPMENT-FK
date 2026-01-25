package com.flipfit.client;

import com.flipfit.business.AdminInterface;
import com.flipfit.business.AdminService;
import com.flipfit.bean.GymOwner;
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
                default:
                    System.out.println("Invalid Selection.");
            }
        }
    }
}