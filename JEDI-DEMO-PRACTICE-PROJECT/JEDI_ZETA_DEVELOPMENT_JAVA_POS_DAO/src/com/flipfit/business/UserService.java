package com.flipfit.business;

import com.flipfit.bean.*;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.dao.impl.GymUserDAOImpl;

public class UserService implements UserInterface {
    private GymUserDAO userDAO = new GymUserDAOImpl();

    @Override
    public User login(String username, String password) {
        User user = userDAO.loginUser(username, password);

        if (user != null) {
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
        Role role = getRoleFromChoice(roleChoice);
        User newUser;
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
        newUser.setRole(role);

        return userDAO.registerUser(newUser);
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return userDAO.changePassword(username, oldPassword, newPassword);
    }

    @Override
    public User getUser(String userId) {
        return userDAO.getUser(userId);
    }

    @Override
    public java.util.List<User> getAllUsers() {
        return userDAO.getAllUsers();
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
}
