package com.flipfit.client;

import com.flipfit.business.CustomerInterface;
import com.flipfit.business.CustomerService;
import com.flipfit.business.UserInterface;
import com.flipfit.business.UserService;
import java.util.Scanner;

public class CustomerFlipFitMenu {
    CustomerInterface customerService = new CustomerService();
    UserInterface userService = new UserService();

    public void registerCustomer(Scanner sc) {
        System.out.println("\n--- Registration of the GymCustomer ---");
        System.out.print("Enter Username: ");
        String username = sc.next();
        System.out.print("Enter Email: ");
        String email = sc.next();
        System.out.print("Enter Password: ");
        String password = sc.next();

        if (userService.register(username, password, email, 2)) {
            System.out.println("[SYSTEM] Customer " + username + " Registration Successful!");
        }
    }

    public void displayMenu(Scanner sc, String userId) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Customer Dashboard (" + userId + ") ---");
            System.out.println("1. Book Workout Slot");
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
                    System.out.print("Enter Schedule ID: ");
                    customerService.bookWorkout(userId, sc.next());
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
}