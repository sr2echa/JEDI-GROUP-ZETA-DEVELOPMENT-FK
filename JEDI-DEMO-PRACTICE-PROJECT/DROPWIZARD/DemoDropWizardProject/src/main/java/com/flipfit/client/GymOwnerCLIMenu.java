package com.flipfit.client;

import java.util.*;

/**
 * Gym Owner CLI Menu - Makes HTTP requests for gym owner operations
 */
public class GymOwnerCLIMenu {

    public void displayMenu(Scanner sc, String ownerId) {
        boolean back = false;

        while (!back) {
            CLIUtils.clear();
            CLIUtils.printBox("GYM OWNER PORTAL", Arrays.asList(
                    "1. Add New Gym Center",
                    "2. View My Centers",
                    "3. Add Slot to Center",
                    "4. View Slots in Center",
                    "5. View Total Revenue",
                    "6. View Notifications",
                    "7. Return to Main Menu"));
            System.out.print(CLIUtils.GREEN + "Select an option: " + CLIUtils.RESET);

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
            CLIUtils.printError("No centers found for your account.");
            return;
        }

        CLIUtils.printHeader("My Gym Centers");
        List<String> headers = Arrays.asList("Center ID", "Name", "Location", "Status");
        List<List<String>> rows = new ArrayList<>();
        for (Map<String, Object> center : centers) {
            String status = (Boolean) center.get("approved") ? "ACTIVE" : "PENDING";
            rows.add(Arrays.asList(
                    String.valueOf(center.get("centerId")),
                    String.valueOf(center.get("name")),
                    String.valueOf(center.get("city")),
                    status));
        }
        CLIUtils.printTable(headers, rows);
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
            CLIUtils.printError("No slots found for this center.");
            return;
        }

        CLIUtils.printHeader("Slots for Center: " + centerId);
        List<String> headers = Arrays.asList("Slot ID", "Time Range", "Capacity", "Price");
        List<List<String>> rows = new ArrayList<>();
        for (Map<String, Object> slot : slots) {
            rows.add(Arrays.asList(
                    String.valueOf(slot.get("slotId")),
                    slot.get("startTime") + " - " + slot.get("endTime"),
                    String.valueOf(slot.get("capacity")),
                    "₹" + slot.get("price")));
        }
        CLIUtils.printTable(headers, rows);
    }

    private void viewRevenue(Scanner sc, String ownerId) throws Exception {
        Map<String, Object> response = HttpClientUtil.get("/payments/revenue/" + ownerId);

        if (response.get("success") != null && (Boolean) response.get("success")) {
            CLIUtils.clear();
            CLIUtils.printBox("FINANCIAL REPORT", Arrays.asList(
                    "Owner ID: " + ownerId,
                    " ",
                    "TOTAL REVENUE GENERATED:",
                    "₹ " + response.get("revenue"),
                    " ",
                    "Report generated on: " + new java.util.Date()));
        } else {
            CLIUtils.printError("Failed to fetch revenue: " + response.get("error"));
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
