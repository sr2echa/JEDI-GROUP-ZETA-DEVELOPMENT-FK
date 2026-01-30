package com.flipfit.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;

/**
 * FlipFit CLI Client Application
 * Makes HTTP requests to the DropWizard REST API running on localhost:8080
 */
public class FlipFitCLIClient {

    // Colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        System.out.println("\n" + "=".repeat(60));
        System.out.println("FlipFit CLI Client - Connected to http://localhost:8080");
        System.out.println("=".repeat(60));

        while (!exit) {
            clearScreen();
            System.out.println(ANSI_CYAN + "\n" + "=".repeat(60) + ANSI_RESET);
            System.out.println(ANSI_YELLOW + "      FlipFit Application - Main Menu" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "=".repeat(60) + ANSI_RESET);
            System.out.println("1. Login");
            System.out.println("2. Register as Customer");
            System.out.println("3. Register as Gym Owner");
            System.out.println("4. Change Password");
            System.out.println("5. Exit");
            System.out.print(ANSI_GREEN + "Choice: " + ANSI_RESET);

            int choice = getIntInput(sc);

            switch (choice) {
                case 1:
                    handleLogin(sc);
                    break;
                case 2:
                    registerCustomer(sc);
                    break;
                case 3:
                    registerGymOwner(sc);
                    break;
                case 4:
                    changePassword(sc);
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting FlipFit CLI. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }

    private static void handleLogin(Scanner sc) {
        try {
            System.out.println("\n--- Login ---");
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            Map<String, Object> credentials = new HashMap<>();
            credentials.put("username", username);
            credentials.put("password", password);

            Map<String, Object> response = HttpClientUtil.post("/users/login", credentials);

            if (response.get("success") != null && (Boolean) response.get("success")) {
                // Set Auth Token
                HttpClientUtil.setAuthToken(username + "+admin");

                clearScreen();
                System.out.println(ANSI_CYAN + "\n" + "=".repeat(60) + ANSI_RESET);
                String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                System.out.printf(ANSI_YELLOW + "Welcome %s" + ANSI_RESET + "%" + (52 - username.length()) + "s%n",
                        username, time);
                System.out.println(ANSI_CYAN + "=".repeat(60) + ANSI_RESET);

                System.out.println("\n" + ANSI_GREEN + "✓ " + response.get("message") + ANSI_RESET);
                String role = (String) response.get("role");
                String userId = (String) response.get("userId");

                switch (role) {
                    case "ADMIN":
                        new AdminCLIMenu().displayMenu(sc, userId);
                        break;
                    case "CUSTOMER":
                        new CustomerCLIMenu().displayMenu(sc, userId);
                        break;
                    case "GYM_OWNER":
                        new GymOwnerCLIMenu().displayMenu(sc, userId);
                        break;
                }
            } else {
                System.out.println(ANSI_RED + "\n✗ Login failed: " + response.get("error") + ANSI_RESET);
                System.out.println("Press Enter to continue...");
                sc.nextLine();
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
    }

    private static void registerCustomer(Scanner sc) {
        try {
            System.out.println("\n--- Register as Customer ---");
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            Map<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("email", email);
            data.put("password", password);
            data.put("roleChoice", 2); // Customer role

            Map<String, Object> response = HttpClientUtil.post("/users/register", data);

            if (response.get("success") != null && (Boolean) response.get("success")) {
                System.out.println("\n✓ Registration successful!");
            } else {
                System.out.println("\n✗ Registration failed: " + response.get("error"));
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
    }

    private static void registerGymOwner(Scanner sc) {
        try {
            System.out.println("\n--- Register as Gym Owner ---");
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();
            System.out.print("PAN Number: ");
            String pan = sc.nextLine();
            System.out.print("GST Number: ");
            String gst = sc.nextLine();
            System.out.print("Aadhar Number: ");
            String aadhar = sc.nextLine();
            System.out.print("Location: ");
            String location = sc.nextLine();

            Map<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("password", password);
            data.put("pan", pan);
            data.put("gst", gst);
            data.put("aadhar", aadhar);
            data.put("location", location);

            Map<String, Object> response = HttpClientUtil.post("/owners/onboard", data);

            if (response.get("success") != null && (Boolean) response.get("success")) {
                System.out.println("\n✓ " + response.get("message"));
            } else {
                System.out.println("\n✗ Registration failed: " + response.get("error"));
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
    }

    private static void changePassword(Scanner sc) {
        try {
            System.out.println("\n--- Change Password ---");
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Old Password: ");
            String oldPassword = sc.nextLine();
            System.out.print("New Password: ");
            String newPassword = sc.nextLine();

            Map<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("oldPassword", oldPassword);
            data.put("newPassword", newPassword);

            Map<String, Object> response = HttpClientUtil.put("/users/password", data);

            if (response.get("success") != null && (Boolean) response.get("success")) {
                System.out.println("\n✓ Password changed successfully!");
            } else {
                System.out.println("\n✗ Failed: " + response.get("error"));
            }
        } catch (Exception e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
    }

    private static int getIntInput(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Invalid input. Enter a number: ");
        }
        int num = sc.nextInt();
        sc.nextLine(); // consume newline
        return num;
    }
}
