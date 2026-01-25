package com.flipfit.client;

import com.flipfit.business.*;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.SlotMaster;
import java.util.List;
import java.util.Scanner;

public class CustomerFlipFitMenu {
    CustomerInterface customerService = new CustomerService();
    GymOwnerInterface ownerService = new GymOwnerService();
    UserInterface userService = new UserService();

    public void registerCustomer(Scanner sc) {
        System.out.println("\n--- Registration ---");
        System.out.print("Username: ");
        String username = sc.next();
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Password: ");
        String password = sc.next();

        if (userService.register(username, password, email, 2)) {
            System.out.println("[SYSTEM] Customer " + username + " Registration Successful!");
        }
    }

    public void displayMenu(Scanner sc, String userId) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Customer Dashboard (" + userId + ") ---");
            System.out.println("1. Book Workout Slot (Browse Centers)");
            System.out.println("2. View My Plan");
            System.out.println("3. Cancel Booking");
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
                    browseAndBook(sc, userId);
                    break;
                case 2:
                    customerService.getCustomerPlan(userId);
                    break;
                case 3:
                    System.out.print("Enter Booking ID: ");
                    customerService.cancelWorkout(sc.next());
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid Selection.");
            }
        }
    }

    private void browseAndBook(Scanner sc, String userId) {
        // 1. Show all approved centers
        List<GymCenter> centers = ownerService.getAllCenters();
        if (centers.isEmpty()) {
            System.out.println("No gym centers currently available.");
            return;
        }

        System.out.println("\nAvailable Gym Centers:");
        centers.forEach(c -> System.out
                .println(" - ID: " + c.getCenterId() + " | Name: " + c.getName() + " | City: " + c.getCity()));

        System.out.print("\nEnter Center ID to view slots: ");
        String centerId = sc.next();

        // 2. Show slots for that center
        List<SlotMaster> slots = ownerService.viewSlots(centerId);
        if (slots.isEmpty()) {
            System.out.println("No slots found for this center.");
            return;
        }

        System.out.println("\nAvailable Slots for " + centerId + ":");
        slots.forEach(s -> System.out.println(" - Slot ID: " + s.getSlotId() +
                " | Timing: " + s.getStartTime() + " - " + s.getEndTime() +
                " | Seats Left: " + s.getAvailableSeats() + "/" + s.getCapacity()));

        System.out.print("\nEnter Slot ID to book: ");
        String slotId = sc.next();

        // 3. Confirm selection and book
        customerService.bookWorkout(userId, slotId);
    }
}