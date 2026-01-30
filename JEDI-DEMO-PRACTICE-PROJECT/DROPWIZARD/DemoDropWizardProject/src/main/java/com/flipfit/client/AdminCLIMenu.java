package com.flipfit.client;

import java.util.*;

/**
 * Admin CLI Menu - Makes HTTP requests for admin operations
 */
public class AdminCLIMenu {

    public void displayMenu(Scanner sc, String adminId) {
        boolean back = false;

        while (!back) {
            CLIUtils.clear();
            CLIUtils.printBox("ADMIN DASHBOARD", Arrays.asList(
                    "1. View Pending Gym Owners",
                    "2. Approve Gym Owner",
                    "3. View Pending Centers",
                    "4. Approve Gym Center",
                    "5. View Pending Slots",
                    "6. Approve Slot",
                    "7. View All Owners (by status)",
                    "8. View All Centers (by status)",
                    "9. View Notifications",
                    "10. Back to Main Menu"));
            System.out.print(CLIUtils.GREEN + "Select an option: " + CLIUtils.RESET);

            int choice = getIntInput(sc);

            try {
                switch (choice) {
                    case 1:
                        viewPendingOwners();
                        break;
                    case 2:
                        approveOwner(sc);
                        break;
                    case 3:
                        viewPendingCenters();
                        break;
                    case 4:
                        approveCenter(sc);
                        break;
                    case 5:
                        viewPendingSlots();
                        break;
                    case 6:
                        approveSlot(sc);
                        break;
                    case 7:
                        viewOwnersByStatus(sc);
                        break;
                    case 8:
                        viewCentersByStatus(sc);
                        break;
                    case 9:
                        viewNotifications(sc, adminId);
                        break;
                    case 10:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("\n✗ Error: " + e.getMessage());
            }

            if (!back) {
                System.out.println("Press Enter to continue...");
                sc.nextLine();
            }
        }
    }

    private void viewPendingOwners() throws Exception {
        Map<String, Object> response = HttpClientUtil.get("/admin/owners/pending");

        List<Map<String, Object>> owners = (List<Map<String, Object>>) response.get("pendingOwners");

        if (owners == null || owners.isEmpty()) {
            CLIUtils.printError("No pending gym owners found.");
            return;
        }

        CLIUtils.printHeader("Pending Gym Owners");
        List<String> headers = Arrays.asList("Owner ID", "Name", "PAN Number");
        List<List<String>> rows = new ArrayList<>();
        for (Map<String, Object> owner : owners) {
            rows.add(Arrays.asList(
                    String.valueOf(owner.get("userId")),
                    String.valueOf(owner.get("name")),
                    String.valueOf(owner.get("panNumber"))));
        }
        CLIUtils.printTable(headers, rows);
    }

    private void approveOwner(Scanner sc) throws Exception {
        System.out.print("\nEnter Owner ID to approve: ");
        String ownerId = sc.nextLine();

        Map<String, Object> response = HttpClientUtil.put("/admin/owners/" + ownerId + "/approve", new HashMap<>());

        if (response.get("success") != null && (Boolean) response.get("success")) {
            CLIUtils.printSuccess(String.valueOf(response.get("message")));
        } else {
            CLIUtils.printError("Approval failed: " + response.get("error"));
        }
    }

    private void viewPendingCenters() throws Exception {
        Map<String, Object> response = HttpClientUtil.get("/admin/centers/pending");

        List<Map<String, Object>> centers = (List<Map<String, Object>>) response.get("pendingCenters");

        if (centers == null || centers.isEmpty()) {
            CLIUtils.printError("No pending gym centers found.");
            return;
        }

        CLIUtils.printHeader("Pending Gym Centers");
        List<String> headers = Arrays.asList("Center ID", "Name", "City", "Owner ID");
        List<List<String>> rows = new ArrayList<>();
        for (Map<String, Object> center : centers) {
            rows.add(Arrays.asList(
                    String.valueOf(center.get("centerId")),
                    String.valueOf(center.get("name")),
                    String.valueOf(center.get("city")),
                    String.valueOf(center.get("ownerId"))));
        }
        CLIUtils.printTable(headers, rows);
    }

    private void approveCenter(Scanner sc) throws Exception {
        System.out.print("\nEnter Center ID to approve: ");
        String centerId = sc.nextLine();

        Map<String, Object> response = HttpClientUtil.put("/admin/centers/" + centerId + "/approve", new HashMap<>());

        if (response.get("success") != null && (Boolean) response.get("success")) {
            System.out.println("\n✓ " + response.get("message"));
        } else {
            System.out.println("\n✗ Approval failed: " + response.get("error"));
        }
    }

    private void viewPendingSlots() throws Exception {
        Map<String, Object> response = HttpClientUtil.get("/admin/slots/pending");

        List<Map<String, Object>> slots = (List<Map<String, Object>>) response.get("pendingSlots");

        if (slots == null || slots.isEmpty()) {
            System.out.println("\n[INFO] No pending slots.");
            return;
        }

        System.out.println("\n--- Pending Slots ---");
        for (Map<String, Object> slot : slots) {
            System.out.println(" • Slot ID: " + slot.get("slotId") +
                    " | Center: " + slot.get("centerId") +
                    " | Time: " + slot.get("startTime") + " - " + slot.get("endTime") +
                    " | Capacity: " + slot.get("capacity"));
        }
    }

    private void approveSlot(Scanner sc) throws Exception {
        System.out.print("\nEnter Slot ID to approve: ");
        String slotId = sc.nextLine();

        Map<String, Object> response = HttpClientUtil.put("/admin/slots/" + slotId + "/approve", new HashMap<>());

        if (response.get("success") != null && (Boolean) response.get("success")) {
            System.out.println("\n✓ " + response.get("message"));
        } else {
            System.out.println("\n✗ Approval failed: " + response.get("error"));
        }
    }

    private void viewOwnersByStatus(Scanner sc) throws Exception {
        System.out.print("\nView (1) Approved or (2) Pending?: ");
        int choice = sc.nextInt();
        sc.nextLine();
        boolean approved = (choice == 1);

        Map<String, Object> response = HttpClientUtil.get("/admin/owners?approved=" + approved);

        List<Map<String, Object>> owners = (List<Map<String, Object>>) response.get("owners");

        if (owners == null || owners.isEmpty()) {
            System.out.println("\n[INFO] No owners found.");
            return;
        }

        System.out.println("\n--- " + (approved ? "Approved" : "Pending") + " Gym Owners ---");
        for (Map<String, Object> owner : owners) {
            System.out.println(" • ID: " + owner.get("userId") +
                    " | Name: " + owner.get("name"));
        }
    }

    private void viewCentersByStatus(Scanner sc) throws Exception {
        System.out.print("\nView (1) Approved or (2) Pending?: ");
        int choice = sc.nextInt();
        sc.nextLine();
        boolean approved = (choice == 1);

        Map<String, Object> response = HttpClientUtil.get("/admin/centers?approved=" + approved);

        List<Map<String, Object>> centers = (List<Map<String, Object>>) response.get("centers");

        if (centers == null || centers.isEmpty()) {
            System.out.println("\n[INFO] No centers found.");
            return;
        }

        System.out.println("\n--- " + (approved ? "Approved" : "Pending") + " Gym Centers ---");
        for (Map<String, Object> center : centers) {
            System.out.println(" • ID: " + center.get("centerId") +
                    " | Name: " + center.get("name"));
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
