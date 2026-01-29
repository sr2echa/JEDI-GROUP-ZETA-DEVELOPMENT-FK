package com.flipfit.resources;

import com.flipfit.bean.GymCenter;
import com.flipfit.business.GymOwnerInterface;
import com.flipfit.business.GymOwnerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GymCenterResource - REST endpoints for gym center operations
 */
@Path("/centers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymCenterResource {

    private final GymOwnerInterface ownerService = new GymOwnerService();

    /**
     * Get all approved gym centers
     * GET /api/centers
     */
    @GET
    public Response getAllCenters() {
        try {
            List<GymCenter> centers = ownerService.getAllCenters();

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
     * Get center by ID
     * GET /api/centers/{centerId}
     */
    @GET
    @Path("/{centerId}")
    public Response getCenterById(@PathParam("centerId") String centerId) {
        try {
            GymCenter center = GymOwnerService.getCenterById(centerId);

            if (center == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(createErrorResponse("Center not found"))
                        .build();
            }

            return Response.ok(center).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get center: " + e.getMessage()))
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
