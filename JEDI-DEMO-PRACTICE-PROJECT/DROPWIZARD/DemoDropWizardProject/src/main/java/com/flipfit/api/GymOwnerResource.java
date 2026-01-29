package com.flipfit.api;

import com.flipfit.api.dto.ApiResponse;
import com.flipfit.bean.*;
import com.flipfit.business.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Path("/api/gymowner")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymOwnerResource {
    
    private final GymOwnerService gymOwnerService;
    private final NotificationService notificationService;
    private final PaymentService paymentService;
    
    public GymOwnerResource() {
        this.gymOwnerService = new GymOwnerService();
        this.notificationService = new NotificationService();
        this.paymentService = new PaymentService();
    }
    
    @POST
    @Path("/add-center")
    public Response addGymCenter(Map<String, String> centerData) {
        try {
            gymOwnerService.addGymCenter(
                centerData.get("ownerId"),
                centerData.get("centerName"),
                centerData.get("location")
            );
            return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse(true, "Gym Center added successfully. Awaiting admin approval."))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to add gym center: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/centers/{ownerId}")
    public Response getOwnerCenters(@PathParam("ownerId") String ownerId) {
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
    public Response addSlot(Map<String, String> slotData) {
        try {
            LocalTime startTime = LocalTime.parse(slotData.get("startTime"));
            LocalTime endTime = LocalTime.parse(slotData.get("endTime"));
            int capacity = Integer.parseInt(slotData.get("capacity"));
            
            boolean success = gymOwnerService.addSlot(
                slotData.get("centerId"),
                startTime,
                endTime,
                capacity
            );
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
    public Response getSlotsByCenter(@PathParam("centerId") String centerId) {
        try {
            List<SlotMaster> slots = gymOwnerService.viewSlots(centerId);
            return Response.ok(new ApiResponse(true, "Slots retrieved", slots)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve slots: " + e.getMessage()))
                .build();
        }
    }
    
    @PUT
    @Path("/update-slot-capacity/{slotId}")
    public Response updateSlotCapacity(@PathParam("slotId") String slotId, @QueryParam("capacity") int capacity) {
        try {
            gymOwnerService.updateSlotCapacity(slotId, capacity);
            return Response.ok(new ApiResponse(true, "Slot capacity updated successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to update capacity: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/center-revenue/{centerId}")
    public Response getCenterRevenue(@PathParam("centerId") String centerId, @QueryParam("ownerId") String ownerId) {
        try {
            paymentService.displayGymRevenue(centerId, ownerId);
            return Response.ok(new ApiResponse(true, "Revenue displayed")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve revenue: " + e.getMessage()))
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
