package com.flipfit.client;

import com.flipfit.business.*;
import com.flipfit.bean.*;
import java.util.Scanner;

public class FlipFitApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Welcome to FlipFit System ===");
        System.out.println("1. Login as Admin");
        System.out.println("2. Login as Gym Owner");
        System.out.println("3. Login as Customer");
        System.out.println("4. Register New User");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                new AdminFlipFitMenu().displayMenu();
                break;
            case 2:
                new GymOwnerFlipFitMenu().displayMenu();
                break;
            case 3:
                new CustomerFlipFitMenu().displayMenu();
                break;
            default:
                System.out.println("Invalid Selection.");
        }
        sc.close();
    }
}
