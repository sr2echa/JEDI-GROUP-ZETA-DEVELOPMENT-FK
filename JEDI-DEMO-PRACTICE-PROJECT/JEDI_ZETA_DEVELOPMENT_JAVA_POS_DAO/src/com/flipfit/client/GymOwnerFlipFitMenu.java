package com.flipfit.client;

import com.flipfit.business.GymOwnerInterface;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.PaymentService;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.SlotMaster;
import com.flipfit.utils.InputValidator;
import com.flipfit.utils.InputSanitizer;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class GymOwnerFlipFitMenu.
 *
 * @author Zeta
 * @ClassName  "GymOwnerFlipFitMenu"
 */
public class GymOwnerFlipFitMenu {
    
    /** The owner service. */
    GymOwnerInterface ownerService = new GymOwnerService();
    
    /** The payment service. */
    PaymentService paymentService = new PaymentService();

    /**
     * Register gym owner.
     *
     * @param sc the sc
     */
    public void registerGymOwner(Scanner sc) {
        System.out.println("\n--- Registration of the GymOwner ---");
        
        // Username validation
        System.out.print("Enter Username: ");
        String username = sc.next();
        username = InputSanitizer.sanitizeUsername(username);
        if (!InputValidator.isValidUsername(username)) {
            System.out.println(InputValidator.getValidationErrorMessage("Username", "USERNAME"));
            return;
        }
        
        // Password validation
        System.out.print("Enter Password: ");
        String password = sc.next();
        if (!InputValidator.isValidPassword(password)) {
            System.out.println(InputValidator.getValidationErrorMessage("Password", "PASSWORD"));
            return;
        }
        
        // PAN validation
        System.out.print("Enter PAN Number: ");
        String pan = sc.next();
        pan = InputSanitizer.sanitizePAN(pan);
        if (!InputValidator.isValidPAN(pan)) {
            System.out.println(InputValidator.getValidationErrorMessage("PAN Number", "PAN"));
            return;
        }
        
        // GST validation
        System.out.print("Enter GST Number: ");
        String gst = sc.next();
        gst = InputSanitizer.sanitizeGST(gst);
        if (!InputValidator.isValidGST(gst)) {
            System.out.println(InputValidator.getValidationErrorMessage("GST Number", "GST"));
            return;
        }
        
        // Aadhar validation
        System.out.print("Enter Aadhar Number: ");
        String aadhar = sc.next();
        aadhar = InputSanitizer.sanitizeAadhar(aadhar);
        if (!InputValidator.isValidAadhar(aadhar)) {
            System.out.println(InputValidator.getValidationErrorMessage("Aadhar Number", "AADHAR"));
            return;
        }
        
        // Location validation
        System.out.print("Enter Location: ");
        String location = sc.next();
        location = InputSanitizer.sanitizeText(location);
        if (!InputValidator.isNotEmpty(location)) {
            System.out.println(InputValidator.getValidationErrorMessage("Location", "EMPTY"));
            return;
        }

        ownerService.onboardGymOwner(username, password, pan, gst, aadhar, location);
    }

    /**
     * Display menu.
     *
     * @param sc the sc
     * @param ownerId the owner id
     */
    public void displayMenu(Scanner sc, String ownerId) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Gym Owner Dashboard (" + ownerId + ") ---");
            System.out.println("1. Add Gym Center");
            System.out.println("2. View My Centers");
            System.out.println("3. Add Slot to Center");
            System.out.println("4. View Slots in Center");
            System.out.println("5. Back to Main Menu");
            System.out.println("6. View Center Revenue & History");
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
                    System.out.print("Enter Center Name: ");
                    String name = sc.next();
                    name = InputSanitizer.sanitizeText(name);
                    if (!InputValidator.isNotEmpty(name)) {
                        System.out.println(InputValidator.getValidationErrorMessage("Center Name", "EMPTY"));
                        break;
                    }
                    System.out.print("Enter Location: ");
                    String loc = sc.next();
                    loc = InputSanitizer.sanitizeText(loc);
                    if (!InputValidator.isNotEmpty(loc)) {
                        System.out.println(InputValidator.getValidationErrorMessage("Location", "EMPTY"));
                        break;
                    }
                    ownerService.addGymCenter(ownerId, name, loc);
                    break;
                case 2:
                    List<GymCenter> centers = ownerService.viewMyCenters(ownerId);
                    if (centers.isEmpty()) {
                        System.out.println("No centers found.");
                    } else {
                        centers.forEach(c -> System.out.println(" - ID: " + c.getCenterId() + " | Name: " + c.getName()
                                + " [" + (c.isApproved() ? "ACTIVE" : "PENDING") + "]"));
                    }
                    break;
                case 3:
                    System.out.print("Enter Center ID: ");
                    String cid = sc.next();
                    cid = InputSanitizer.sanitizeAlphanumeric(cid);
                    if (!InputValidator.isNotEmpty(cid)) {
                        System.out.println(InputValidator.getValidationErrorMessage("Center ID", "EMPTY"));
                        break;
                    }
                    System.out.print("Enter Start Time (HH:MM / 24hr): ");
                    String startStr = sc.next();
                    System.out.print("Enter End Time (HH:MM / 24hr): ");
                    String endStr = sc.next();
                    System.out.print("Enter Capacity: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("[ERROR] Capacity must be a positive number.");
                        sc.next(); // consume invalid input
                        break;
                    }
                    int cap = sc.nextInt();
                    if (!InputValidator.isPositiveNumber(cap)) {
                        System.out.println(InputValidator.getValidationErrorMessage("Capacity", "POSITIVE"));
                        break;
                    }
                    try {
                        LocalTime start = LocalTime.parse(startStr);
                        LocalTime end = LocalTime.parse(endStr);
                        if (end.isBefore(start) || end.equals(start)) {
                            System.out.println("[ERROR] End time must be after start time.");
                        } else {
                            ownerService.addSlot(cid, start, end, cap);
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("[ERROR] Invalid time format. Use HH:MM (e.g. 21:00)");
                    }
                    break;
                case 4:
                    System.out.print("Enter Center ID: ");
                    String viewCid = sc.next();
                    List<SlotMaster> slots = ownerService.viewSlots(viewCid);
                    if (slots.isEmpty()) {
                        System.out.println("No slots found for this center.");
                    } else {
                        slots.forEach(s -> System.out.println(" - Slot ID: " + s.getSlotId() + " | Time: "
                                + s.getStartTime() + " - " + s.getEndTime() + " | Capacity: " + s.getCapacity()));
                    }
                    break;
                case 5:
                    back = true;
                    break;
                case 6:
                    System.out.print("Enter Center ID to view revenue: ");
                    String centerId = sc.next();
                    paymentService.displayGymRevenue(centerId, ownerId);
                    break;
                default:
                    System.out.println("Invalid Selection.");
            }
        }
    }
}