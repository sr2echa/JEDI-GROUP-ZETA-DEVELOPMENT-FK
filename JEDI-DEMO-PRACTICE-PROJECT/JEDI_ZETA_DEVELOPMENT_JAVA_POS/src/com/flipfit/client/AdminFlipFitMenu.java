package com.flipfit.client;

import com.flipfit.business.AdminInterface;
import com.flipfit.business.AdminService;
import java.util.Scanner;

public class AdminFlipFitMenu {
    AdminInterface adminService = new AdminService();
    Scanner sc = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("--- Admin Dashboard ---");
        System.out.println("1. Approve Gym Owner");
        System.out.println("2. View Pending Centers");
        int choice = sc.nextInt();
        if (choice == 1) {
            System.out.print("Enter Owner ID: ");
            adminService.approveGymOwner(sc.next());
        }
    }
}