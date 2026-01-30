package com.flipfit.resources;

import com.flipfit.bean.PaymentRecord;
import com.flipfit.business.GymOwnerService;
import com.flipfit.dao.PaymentDAO;
import com.flipfit.dao.impl.PaymentDAOImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PaymentResource - REST endpoints for payment operations
 */
@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final GymOwnerService ownerService = new GymOwnerService();

    /**
     * Get payment history for a user
     * GET /api/payments/history/{userId}
     */
    @GET
    @Path("/history/{userId}")
    public Response getPaymentHistory(@PathParam("userId") String userId) {
        try {
            List<PaymentRecord> history = paymentDAO.getPaymentHistory(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("history", history);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to fetch history: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get revenue for a gym owner
     * GET /api/payments/revenue/{ownerId}
     */
    @GET
    @Path("/revenue/{ownerId}")
    public Response getOwnerRevenue(@PathParam("ownerId") String ownerId) {
        try {
            double revenue = ownerService.getOwnerRevenue(ownerId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("revenue", revenue);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to fetch revenue: " + e.getMessage()))
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
