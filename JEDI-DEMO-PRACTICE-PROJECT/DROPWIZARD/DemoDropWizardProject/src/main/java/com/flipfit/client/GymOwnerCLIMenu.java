package com.flipfit.client;

import java.util.*;

/**
 * Gym Owner CLI Menu - Makes HTTP requests for gym owner operations
 */
public class GymOwnerCLIMenu {

    public void displayMenu(Scanner sc, String ownerId) {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Gym Owner Dashboard (" + ownerId + ") ---");
            System.out.println("1. Add Gym Center");
            System.out.println("2. View My Centers");
            System.out.println("3. Add Slot to Center");
            System.out.println("4. View Slots in Center");
            System.out.println("5. View Center Revenue");
            System.out.println("6. View Notifications");
            System.out.println("7. Back to Main Menu");
            System.out.print("Choice: ");

            int choice = getIntInput(sc);

            try {
                switch (choice) {
                    case 1:
                        addGymCenter(sc, ownerId);
                        break;
                    case 2:
                        viewMyCenters(sc, ownerId);
                        break;
                    case 3:
                        addSlot(sc);
                        break;
                    case 4:
                        viewSlots(sc);
                        break;
                    case 5:
                        viewRevenue(sc, ownerId);
                        break;
                    case 6:
                        viewNotifications(sc, ownerId);
                        break;
                    case 7:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("\n✗ Error: " + e.getMessage());
            }
        }
    }

    private void addGymCenter(Scanner sc, String ownerId) throws Exception {
        System.out.println("\n--- Add Gym Center ---");
        System.out.print("Center Name: ");
        String name = sc.nextLine();
        System.out.print("Location: ");
        String location = sc.nextLine();

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("location", location);

        Map<String, Object> response = HttpClientUtil.post("/owners/" + ownerId + "/centers", data);

        if (response.get("success") != null && (Boolean) response.get("success")) {
            System.out.println("\n✓ " + response.get("message"));
        } else {
            System.out.println("\n✗ Failed: " + response.get("error"));
        }
    }

    private void viewMyCenters(Scanner sc, String ownerId) throws Exception {
        Map<String, Object> response = HttpClientUtil.get("/owners/" + ownerId + "/centers");

        List<Map<String, Object>> centers = (List<Map<String, Object>>) response.get("centers");

        if (centers == null || centers.isEmpty()) {
            System.out.println("\n[INFO] No centers found.");
            return;
        }

        System.out.println("\n--- My Gym Centers ---");
        for (Map<String, Object> center : centers) {
            String status = (Boolean) center.get("approved") ? "ACTIVE" : "PENDING";
            System.out.println(" • ID: " + center.get("centerId") +
                    " | Name: " + center.get("name") +
                    " | Status: [" + status + "]");
        }
    }

    private void addSlot(Scanner sc) throws Exception {
        System.out.println("\n--- Add Slot to Center ---");
        System.out.print("Center ID: ");
        String centerId = sc.nextLine();
        System.out.print("Start Time (HH:MM): ");
        String startTime = sc.nextLine();
        System.out.print("End Time (HH:MM): ");
        String endTime = sc.nextLine();
        System.out.print("Capacity: ");
        int capacity = sc.nextInt();
        System.out.print("Price (₹): ");
        double price = sc.nextDouble();
        sc.nextLine();

        Map<String, Object> data = new HashMap<>();
        data.put("startTime", startTime);
        data.put("endTime", endTime);
        data.put("capacity", capacity);
        data.put("price", price);

        Map<String, Object> response = HttpClientUtil.post("/owners/centers/" + centerId + "/slots", data);

        if (response.get("success") != null && (Boolean) response.get("success")) {
            System.out.println("\n✓ " + response.get("message"));
        } else {
            System.out.println("\n✗ Failed: " + response.get("error"));
        }
    }

    private void viewSlots(Scanner sc) throws Exception {
        System.out.print("\nEnter Center ID: ");
        String centerId = sc.nextLine();

        Map<String, Object> response = HttpClientUtil.get("/slots/center/" + centerId);

        List<Map<String, Object>> slots = (List<Map<String, Object>>) response.get("slots");

        if (slots == null || slots.isEmpty()) {
            System.out.println("\n[INFO] No slots found for this center.");
            return;
        }

        System.out.println("\n--- Slots for Center " + centerId + " ---");
        for (Map<String, Object> slot : slots) {
            System.out.println(" • Slot ID: " + slot.get("slotId") +
                    " | Time: " + slot.get("startTime") + " - " + slot.get("endTime") +
                    " | Capacity: " + slot.get("capacity") +
                    " | Price: ₹" + slot.get("price"));
        }
    }

    private void viewRevenue(Scanner sc, String ownerId) throws Exception {
        System.out.print("\nEnter Center ID to view revenue: ");
        String centerId = sc.nextLine();

        Map<String, Object> response = HttpClientUtil.get("/payments/revenue/" + centerId + "?userId=" + ownerId);

        if (response.get("success") != null && (Boolean) response.get("success")) {
            System.out.println("\n✓ " + response.get("message"));
        } else {
            System.out.println("\n✗ Failed: " + response.get("error"));
        }
    }

    private void viewNotifications(Scanner sc, String userId) throws Exception {
        Map<String, Object> response = HttpClientUtil.get("/notifications/" + userId);

        List<Map<String, Object>> notifications = (List<Map<String, Object>>) response.get("notifications");

        if (notifications == null || notifications.isEmpty()) {
            System.out.println("\n[INFO] No notifications found.");
            return;
        }

        System.out.println("\n--- Your Notifications ---");
        for (Map<String, Object> notification : notifications) {
            System.out.println(" • [" + notification.get("timestamp") + "] " + notification.get("message"));
        }
    }

    private int getIntInput(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Invalid input. Enter a number: ");
        }
        int num = sc.nextInt();
        sc.nextLine();
        return num;
    }
}
