package com.flipfit.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipfit.bean.Role;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FlipFitCLIClient {
    
    private static Scanner scanner = new Scanner(System.in);
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static int currentUserId = -1;
    private static String currentUsername = "";
    private static Role currentRole = null;
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Welcome to FlipFit Application");
        System.out.println("========================================\n");
        
        boolean running = true;
        while (running) {
            running = showMainMenu();
        }
        
        scanner.close();
        System.out.println("\nThank you for using FlipFit!");
    }
    
    private static boolean showMainMenu() {
        System.out.println("\n========== Main Menu ==========");
        System.out.println("1. Login");
        System.out.println("2. Register as Customer");
        System.out.println("3. Register as Gym Owner");
        System.out.println("4. Change Password");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    registerCustomer();
                    break;
                case 3:
                    registerGymOwner();
                    break;
                case 4:
                    changePassword();
                    break;
                case 5:
                    return false;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return true;
    }
    
    private static void login() {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            
            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("username", username);
            loginRequest.put("password", password);
            
            String response = HttpClientUtil.post("/api/auth/login", loginRequest);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.has("userId")) {
                currentUserId = jsonNode.get("userId").asInt();
                currentUsername = jsonNode.get("username").asText();
                String roleStr = jsonNode.get("role").asText();
                currentRole = Role.valueOf(roleStr);
                
                System.out.println("\n✓ Login successful! Welcome, " + currentUsername);
                
                // Route to role-specific menu
                if (currentRole == Role.ADMIN) {
                    showAdminMenu();
                } else if (currentRole == Role.CUSTOMER) {
                    showCustomerMenu();
                } else if (currentRole == Role.GYM_OWNER) {
                    showGymOwnerMenu();
                }
            } else {
                System.out.println("\n✗ Login failed: " + jsonNode.get("message").asText());
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }
    
    private static void registerCustomer() {
        try {
            System.out.println("\n=== Customer Registration ===");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter phone: ");
            String phone = scanner.nextLine();
            System.out.print("Enter address: ");
            String address = scanner.nextLine();
            
            Map<String, Object> user = new HashMap<>();
            user.put("username", username);
            user.put("password", password);
            user.put("email", email);
            user.put("phoneNumber", phone);
            user.put("address", address);
            
            String response = HttpClientUtil.post("/api/auth/register/customer", user);
            JsonNode jsonNode = objectMapper.readTree(response);
            System.out.println("\n" + jsonNode.get("message").asText());
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }
    
    private static void registerGymOwner() {
        try {
            System.out.println("\n=== Gym Owner Registration ===");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter phone: ");
            String phone = scanner.nextLine();
            System.out.print("Enter PAN card: ");
            String pan = scanner.nextLine();
            System.out.print("Enter GST number: ");
            String gst = scanner.nextLine();
            System.out.print("Enter Aadhaar: ");
            String aadhaar = scanner.nextLine();
            
            Map<String, Object> user = new HashMap<>();
            user.put("username", username);
            user.put("password", password);
            user.put("email", email);
            user.put("phoneNumber", phone);
            user.put("panCard", pan);
            user.put("gstNumber", gst);
            user.put("aadhaarNumber", aadhaar);
            
            String response = HttpClientUtil.post("/api/auth/register/gymowner", user);
            JsonNode jsonNode = objectMapper.readTree(response);
            System.out.println("\n" + jsonNode.get("message").asText());
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }
    
    private static void changePassword() {
        try {
            System.out.println("\n=== Change Password ===");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter old password: ");
            String oldPassword = scanner.nextLine();
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();
            
            Map<String, String> request = new HashMap<>();
            request.put("username", username);
            request.put("oldPassword", oldPassword);
            request.put("newPassword", newPassword);
            
            String response = HttpClientUtil.put("/api/auth/change-password", request);
            JsonNode jsonNode = objectMapper.readTree(response);
            System.out.println("\n" + jsonNode.get("message").asText());
        } catch (Exception e) {
            System.out.println("Password change error: " + e.getMessage());
        }
    }
    
    private static void showAdminMenu() {
        boolean inAdminMenu = true;
        while (inAdminMenu) {
            System.out.println("\n========== Admin Menu ==========");
            System.out.println("1. View Pending Gym Owners");
            System.out.println("2. Approve Gym Owner");
            System.out.println("3. View Pending Gym Centers");
            System.out.println("4. Approve Gym Center");
            System.out.println("5. View Pending Slots");
            System.out.println("6. Approve Slot");
            System.out.println("7. View Center Revenue & History");
            System.out.println("8. View Gym Owners by Status");
            System.out.println("9. View Gym Centers by Status");
            System.out.println("10. View My Notifications");
            System.out.println("11. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        viewPendingGymOwners();
                        break;
                    case 2:
                        approveGymOwner();
                        break;
                    case 3:
                        viewPendingGymCenters();
                        break;
                    case 4:
                        approveGymCenter();
                        break;
                    case 5:
                        viewPendingSlots();
                        break;
                    case 6:
                        approveSlot();
                        break;
                    case 7:
                        viewCenterRevenue();
                        break;
                    case 8:
                        viewGymOwnersByStatus();
                        break;
                    case 9:
                        viewGymCentersByStatus();
                        break;
                    case 10:
                        viewNotifications();
                        break;
                    case 11:
                        inAdminMenu = false;
                        currentUserId = -1;
                        currentUsername = "";
                        currentRole = null;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static void viewPendingGymOwners() {
        try {
            String response = HttpClientUtil.get("/api/admin/pending-owners");
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode owners = jsonNode.get("data");
                System.out.println("\n=== Pending Gym Owners ===");
                if (owners.isArray() && owners.size() > 0) {
                    for (JsonNode owner : owners) {
                        System.out.println("ID: " + owner.get("userId").asInt() + 
                                         " | Username: " + owner.get("username").asText() +
                                         " | Email: " + owner.get("email").asText());
                    }
                } else {
                    System.out.println("No pending gym owners.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void approveGymOwner() {
        try {
            System.out.print("Enter Gym Owner ID to approve: ");
            int ownerId = Integer.parseInt(scanner.nextLine());
            
            String response = HttpClientUtil.put("/api/admin/approve-owner/" + ownerId, null);
            JsonNode jsonNode = objectMapper.readTree(response);
            System.out.println("\n" + jsonNode.get("message").asText());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewPendingGymCenters() {
        try {
            String response = HttpClientUtil.get("/api/admin/pending-centers");
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode centers = jsonNode.get("data");
                System.out.println("\n=== Pending Gym Centers ===");
                if (centers.isArray() && centers.size() > 0) {
                    for (JsonNode center : centers) {
                        System.out.println("ID: " + center.get("centerId").asInt() + 
                                         " | Name: " + center.get("centerName").asText() +
                                         " | Location: " + center.get("location").asText());
                    }
                } else {
                    System.out.println("No pending gym centers.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void approveGymCenter() {
        try {
            System.out.print("Enter Gym Center ID to approve: ");
            int centerId = Integer.parseInt(scanner.nextLine());
            
            String response = HttpClientUtil.put("/api/admin/approve-center/" + centerId, null);
            JsonNode jsonNode = objectMapper.readTree(response);
            System.out.println("\n" + jsonNode.get("message").asText());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewPendingSlots() {
        try {
            String response = HttpClientUtil.get("/api/admin/pending-slots");
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode slots = jsonNode.get("data");
                System.out.println("\n=== Pending Slots ===");
                if (slots.isArray() && slots.size() > 0) {
                    for (JsonNode slot : slots) {
                        System.out.println("ID: " + slot.get("slotId").asInt() + 
                                         " | Center ID: " + slot.get("centerId").asInt() +
                                         " | Time: " + slot.get("startTime").asText() + " - " + slot.get("endTime").asText());
                    }
                } else {
                    System.out.println("No pending slots.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void approveSlot() {
        try {
            System.out.print("Enter Slot ID to approve: ");
            int slotId = Integer.parseInt(scanner.nextLine());
            
            String response = HttpClientUtil.put("/api/admin/approve-slot/" + slotId, null);
            JsonNode jsonNode = objectMapper.readTree(response);
            System.out.println("\n" + jsonNode.get("message").asText());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewCenterRevenue() {
        try {
            System.out.print("Enter Center ID: ");
            int centerId = Integer.parseInt(scanner.nextLine());
            
            String response = HttpClientUtil.get("/api/admin/center-revenue/" + centerId);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode data = jsonNode.get("data");
                System.out.println("\n=== Center Revenue ===");
                System.out.println("Total Revenue: $" + data.get("revenue").asDouble());
                
                JsonNode history = data.get("paymentHistory");
                if (history.isArray() && history.size() > 0) {
                    System.out.println("\nPayment History:");
                    for (JsonNode payment : history) {
                        System.out.println("Payment ID: " + payment.get("paymentId").asInt() +
                                         " | Amount: $" + payment.get("amount").asDouble() +
                                         " | Date: " + payment.get("paymentDate").asText());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewGymOwnersByStatus() {
        try {
            System.out.print("Enter status (approved/pending): ");
            String status = scanner.nextLine();
            
            String response = HttpClientUtil.get("/api/admin/owners?status=" + status);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode owners = jsonNode.get("data");
                System.out.println("\n=== " + status.toUpperCase() + " Gym Owners ===");
                if (owners.isArray() && owners.size() > 0) {
                    for (JsonNode owner : owners) {
                        System.out.println("ID: " + owner.get("userId").asInt() + 
                                         " | Username: " + owner.get("username").asText() +
                                         " | Email: " + owner.get("email").asText());
                    }
                } else {
                    System.out.println("No gym owners found.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewGymCentersByStatus() {
        try {
            System.out.print("Enter status (approved/pending): ");
            String status = scanner.nextLine();
            
            String response = HttpClientUtil.get("/api/admin/centers?status=" + status);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode centers = jsonNode.get("data");
                System.out.println("\n=== " + status.toUpperCase() + " Gym Centers ===");
                if (centers.isArray() && centers.size() > 0) {
                    for (JsonNode center : centers) {
                        System.out.println("ID: " + center.get("centerId").asInt() + 
                                         " | Name: " + center.get("centerName").asText() +
                                         " | Location: " + center.get("location").asText());
                    }
                } else {
                    System.out.println("No gym centers found.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewNotifications() {
        try {
            String response = HttpClientUtil.get("/api/admin/notifications/" + currentUserId);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode notifications = jsonNode.get("data");
                System.out.println("\n=== My Notifications ===");
                if (notifications.isArray() && notifications.size() > 0) {
                    for (JsonNode notif : notifications) {
                        System.out.println("- " + notif.get("message").asText() + 
                                         " (" + notif.get("timestamp").asText() + ")");
                    }
                } else {
                    System.out.println("No notifications.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void showCustomerMenu() {
        boolean inCustomerMenu = true;
        while (inCustomerMenu) {
            System.out.println("\n========== Customer Menu ==========");
            System.out.println("1. Book Workout Slot");
            System.out.println("2. View My Plan");
            System.out.println("3. Pay for Pending Bookings");
            System.out.println("4. Cancel Booking");
            System.out.println("5. View Payment History");
            System.out.println("6. View My Notifications");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        bookWorkoutSlot();
                        break;
                    case 2:
                        viewMyPlan();
                        break;
                    case 3:
                        payForPendingBookings();
                        break;
                    case 4:
                        cancelBooking();
                        break;
                    case 5:
                        viewPaymentHistory();
                        break;
                    case 6:
                        viewCustomerNotifications();
                        break;
                    case 7:
                        inCustomerMenu = false;
                        currentUserId = -1;
                        currentUsername = "";
                        currentRole = null;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static void bookWorkoutSlot() {
        try {
            // First, browse centers
            System.out.print("Enter city (or press Enter for all): ");
            String city = scanner.nextLine();
            
            String endpoint = city.isEmpty() ? "/api/customer/centers" : "/api/customer/centers?city=" + city;
            String response = HttpClientUtil.get(endpoint);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode centers = jsonNode.get("data");
                System.out.println("\n=== Available Gym Centers ===");
                if (centers.isArray() && centers.size() > 0) {
                    for (JsonNode center : centers) {
                        System.out.println("ID: " + center.get("centerId").asInt() + 
                                         " | Name: " + center.get("centerName").asText() +
                                         " | Location: " + center.get("location").asText());
                    }
                    
                    System.out.print("\nEnter Center ID to view slots: ");
                    int centerId = Integer.parseInt(scanner.nextLine());
                    
                    // Get slots for the center
                    response = HttpClientUtil.get("/api/customer/slots/" + centerId);
                    jsonNode = objectMapper.readTree(response);
                    
                    if (jsonNode.get("success").asBoolean()) {
                        JsonNode slots = jsonNode.get("data");
                        System.out.println("\n=== Available Slots ===");
                        if (slots.isArray() && slots.size() > 0) {
                            for (JsonNode slot : slots) {
                                System.out.println("ID: " + slot.get("slotId").asInt() + 
                                                 " | Time: " + slot.get("startTime").asText() + " - " + slot.get("endTime").asText() +
                                                 " | Capacity: " + slot.get("availableSeats").asInt());
                            }
                            
                            System.out.print("\nEnter Slot ID to book: ");
                            int slotId = Integer.parseInt(scanner.nextLine());
                            System.out.print("Enter booking date (YYYY-MM-DD): ");
                            String bookingDate = scanner.nextLine();
                            
                            Map<String, Object> booking = new HashMap<>();
                            booking.put("userId", currentUserId);
                            booking.put("slotId", slotId);
                            booking.put("bookingDate", bookingDate);
                            
                            response = HttpClientUtil.post("/api/customer/book-slot", booking);
                            jsonNode = objectMapper.readTree(response);
                            System.out.println("\n" + jsonNode.get("message").asText());
                        } else {
                            System.out.println("No slots available.");
                        }
                    }
                } else {
                    System.out.println("No centers found.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewMyPlan() {
        try {
            String response = HttpClientUtil.get("/api/customer/plan/" + currentUserId);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode plan = jsonNode.get("data");
                System.out.println("\n=== My Workout Plan ===");
                if (plan.isArray() && plan.size() > 0) {
                    for (JsonNode booking : plan) {
                        System.out.println("Booking ID: " + booking.get("bookingId").asInt() +
                                         " | Center: " + booking.get("centerName").asText() +
                                         " | Date: " + booking.get("bookingDate").asText() +
                                         " | Time: " + booking.get("startTime").asText() + " - " + booking.get("endTime").asText() +
                                         " | Status: " + booking.get("status").asText());
                    }
                } else {
                    System.out.println("No bookings in your plan.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void payForPendingBookings() {
        try {
            String response = HttpClientUtil.get("/api/customer/pending-bookings/" + currentUserId);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode bookings = jsonNode.get("data");
                System.out.println("\n=== Pending Bookings ===");
                if (bookings.isArray() && bookings.size() > 0) {
                    for (JsonNode booking : bookings) {
                        System.out.println("Booking ID: " + booking.get("bookingId").asInt() +
                                         " | Center: " + booking.get("centerName").asText() +
                                         " | Date: " + booking.get("bookingDate").asText());
                    }
                    
                    System.out.print("\nEnter Booking ID to pay: ");
                    int bookingId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter amount: ");
                    double amount = Double.parseDouble(scanner.nextLine());
                    
                    response = HttpClientUtil.post("/api/customer/pay-booking/" + bookingId + "?amount=" + amount, null);
                    jsonNode = objectMapper.readTree(response);
                    System.out.println("\n" + jsonNode.get("message").asText());
                } else {
                    System.out.println("No pending bookings.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void cancelBooking() {
        try {
            System.out.print("Enter Booking ID to cancel: ");
            int bookingId = Integer.parseInt(scanner.nextLine());
            
            String response = HttpClientUtil.delete("/api/customer/cancel-booking/" + bookingId);
            JsonNode jsonNode = objectMapper.readTree(response);
            System.out.println("\n" + jsonNode.get("message").asText());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewPaymentHistory() {
        try {
            String response = HttpClientUtil.get("/api/customer/payment-history/" + currentUserId);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode history = jsonNode.get("data");
                System.out.println("\n=== Payment History ===");
                if (history.isArray() && history.size() > 0) {
                    for (JsonNode payment : history) {
                        System.out.println("Payment ID: " + payment.get("paymentId").asInt() +
                                         " | Amount: $" + payment.get("amount").asDouble() +
                                         " | Date: " + payment.get("paymentDate").asText() +
                                         " | Status: " + payment.get("status").asText());
                    }
                } else {
                    System.out.println("No payment history.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewCustomerNotifications() {
        try {
            String response = HttpClientUtil.get("/api/customer/notifications/" + currentUserId);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode notifications = jsonNode.get("data");
                System.out.println("\n=== My Notifications ===");
                if (notifications.isArray() && notifications.size() > 0) {
                    for (JsonNode notif : notifications) {
                        System.out.println("- " + notif.get("message").asText() + 
                                         " (" + notif.get("timestamp").asText() + ")");
                    }
                } else {
                    System.out.println("No notifications.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void showGymOwnerMenu() {
        boolean inOwnerMenu = true;
        while (inOwnerMenu) {
            System.out.println("\n========== Gym Owner Menu ==========");
            System.out.println("1. Add Gym Center");
            System.out.println("2. View My Centers");
            System.out.println("3. Add Slot to Center");
            System.out.println("4. View Slots in Center");
            System.out.println("5. Update Slot Capacity");
            System.out.println("6. View Center Revenue & History");
            System.out.println("7. View My Notifications");
            System.out.println("8. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        addGymCenter();
                        break;
                    case 2:
                        viewMyCenters();
                        break;
                    case 3:
                        addSlotToCenter();
                        break;
                    case 4:
                        viewSlotsInCenter();
                        break;
                    case 5:
                        updateSlotCapacity();
                        break;
                    case 6:
                        viewOwnerCenterRevenue();
                        break;
                    case 7:
                        viewOwnerNotifications();
                        break;
                    case 8:
                        inOwnerMenu = false;
                        currentUserId = -1;
                        currentUsername = "";
                        currentRole = null;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static void addGymCenter() {
        try {
            System.out.println("\n=== Add Gym Center ===");
            System.out.print("Enter center name: ");
            String name = scanner.nextLine();
            System.out.print("Enter location/address: ");
            String location = scanner.nextLine();
            System.out.print("Enter city: ");
            String city = scanner.nextLine();
            
            Map<String, Object> center = new HashMap<>();
            center.put("ownerId", currentUserId);
            center.put("centerName", name);
            center.put("location", location);
            center.put("city", city);
            
            String response = HttpClientUtil.post("/api/gymowner/add-center", center);
            JsonNode jsonNode = objectMapper.readTree(response);
            System.out.println("\n" + jsonNode.get("message").asText());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewMyCenters() {
        try {
            String response = HttpClientUtil.get("/api/gymowner/centers/" + currentUserId);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode centers = jsonNode.get("data");
                System.out.println("\n=== My Gym Centers ===");
                if (centers.isArray() && centers.size() > 0) {
                    for (JsonNode center : centers) {
                        System.out.println("ID: " + center.get("centerId").asInt() + 
                                         " | Name: " + center.get("centerName").asText() +
                                         " | Location: " + center.get("location").asText() +
                                         " | Status: " + (center.get("isApproved").asBoolean() ? "Approved" : "Pending"));
                    }
                } else {
                    System.out.println("No centers registered yet.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void addSlotToCenter() {
        try {
            System.out.print("Enter Center ID: ");
            int centerId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter start time (HH:MM): ");
            String startTime = scanner.nextLine();
            System.out.print("Enter end time (HH:MM): ");
            String endTime = scanner.nextLine();
            System.out.print("Enter capacity: ");
            int capacity = Integer.parseInt(scanner.nextLine());
            
            Map<String, Object> slot = new HashMap<>();
            slot.put("centerId", centerId);
            slot.put("startTime", startTime);
            slot.put("endTime", endTime);
            slot.put("totalSeats", capacity);
            slot.put("availableSeats", capacity);
            
            String response = HttpClientUtil.post("/api/gymowner/add-slot", slot);
            JsonNode jsonNode = objectMapper.readTree(response);
            System.out.println("\n" + jsonNode.get("message").asText());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewSlotsInCenter() {
        try {
            System.out.print("Enter Center ID: ");
            int centerId = Integer.parseInt(scanner.nextLine());
            
            String response = HttpClientUtil.get("/api/gymowner/slots/" + centerId);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode slots = jsonNode.get("data");
                System.out.println("\n=== Slots in Center ===");
                if (slots.isArray() && slots.size() > 0) {
                    for (JsonNode slot : slots) {
                        System.out.println("Slot ID: " + slot.get("slotId").asInt() + 
                                         " | Time: " + slot.get("startTime").asText() + " - " + slot.get("endTime").asText() +
                                         " | Capacity: " + slot.get("availableSeats").asInt() + "/" + slot.get("totalSeats").asInt() +
                                         " | Status: " + (slot.get("isApproved").asBoolean() ? "Approved" : "Pending"));
                    }
                } else {
                    System.out.println("No slots in this center.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void updateSlotCapacity() {
        try {
            System.out.print("Enter Slot ID: ");
            int slotId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter new capacity: ");
            int capacity = Integer.parseInt(scanner.nextLine());
            
            String response = HttpClientUtil.put("/api/gymowner/update-slot-capacity/" + slotId + "?capacity=" + capacity, null);
            JsonNode jsonNode = objectMapper.readTree(response);
            System.out.println("\n" + jsonNode.get("message").asText());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewOwnerCenterRevenue() {
        try {
            System.out.print("Enter Center ID: ");
            int centerId = Integer.parseInt(scanner.nextLine());
            
            String response = HttpClientUtil.get("/api/gymowner/center-revenue/" + centerId);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode data = jsonNode.get("data");
                System.out.println("\n=== Center Revenue ===");
                System.out.println("Total Revenue: $" + data.get("revenue").asDouble());
                
                JsonNode history = data.get("paymentHistory");
                if (history.isArray() && history.size() > 0) {
                    System.out.println("\nPayment History:");
                    for (JsonNode payment : history) {
                        System.out.println("Payment ID: " + payment.get("paymentId").asInt() +
                                         " | Amount: $" + payment.get("amount").asDouble() +
                                         " | Date: " + payment.get("paymentDate").asText());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewOwnerNotifications() {
        try {
            String response = HttpClientUtil.get("/api/gymowner/notifications/" + currentUserId);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.get("success").asBoolean()) {
                JsonNode notifications = jsonNode.get("data");
                System.out.println("\n=== My Notifications ===");
                if (notifications.isArray() && notifications.size() > 0) {
                    for (JsonNode notif : notifications) {
                        System.out.println("- " + notif.get("message").asText() + 
                                         " (" + notif.get("timestamp").asText() + ")");
                    }
                } else {
                    System.out.println("No notifications.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
