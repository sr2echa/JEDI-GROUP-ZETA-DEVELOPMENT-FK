package com.flipfit.client;

import com.flipfit.business.GymOwnerInterface;
import com.flipfit.business.GymOwnerService;
import java.util.Scanner;

public class GymOwnerFlipFitMenu {
    GymOwnerInterface ownerService = new GymOwnerService();

    public void registerGymOwner(Scanner sc) {
        System.out.println("\n--- Registration of the GymOwner ---");
        System.out.print("Enter Username: ");
        String username = sc.next();
        System.out.print("Enter License ID: ");
        String license = sc.next();
        System.out.print("Enter Password: ");
        String password = sc.next();
        System.out.println("[SYSTEM] GymOwner " + username + " Registration Requested (Pending Approval)!");
    }

    public void displayMenu(Scanner sc, String ownerId) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Gym Owner Dashboard (" + ownerId + ") ---");
            System.out.println("1. Add New Slot");
            System.out.println("2. View My Centers");
            System.out.println("3. Back to Main Menu");
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
                    System.out.print("Enter Center ID: ");
                    ownerService.manageSlots(sc.next());
                    break;
                case 2:
                    System.out.println("[SYSTEM] Fetching your centers...");
                    // ownerService.viewCenters(ownerId);
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid Selection.");
            }
        }
    }
}