package com.flipfit.resources;

import com.flipfit.bean.GymCenter;
import com.flipfit.business.GymOwnerInterface;
import com.flipfit.business.GymOwnerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GymOwnerResource - REST endpoints for gym owner operations
 */
@Path("/api/owners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymOwnerResource {

    private final GymOwnerService ownerService = new GymOwnerService();

    /**
     * Onboard gym owner
     * POST /api/owners/onboard
     */
    @POST
    @Path("/onboard")
    public Response onboardGymOwner(Map<String, String> ownerData) {
        try {
            String username = ownerData.get("username");
            String password = ownerData.get("password");
            String pan = ownerData.get("pan");
            String gst = ownerData.get("gst");
            String aadhar = ownerData.get("aadhar");
            String location = ownerData.get("location");

            ownerService.onboardGymOwner(username, password, pan, gst, aadhar, location);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Gym owner registered successfully. Awaiting admin approval.");

            return Response.status(Response.Status.CREATED).entity(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Onboarding failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Add gym center
     * POST /api/owners/{ownerId}/centers
     */
    @POST
    @Path("/{ownerId}/centers")
    public Response addGymCenter(@PathParam("ownerId") String ownerId, Map<String, String> centerData) {
        try {
            String name = centerData.get("name");
            String location = centerData.get("location");

            ownerService.addGymCenter(ownerId, name, location);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Gym center added successfully. Awaiting admin approval.");

            return Response.status(Response.Status.CREATED).entity(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to add center: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * View my centers
     * GET /api/owners/{ownerId}/centers
     */
    @GET
    @Path("/{ownerId}/centers")
    public Response viewMyCenters(@PathParam("ownerId") String ownerId) {
        try {
            List<GymCenter> centers = ownerService.viewMyCenters(ownerId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("centers", centers);

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get centers: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Add slot to center
     * POST /api/owners/centers/{centerId}/slots
     */
    @POST
    @Path("/centers/{centerId}/slots")
    public Response addSlot(@PathParam("centerId") String centerId, Map<String, Object> slotData) {
        try {
            String startTime = (String) slotData.get("startTime");
            String endTime = (String) slotData.get("endTime");
            Integer capacity = (Integer) slotData.get("capacity");
            Double price = slotData.get("price") != null ? ((Number) slotData.get("price")).doubleValue() : 100.0;

            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);

            ownerService.addSlot(centerId, start, end, capacity, price);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Slot added successfully. Awaiting admin approval.");

            return Response.status(Response.Status.CREATED).entity(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to add slot: " + e.getMessage()))
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
