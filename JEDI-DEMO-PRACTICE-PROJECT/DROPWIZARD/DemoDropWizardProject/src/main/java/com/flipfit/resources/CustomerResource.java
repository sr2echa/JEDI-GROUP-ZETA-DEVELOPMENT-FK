package com.flipfit.resources;

import com.flipfit.bean.Booking;
import com.flipfit.business.CustomerInterface;
import com.flipfit.business.CustomerService;
import com.flipfit.exception.BookingFailedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CustomerResource - REST endpoints for customer operations
 */
@Path("/api/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private final CustomerService customerService = new CustomerService();

    /**
     * Book a workout slot
     * POST /api/customers/{userId}/bookings
     */
    @POST
    @Path("/{userId}/bookings")
    public Response bookWorkout(@PathParam("userId") String userId, Map<String, String> bookingData) {
        try {
            String slotId = bookingData.get("slotId");
            customerService.bookWorkout(userId, slotId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Booking initiated successfully. Please complete payment.");

            return Response.status(Response.Status.CREATED).entity(response).build();

        } catch (BookingFailedException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Booking failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Cancel a booking
     * DELETE /api/customers/bookings/{bookingId}
     */
    @DELETE
    @Path("/bookings/{bookingId}")
    public Response cancelWorkout(@PathParam("bookingId") String bookingId) {
        try {
            customerService.cancelWorkout(bookingId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Booking cancelled successfully");

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Cancellation failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get customer's plan (all bookings)
     * GET /api/customers/{userId}/plan
     */
    @GET
    @Path("/{userId}/plan")
    public Response getCustomerPlan(@PathParam("userId") String userId) {
        try {
            List<Booking> bookings = customerService.getCustomerPlan(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("bookings", bookings);

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get plan: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get pending payments
     * GET /api/customers/{userId}/payments/pending
     */
    @GET
    @Path("/{userId}/payments/pending")
    public Response getPendingPayments(@PathParam("userId") String userId) {
        try {
            List<Booking> pendingBookings = customerService.getPendingPayments(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("pendingBookings", pendingBookings);

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get pending payments: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Confirm booking after payment
     * POST /api/customers/bookings/{bookingId}/confirm
     */
    @POST
    @Path("/bookings/{bookingId}/confirm")
    public Response confirmBooking(@PathParam("bookingId") String bookingId, Map<String, Object> paymentData) {
        try {
            Double amount = ((Number) paymentData.get("amount")).doubleValue();
            String paymentMethod = (String) paymentData.get("paymentMethod");

            boolean success = customerService.confirmBooking(bookingId, amount, paymentMethod);

            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", "Booking confirmed successfully");

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Confirmation failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Cancel pending booking
     * DELETE /api/customers/bookings/{bookingId}/pending
     */
    @DELETE
    @Path("/bookings/{bookingId}/pending")
    public Response cancelPendingBooking(@PathParam("bookingId") String bookingId) {
        try {
            boolean success = customerService.cancelPendingBooking(bookingId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", "Pending booking cancelled");

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to cancel: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get booking by ID
     * GET /api/customers/bookings/{bookingId}
     */
    @GET
    @Path("/bookings/{bookingId}")
    public Response getBooking(@PathParam("bookingId") String bookingId) {
        try {
            Booking booking = customerService.getBookingById(bookingId);

            if (booking == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(createErrorResponse("Booking not found"))
                        .build();
            }

            return Response.ok(booking).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to get booking: " + e.getMessage()))
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
