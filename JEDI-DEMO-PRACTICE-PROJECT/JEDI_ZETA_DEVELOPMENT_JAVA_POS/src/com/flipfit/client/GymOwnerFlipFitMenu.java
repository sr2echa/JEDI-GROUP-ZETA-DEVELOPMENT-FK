package com.flipfit.client;

import com.flipfit.business.GymOwnerInterface;
import com.flipfit.business.GymOwnerService;
import java.util.Scanner;

public class GymOwnerFlipFitMenu {
    GymOwnerInterface ownerService = new GymOwnerService();
    Scanner sc = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("--- Gym Owner Dashboard ---");
        System.out.println("1. Add New Slot");
        System.out.println("2. View My Centers");
        int choice = sc.nextInt();
        if (choice == 1) {
            System.out.print("Enter Center ID: ");
            ownerService.manageSlots(sc.next());
        }
    }
}