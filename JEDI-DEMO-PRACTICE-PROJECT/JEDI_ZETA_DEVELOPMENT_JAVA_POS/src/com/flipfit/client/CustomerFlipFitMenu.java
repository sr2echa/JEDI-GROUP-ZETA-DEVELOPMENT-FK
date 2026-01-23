package com.flipfit.client;

import com.flipfit.business.CustomerInterface;
import com.flipfit.business.CustomerService;
import java.util.Scanner;

public class CustomerFlipFitMenu {
    CustomerInterface customerService = new CustomerService();
    Scanner sc = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("--- Customer Dashboard ---");
        System.out.println("1. Book Workout Slot");
        System.out.println("2. View My Plan");
        System.out.println("3. Cancel Booking");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                System.out.print("Enter Schedule ID: ");
                customerService.bookWorkout("CUST101", sc.next());
                break;
            case 3:
                System.out.print("Enter Booking ID: ");
                customerService.cancelWorkout(sc.next());
                break;
        }
    }
}