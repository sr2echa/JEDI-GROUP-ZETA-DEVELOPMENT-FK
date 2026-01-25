package com.flipfit.business;

import com.flipfit.bean.*;
import java.util.HashMap;
import java.util.Map;

public class UserService implements UserInterface {
    private static Map<String, User> userMap = new HashMap<>();

    static {
        // STATIC LOGIN FOR ADMIN
        Customer admin = new Customer();
        admin.setUserId("admin");
        admin.setName("Admin User");
        admin.setPassword("admin123");
        admin.setRole(Role.ADMIN);
        userMap.put("admin", admin);

        // Pre-loaded data for testing
        Customer customer = new Customer();
        customer.setUserId("customer1");
        customer.setName("John Doe");
        customer.setPassword("pass123");
        customer.setRole(Role.CUSTOMER);
        userMap.put("customer1", customer);

        GymOwner owner = new GymOwner();
        owner.setUserId("owner1");
        owner.setName("Gym Master");
        owner.setPassword("owner123");
        owner.setRole(Role.GYM_OWNER);
        owner.setApproved(true);
        owner.setPanNumber("ABCDE1234F");
        userMap.put("owner1", owner);
    }

    @Override
    public User login(String username, String password) {
        User user = userMap.get(username);

        // 1. Check if account exists and password matches
        if (user != null && user.getPassword().equals(password)) {

            // 2. Role-specific logic (e.g., Approval for Gym Owners)
            if (user.getRole() == Role.GYM_OWNER) {
                GymOwner owner = (GymOwner) user;
                if (!owner.isApproved()) {
                    System.out.println("[ERROR] Gym Owner account '" + username + "' is pending Admin approval.");
                    return null;
                }
            }

            System.out.println("[SUCCESS] Welcome back, " + user.getName() + "!");
            return user;
        }

        System.out.println("[ERROR] Invalid Username or Password. Please try again or Register.");
        return null;
    }

    @Override
    public boolean register(String username, String password, String email, int roleChoice) {
        if (userMap.containsKey(username)) {
            System.out.println("[ERROR] Username '" + username + "' already exists.");
            return false;
        }

        User newUser;
        Role role = getRoleFromChoice(roleChoice);

        if (role == Role.CUSTOMER) {
            newUser = new Customer();
        } else if (role == Role.GYM_OWNER) {
            newUser = new GymOwner();
        } else {
            System.out.println("[ERROR] Invalid registration path.");
            return false;
        }

        newUser.setUserId(username);
        newUser.setName(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setRole(role); // Role auto-assigned based on account type registration

        userMap.put(username, newUser);
        return true;
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = userMap.get(username);
        if (user != null && user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }

    private Role getRoleFromChoice(int choice) {
        switch (choice) {
            case 1:
                return Role.GYM_OWNER;
            case 2:
                return Role.CUSTOMER;
            default:
                return null;
        }
    }

    public static void addUser(User user) {
        userMap.put(user.getUserId(), user);
    }

    public static User getUser(String username) {
        return userMap.get(username);
    }

    public static Map<String, User> getAllUsers() {
        return userMap;
    }
}
