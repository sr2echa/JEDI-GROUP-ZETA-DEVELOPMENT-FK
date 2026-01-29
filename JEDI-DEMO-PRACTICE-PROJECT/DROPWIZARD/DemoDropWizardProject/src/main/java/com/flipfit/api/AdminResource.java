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
    
    public AdminResource() {
        this.adminService = new AdminService();
        this.notificationService = new NotificationService();
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
    public Response approveGymOwner(@PathParam("ownerId") int ownerId) {
        try {
            boolean success = adminService.approveGymOwner(ownerId);
            if (success) {
                return Response.ok(new ApiResponse(true, "Gym Owner approved successfully")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Failed to approve gym owner"))
                    .build();
            }
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
    public Response approveGymCenter(@PathParam("centerId") int centerId) {
        try {
            boolean success = adminService.approveGymCenter(centerId);
            if (success) {
                return Response.ok(new ApiResponse(true, "Gym Center approved successfully")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Failed to approve gym center"))
                    .build();
            }
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
    public Response approveSlot(@PathParam("slotId") int slotId) {
        try {
            boolean success = adminService.approveSlot(slotId);
            if (success) {
                return Response.ok(new ApiResponse(true, "Slot approved successfully")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Failed to approve slot"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Approval failed: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/center-revenue/{centerId}")
    public Response getCenterRevenue(@PathParam("centerId") int centerId) {
        try {
            double revenue = adminService.viewCenterRevenue(centerId);
            List<PaymentRecord> history = adminService.viewCenterPaymentHistory(centerId);
            ApiResponse response = new ApiResponse(true, "Revenue retrieved");
            response.setData(new Object() {
                public double revenue = revenue;
                public List<PaymentRecord> paymentHistory = history;
            });
            return Response.ok(response).build();
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
                owners = adminService.viewApprovedGymOwners();
            } else if ("pending".equalsIgnoreCase(status)) {
                owners = adminService.viewPendingGymOwners();
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
                centers = adminService.viewApprovedGymCenters();
            } else if ("pending".equalsIgnoreCase(status)) {
                centers = adminService.viewPendingGymCenters();
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
    public Response getNotifications(@PathParam("userId") int userId) {
        try {
            List<Notification> notifications = notificationService.getNotificationsByUser(userId);
            return Response.ok(new ApiResponse(true, "Notifications retrieved", notifications)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve notifications: " + e.getMessage()))
                .build();
        }
    }
}
