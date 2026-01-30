package com.flipfit.auth;

import io.dropwizard.auth.Authorizer;

public class AppAuthorizer implements Authorizer<AppUser> {
    @Override
    public boolean authorize(AppUser user, String role) {
        return user.getRole().toString().equals(role);
    }
}
