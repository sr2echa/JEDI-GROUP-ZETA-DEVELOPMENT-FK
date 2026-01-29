package com.flipfit.resources;

import com.flipfit.bean.SlotMaster;
import com.flipfit.business.GymOwnerInterface;
import com.flipfit.business.GymOwnerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SlotResource - REST endpoints for slot operations
 */
@Path("/api/slots")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SlotResource {

    private final GymOwnerInterface ownerService = new GymOwnerService();

    /**
     * Get slots by center ID
     * GET /api/slots/center/{centerId}
     */
    @GET
    @Path("/center/{centerId}")
    public Response getSlotsByCenter(@PathParam("centerId") String centerId) {
        try {
            List<SlotMaster> slots = ownerService.viewSlots(centerId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("slots", slots);

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get slots: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get slot by ID
     * GET /api/slots/{slotId}
     */
    @GET
    @Path("/{slotId}")
    public Response getSlotById(@PathParam("slotId") String slotId) {
        try {
            SlotMaster slot = GymOwnerService.getSlot(slotId);

            if (slot == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(createErrorResponse("Slot not found"))
                        .build();
            }

            return Response.ok(slot).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get slot: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Update slot availability
     * PUT /api/slots/{slotId}/availability
     */
    @PUT
    @Path("/{slotId}/availability")
    public Response updateAvailability(@PathParam("slotId") String slotId, Map<String, Integer> data) {
        try {
            Integer delta = data.get("delta");
            GymOwnerService.updateAvailability(slotId, delta);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Availability updated");

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to update availability: " + e.getMessage()))
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
