package com.flipfit.business;

import com.flipfit.bean.*;
import java.util.HashMap;
import java.util.Map;

public class UserService implements UserInterface {
    private static Map<String, User> userMap = new HashMap<>();

    static {
        // Hardcoded data initialization
        Customer admin = new Customer();
        admin.setUserId("admin");
        admin.setName("Admin User");
        admin.setPassword("admin123");
        admin.setRole(Role.ADMIN);
        userMap.put("admin", admin);

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
        userMap.put("owner1", owner);
    }

    @Override
    public boolean login(String username, String password, int roleChoice) {
        User user = userMap.get(username);
        if (user != null && user.getPassword().equals(password)) {
            Role expectedRole = getRoleFromChoice(roleChoice);
            if (user.getRole() == expectedRole) {
                if (user.getRole() == Role.GYM_OWNER) {
                    GymOwner owner = (GymOwner) user;
                    if (!owner.isApproved()) {
                        System.out.println("[ERROR] Gym Owner account not yet approved by Admin.");
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean register(String username, String password, String email, int roleChoice) {
        if (userMap.containsKey(username)) {
            System.out.println("[ERROR] Username already exists.");
            return false;
        }

        User newUser;
        Role role = getRoleFromChoice(roleChoice);
        if (role == Role.CUSTOMER) {
            newUser = new Customer();
        } else if (role == Role.GYM_OWNER) {
            newUser = new GymOwner();
        } else {
            System.out.println("[ERROR] Invalid registration role.");
            return false;
        }

        newUser.setUserId(username);
        newUser.setName(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setRole(role);

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
            case 3:
                return Role.ADMIN;
            default:
                return null;
        }
    }

    // Helper method to get user for other services
    public static User getUser(String username) {
        return userMap.get(username);
    }
}
