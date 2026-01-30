package com.flipfit.resources;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.SlotMaster;
import com.flipfit.business.AdminInterface;
import com.flipfit.business.AdminService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AdminResource - REST endpoints for admin operations
 */
@Path("/admin")
@RolesAllowed("ADMIN")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {

    private final AdminInterface adminService = new AdminService();

    /**
     * Approve gym owner
     * PUT /api/admin/owners/{ownerId}/approve
     */
    @PUT
    @Path("/owners/{ownerId}/approve")
    public Response approveGymOwner(@PathParam("ownerId") String ownerId) {
        try {
            adminService.approveGymOwner(ownerId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Gym owner approved successfully");

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Approval failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Approve gym center
     * PUT /api/admin/centers/{centerId}/approve
     */
    @PUT
    @Path("/centers/{centerId}/approve")
    public Response approveGymCenter(@PathParam("centerId") String centerId) {
        try {
            adminService.approveGymCenter(centerId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Gym center approved successfully");

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Approval failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Approve slot
     * PUT /api/admin/slots/{slotId}/approve
     */
    @PUT
    @Path("/slots/{slotId}/approve")
    public Response approveSlot(@PathParam("slotId") String slotId) {
        try {
            adminService.approveSlot(slotId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Slot approved successfully");

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Approval failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get pending gym owners
     * GET /api/admin/owners/pending
     */
    @GET
    @Path("/owners/pending")
    public Response viewPendingGymOwners() {
        try {
            List<GymOwner> pendingOwners = adminService.viewPendingGymOwners();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("pendingOwners", pendingOwners);

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get pending owners: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get pending gym centers
     * GET /api/admin/centers/pending
     */
    @GET
    @Path("/centers/pending")
    public Response viewPendingGymCenters() {
        try {
            List<GymCenter> pendingCenters = adminService.viewPendingGymCenters();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("pendingCenters", pendingCenters);

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get pending centers: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get pending slots
     * GET /api/admin/slots/pending
     */
    @GET
    @Path("/slots/pending")
    public Response viewPendingSlots() {
        try {
            List<SlotMaster> pendingSlots = adminService.viewPendingSlots();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("pendingSlots", pendingSlots);

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get pending slots: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get gym owners by status
     * GET /api/admin/owners?approved={true|false}
     */
    @GET
    @Path("/owners")
    public Response viewGymOwnersByStatus(@QueryParam("approved") @DefaultValue("true") boolean isApproved) {
        try {
            List<GymOwner> owners = adminService.viewGymOwnersByStatus(isApproved);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("owners", owners);

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get owners: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get gym centers by status
     * GET /api/admin/centers?approved={true|false}
     */
    @GET
    @Path("/centers")
    public Response viewGymCentersByStatus(@QueryParam("approved") @DefaultValue("true") boolean isApproved) {
        try {
            List<GymCenter> centers = adminService.viewGymCentersByStatus(isApproved);

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

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", message);
        return error;
    }
}
