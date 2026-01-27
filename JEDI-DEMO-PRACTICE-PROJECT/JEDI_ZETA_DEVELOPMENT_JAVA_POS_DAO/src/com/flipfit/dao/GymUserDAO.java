package com.flipfit.dao;

import com.flipfit.bean.User;

public interface GymUserDAO {
    public boolean registerUser(User user);

    public User loginUser(String username, String password);

    public boolean changePassword(String username, String oldPassword, String newPassword);

    public User getUser(String userId);

    public java.util.List<User> getAllUsers();
}
