package com.flipfit.resources;

import com.flipfit.bean.Notification;
import com.flipfit.business.NotificationService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NotificationResource - REST endpoints for notification operations
 */
@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    private final NotificationService notificationService = new NotificationService();

    /**
     * Get notifications for a user
     * GET /api/notifications/{userId}
     */
    @GET
    @Path("/{userId}")
    public Response getNotifications(@PathParam("userId") String userId) {
        try {
            List<Notification> notifications = notificationService.getNotifications(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("notifications", notifications);

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get notifications: " + e.getMessage()))
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
