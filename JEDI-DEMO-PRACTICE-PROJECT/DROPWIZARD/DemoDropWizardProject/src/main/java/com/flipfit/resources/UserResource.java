package com.flipfit.resources;

import com.flipfit.bean.User;
import com.flipfit.business.UserInterface;
import com.flipfit.business.UserService;
import com.flipfit.exception.RegistrationFailedException;
import com.flipfit.exception.UserNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserResource - REST endpoints for user operations
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserInterface userService = new UserService();

    /**
     * Login endpoint
     * POST /api/users/login
     */
    @POST
    @Path("/login")
    public Response login(Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            User user = userService.login(username, password);

            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(createErrorResponse("Invalid credentials or account pending approval"))
                        .build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", user.getUserId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("role", user.getRole().toString());
            response.put("message", "Login successful");

            return Response.ok(response).build();

        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Login failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Register endpoint
     * POST /api/users/register
     */
    @POST
    @Path("/register")
    public Response register(Map<String, Object> registrationData) {
        try {
            String username = (String) registrationData.get("username");
            String password = (String) registrationData.get("password");
            String email = (String) registrationData.get("email");
            Integer roleChoice = (Integer) registrationData.get("roleChoice");

            boolean success = userService.register(username, password, email, roleChoice);

            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", "Registration successful");
            response.put("userId", username);

            return Response.status(Response.Status.CREATED).entity(response).build();

        } catch (RegistrationFailedException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Registration failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Change password endpoint
     * PUT /api/users/password
     */
    @PUT
    @Path("/password")
    public Response changePassword(Map<String, String> passwordData) {
        try {
            String username = passwordData.get("username");
            String oldPassword = passwordData.get("oldPassword");
            String newPassword = passwordData.get("newPassword");

            boolean success = userService.changePassword(username, oldPassword, newPassword);

            if (success) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Password changed successfully");
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("Password change failed. Check your credentials."))
                        .build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Password change failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get user by ID
     * GET /api/users/{userId}
     */
    @GET
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") String userId) {
        try {
            User user = userService.getUser(userId);

            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(createErrorResponse("User not found"))
                        .build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getUserId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("role", user.getRole().toString());

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get user: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get all users
     * GET /api/users
     */
    @GET
    public Response getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return Response.ok(users).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get users: " + e.getMessage()))
                    .build();
        }
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", message);
        return error;
    }
}
