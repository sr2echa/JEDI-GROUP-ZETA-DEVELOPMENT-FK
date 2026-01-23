package com.flipflit.client;
import com.flipfit.business.*;
import com.flipfit.bean.*;
import java.util.Scanner;

public class FlipFitApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AdminInterface adminService = new AdminService();
        CustomerInterface customerService = new CustomerService();
        GymOwnerInterface ownerService = new GymOwnerService();

        System.out.println("=== FlipFit System Console ===");
        System.out.println("Login as: 1. Admin | 2. Gym Owner | 3. Customer");
        int roleChoice = sc.nextInt();

        switch (roleChoice) {
            case 1:
                System.out.print("Enter Owner ID to approve: ");
                adminService.approveGymOwner(sc.next());
                break;
            case 2:
                System.out.print("Enter Center ID to manage: ");
                ownerService.manageSlots(sc.next());
                break;
            case 3:
                System.out.println("1. Book Slot | 2. View History");
                int action = sc.nextInt();
                if (action == 1) {
                    System.out.print("Enter Schedule ID: ");
                    customerService.bookWorkout("USER_01", sc.next());
                }
                break;
            default:
                System.out.println("Invalid Session.");
        }
        sc.close();
    }
}
