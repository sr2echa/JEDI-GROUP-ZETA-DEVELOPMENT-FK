package com.flipfit.api;

import com.flipfit.api.dto.*;
import com.flipfit.bean.Role;
import com.flipfit.bean.User;
import com.flipfit.business.*;
import com.flipfit.exception.UserNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    
    private final UserService userService;
    private final CustomerService customerService;
    private final GymOwnerService gymOwnerService;
    
    public AuthResource() {
        this.userService = new UserService();
        this.customerService = new CustomerService();
        this.gymOwnerService = new GymOwnerService();
    }
    
    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            if (user != null) {
                LoginResponse response = new LoginResponse(
                    user.getUserId(),
                    user.getUsername(),
                    user.getRole(),
                    "Login successful"
                );
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ApiResponse(false, "Invalid username or password"))
                    .build();
            }
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ApiResponse(false, e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Login failed: " + e.getMessage()))
                .build();
        }
    }
    
    @POST
    @Path("/register/customer")
    public Response registerCustomer(User user) {
        try {
            user.setRole(Role.CUSTOMER);
            boolean success = customerService.registerCustomer(user);
            if (success) {
                return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse(true, "Customer registered successfully. Awaiting approval."))
                    .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Customer registration failed"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Registration failed: " + e.getMessage()))
                .build();
        }
    }
    
    @POST
    @Path("/register/gymowner")
    public Response registerGymOwner(User user) {
        try {
            user.setRole(Role.GYMOWNER);
            boolean success = gymOwnerService.registerGymOwner(user);
            if (success) {
                return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse(true, "Gym Owner registered successfully. Awaiting approval."))
                    .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Gym Owner registration failed"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Registration failed: " + e.getMessage()))
                .build();
        }
    }
    
    @PUT
    @Path("/change-password")
    public Response changePassword(ChangePasswordRequest request) {
        try {
            boolean success = userService.changePassword(
                request.getUsername(),
                request.getOldPassword(),
                request.getNewPassword()
            );
            if (success) {
                return Response.ok(new ApiResponse(true, "Password changed successfully")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Password change failed"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Password change failed: " + e.getMessage()))
                .build();
        }
    }
}
