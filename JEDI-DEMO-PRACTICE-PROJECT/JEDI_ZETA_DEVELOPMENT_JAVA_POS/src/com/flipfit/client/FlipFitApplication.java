package com.flipfit.client;

import java.util.Scanner;

public class FlipFitApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        AdminFlipFitMenu adminMenu = new AdminFlipFitMenu();
        CustomerFlipFitMenu customerMenu = new CustomerFlipFitMenu();
        GymOwnerFlipFitMenu ownerMenu = new GymOwnerFlipFitMenu();

        while (!exit) {
            System.out.println("\nWelcome to the Flipfit Application for GYM");
            System.out.println("1. Login");
            System.out.println("2. Registration of the GymCustomer");
            System.out.println("3. Registration of the GymOwner");
            System.out.println("4. Change Password");
            System.out.println("5. Exit");
            System.out.print("Press choice: -> ");

            int choice = 0;
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); // consume newline
            } else {
                sc.nextLine();
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    handleLogin(sc, adminMenu, customerMenu, ownerMenu);
                    break;
                case 2:
                    customerMenu.registerCustomer(sc);
                    break;
                case 3:
                    ownerMenu.registerGymOwner(sc);
                    break;
                case 4:
                    handleChangePassword(sc);
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting FlipFit. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid Selection.");
            }
        }
        sc.close();
    }

    private static void handleLogin(Scanner sc, AdminFlipFitMenu admin, CustomerFlipFitMenu customer,
            GymOwnerFlipFitMenu owner) {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        System.out.println("Role: 1. GYMOwner 2. GymCustomer 3. GymAdmin");
        System.out.print("Select Role (1-3): ");

        int role = 0;
        if (sc.hasNextInt()) {
            role = sc.nextInt();
            sc.nextLine(); // consume newline
        } else {
            sc.nextLine();
            System.out.println("Invalid role selection.");
            return;
        }

        switch (role) {
            case 1:
                owner.displayMenu(sc, username);
                break;
            case 2:
                customer.displayMenu(sc, username);
                break;
            case 3:
                admin.displayMenu(sc);
                break;
            default:
                System.out.println("Invalid Role selected.");
        }
    }

    private static void handleChangePassword(Scanner sc) {
        System.out.println("\n--- Change Password ---");
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Old Password: ");
        String oldPwd = sc.nextLine();
        System.out.print("Enter New Password: ");
        String newPwd = sc.nextLine();
        System.out.println("[SYSTEM] Password for " + username + " updated successfully!");
    }
}
