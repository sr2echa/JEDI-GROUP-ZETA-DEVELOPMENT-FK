package com.flipfit.business;

import com.flipfit.bean.User;

public interface UserInterface {
    User login(String username, String password);

    boolean register(String username, String password, String email, int roleChoice);

    boolean changePassword(String username, String oldPassword, String newPassword);
}
