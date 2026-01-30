package com.flipfit.auth;

import com.flipfit.bean.User;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.dao.impl.GymUserDAOImpl;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import java.util.Optional;

public class AppAuthenticator implements Authenticator<String, AppUser> {
    
    private final GymUserDAO gymUserDAO;

    public AppAuthenticator() {
        this.gymUserDAO = new GymUserDAOImpl();
    }

    @Override
    public Optional<AppUser> authenticate(String token) throws AuthenticationException {
        // Token format: username+admin
        String[] parts = token.split("\\+");
        if (parts.length != 2 || !"admin".equals(parts[1])) {
            return Optional.empty();
        }
        
        String userId = parts[0];
        // Validate user exists and get role
        User user = gymUserDAO.getUser(userId);
        if (user != null) {
            return Optional.of(new AppUser(user.getUserId(), user.getRole()));
        }
        
        return Optional.empty();
    }
}
