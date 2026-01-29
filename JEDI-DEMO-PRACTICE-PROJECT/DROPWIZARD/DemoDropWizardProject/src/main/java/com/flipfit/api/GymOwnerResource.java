package com.flipfit.api;

import com.flipfit.api.dto.ApiResponse;
import com.flipfit.bean.*;
import com.flipfit.business.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/gymowner")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymOwnerResource {
    
    private final GymOwnerService gymOwnerService;
    private final NotificationService notificationService;
    
    public GymOwnerResource() {
        this.gymOwnerService = new GymOwnerService();
        this.notificationService = new NotificationService();
    }
    
    @POST
    @Path("/add-center")
    public Response addGymCenter(GymCenter center) {
        try {
            boolean success = gymOwnerService.addGymCenter(center);
            if (success) {
                return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse(true, "Gym Center added successfully. Awaiting admin approval."))
                    .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Failed to add gym center"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to add gym center: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/centers/{ownerId}")
    public Response getOwnerCenters(@PathParam("ownerId") int ownerId) {
        try {
            List<GymCenter> centers = gymOwnerService.viewMyCenters(ownerId);
            return Response.ok(new ApiResponse(true, "Centers retrieved", centers)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve centers: " + e.getMessage()))
                .build();
        }
    }
    
    @POST
    @Path("/add-slot")
    public Response addSlot(SlotMaster slot) {
        try {
            boolean success = gymOwnerService.addSlot(slot);
            if (success) {
                return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse(true, "Slot added successfully. Awaiting admin approval."))
                    .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Failed to add slot"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to add slot: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/slots/{centerId}")
    public Response getSlotsByCenter(@PathParam("centerId") int centerId) {
        try {
            List<SlotMaster> slots = gymOwnerService.viewSlotsByCenter(centerId);
            return Response.ok(new ApiResponse(true, "Slots retrieved", slots)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve slots: " + e.getMessage()))
                .build();
        }
    }
    
    @PUT
    @Path("/update-slot-capacity/{slotId}")
    public Response updateSlotCapacity(@PathParam("slotId") int slotId, @QueryParam("capacity") int capacity) {
        try {
            boolean success = gymOwnerService.updateSlotCapacity(slotId, capacity);
            if (success) {
                return Response.ok(new ApiResponse(true, "Slot capacity updated successfully")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Failed to update slot capacity"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to update capacity: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/center-revenue/{centerId}")
    public Response getCenterRevenue(@PathParam("centerId") int centerId) {
        try {
            double revenue = gymOwnerService.viewCenterRevenue(centerId);
            List<PaymentRecord> history = gymOwnerService.viewCenterPaymentHistory(centerId);
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
