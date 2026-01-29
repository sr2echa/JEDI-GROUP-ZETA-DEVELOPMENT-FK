package com.flipfit.resources;

import com.flipfit.bean.PaymentRecord;
import com.flipfit.bean.User;
import com.flipfit.business.PaymentInterface;
import com.flipfit.business.PaymentService;
import com.flipfit.business.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PaymentResource - REST endpoints for payment operations
 */
@Path("/api/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    private final PaymentService paymentService = new PaymentService();
    private final UserService userService = new UserService();

    /**
     * Process payment
     * POST /api/payments/process
     */
    @POST
    @Path("/process")
    public Response processPayment(Map<String, Object> paymentData) {
        try {
            String bookingId = (String) paymentData.get("bookingId");
            Double amount = ((Number) paymentData.get("amount")).doubleValue();
            String paymentMethod = (String) paymentData.get("paymentMethod");

            boolean success = paymentService.processPayment(bookingId, amount, paymentMethod);

            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", success ? "Payment processed successfully" : "Payment failed");

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Payment processing failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get customer payment history
     * GET /api/payments/history/{userId}
     */
    @GET
    @Path("/history/{userId}")
    public Response getCustomerHistory(@PathParam("userId") String userId) {
        try {
            List<PaymentRecord> history = paymentService.getCustomerHistory(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("history", history);

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get payment history: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get revenue for a gym center
     * GET /api/payments/revenue/{centerId}?userId={userId}
     */
    @GET
    @Path("/revenue/{centerId}")
    public Response getGymRevenue(@PathParam("centerId") String centerId,
            @QueryParam("userId") String userId) {
        try {
            User user = userService.getUser(userId);
            // displayGymRevenue prints to console, so we'll call it
            // For REST API, we just return success confirmation
            paymentService.displayGymRevenue(centerId, userId, user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Revenue displayed in server logs");

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get revenue: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get payment method for a booking
     * GET /api/payments/method/{bookingId}
     */
    @GET
    @Path("/method/{bookingId}")
    public Response getPaymentMethod(@PathParam("bookingId") String bookingId) {
        try {
            String method = paymentService.getPaymentMethod(bookingId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("paymentMethod", method);

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get payment method: " + e.getMessage()))
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
