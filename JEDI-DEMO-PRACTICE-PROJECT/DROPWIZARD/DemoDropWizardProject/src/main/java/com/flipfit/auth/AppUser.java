package com.flipfit.auth;

import com.flipfit.bean.Role;
import java.security.Principal;

public class AppUser implements Principal {
    private final String name;
    private final Role role;

    public AppUser(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    @Override
    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
