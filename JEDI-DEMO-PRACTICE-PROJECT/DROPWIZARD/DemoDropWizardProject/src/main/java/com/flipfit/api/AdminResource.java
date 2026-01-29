package com.flipfit.api;

import com.flipfit.api.dto.ApiResponse;
import com.flipfit.bean.*;
import com.flipfit.business.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {
    
    private final AdminService adminService;
    private final NotificationService notificationService;
    private final PaymentService paymentService;
    
    public AdminResource() {
        this.adminService = new AdminService();
        this.notificationService = new NotificationService();
        this.paymentService = new PaymentService();
    }
    
    @GET
    @Path("/pending-owners")
    public Response getPendingGymOwners() {
        try {
            List<GymOwner> owners = adminService.viewPendingGymOwners();
            return Response.ok(new ApiResponse(true, "Pending gym owners retrieved", owners)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve pending gym owners: " + e.getMessage()))
                .build();
        }
    }
    
    @PUT
    @Path("/approve-owner/{ownerId}")
    public Response approveGymOwner(@PathParam("ownerId") String ownerId) {
        try {
            adminService.approveGymOwner(ownerId);
            return Response.ok(new ApiResponse(true, "Gym Owner approved successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Approval failed: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/pending-centers")
    public Response getPendingGymCenters() {
        try {
            List<GymCenter> centers = adminService.viewPendingGymCenters();
            return Response.ok(new ApiResponse(true, "Pending gym centers retrieved", centers)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve pending gym centers: " + e.getMessage()))
                .build();
        }
    }
    
    @PUT
    @Path("/approve-center/{centerId}")
    public Response approveGymCenter(@PathParam("centerId") String centerId) {
        try {
            adminService.approveGymCenter(centerId);
            return Response.ok(new ApiResponse(true, "Gym Center approved successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Approval failed: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/pending-slots")
    public Response getPendingSlots() {
        try {
            List<SlotMaster> slots = adminService.viewPendingSlots();
            return Response.ok(new ApiResponse(true, "Pending slots retrieved", slots)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve pending slots: " + e.getMessage()))
                .build();
        }
    }
    
    @PUT
    @Path("/approve-slot/{slotId}")
    public Response approveSlot(@PathParam("slotId") String slotId) {
        try {
            adminService.approveSlot(slotId);
            return Response.ok(new ApiResponse(true, "Slot approved successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Approval failed: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/center-revenue/{centerId}")
    public Response getCenterRevenue(@PathParam("centerId") String centerId, @QueryParam("ownerId") String ownerId) {
        try {
            // Use PaymentService for revenue display
            paymentService.displayGymRevenue(centerId, ownerId);
            return Response.ok(new ApiResponse(true, "Revenue displayed")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve revenue: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/owners")
    public Response getGymOwnersByStatus(@QueryParam("status") String status) {
        try {
            List<GymOwner> owners;
            if ("approved".equalsIgnoreCase(status)) {
                owners = adminService.viewGymOwnersByStatus(true);
            } else if ("pending".equalsIgnoreCase(status)) {
                owners = adminService.viewGymOwnersByStatus(false);
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Invalid status. Use 'approved' or 'pending'"))
                    .build();
            }
            return Response.ok(new ApiResponse(true, "Gym owners retrieved", owners)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve gym owners: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/centers")
    public Response getGymCentersByStatus(@QueryParam("status") String status) {
        try {
            List<GymCenter> centers;
            if ("approved".equalsIgnoreCase(status)) {
                centers = adminService.viewGymCentersByStatus(true);
            } else if ("pending".equalsIgnoreCase(status)) {
                centers = adminService.viewGymCentersByStatus(false);
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Invalid status. Use 'approved' or 'pending'"))
                    .build();
            }
            return Response.ok(new ApiResponse(true, "Gym centers retrieved", centers)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve gym centers: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/notifications/{userId}")
    public Response getNotifications(@PathParam("userId") String userId) {
        try {
            List<Notification> notifications = notificationService.getNotifications(userId);
            return Response.ok(new ApiResponse(true, "Notifications retrieved", notifications)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve notifications: " + e.getMessage()))
                .build();
        }
    }
}
