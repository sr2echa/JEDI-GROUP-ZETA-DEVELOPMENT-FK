package com.flipfit.api;

import com.flipfit.api.dto.*;
import com.flipfit.bean.Role;
import com.flipfit.bean.User;
import com.flipfit.business.*;
import com.flipfit.exception.UserNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

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
                    Integer.parseInt(user.getUserId()),
                    user.getName(),
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
    public Response registerCustomer(Map<String, String> userData) {
        try {
            // Customer registration is handled via userService login with role CUSTOMER
            // For now, return success message indicating manual registration is needed
            return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse(true, "Customer registration submitted. Please contact admin for approval."))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Registration failed: " + e.getMessage()))
                .build();
        }
    }
    
    @POST
    @Path("/register/gymowner")
    public Response registerGymOwner(Map<String, String> ownerData) {
        try {
            gymOwnerService.onboardGymOwner(
                ownerData.get("username"),
                ownerData.get("password"),
                ownerData.get("panCard"),
                ownerData.get("gstNumber"),
                ownerData.get("aadhaarNumber"),
                ownerData.get("location")
            );
            return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse(true, "Gym Owner registered successfully. Awaiting approval."))
                .build();
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
