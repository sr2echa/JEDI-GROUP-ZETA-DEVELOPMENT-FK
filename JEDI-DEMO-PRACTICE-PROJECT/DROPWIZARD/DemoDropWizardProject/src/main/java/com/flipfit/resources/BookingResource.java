package com.flipfit.resources;

import com.flipfit.bean.Booking;
import com.flipfit.business.CustomerInterface;
import com.flipfit.business.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * BookingResource - REST endpoints for booking operations
 */
@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    private final CustomerService customerService = new CustomerService();

    private final com.flipfit.business.BookingService bookingService = new com.flipfit.business.BookingService();

    /**
     * Get booking by ID
     * GET /api/bookings/{bookingId}
     */
    @GET
    @Path("/{bookingId}")
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

    /**
     * Add to Waitlist
     * POST /api/bookings/waitlist
     */
    @POST
    @Path("/waitlist")
    public Response addToWaitlist(Map<String, String> request) {
        try {
            String userId = request.get("userId");
            String slotId = request.get("slotId");

            int position = bookingService.addCustomerToWaitlist(userId, slotId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Added to waitlist. Position: " + position);

            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Waitlist failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get User Waitlist
     * GET /api/bookings/waitlist/{userId}
     */
    @GET
    @Path("/waitlist/{userId}")
    public Response getUserWaitlist(@PathParam("userId") String userId) {
        try {
            java.util.List<String> waitlist = bookingService.getUserWaitlist(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("waitlist", waitlist);

            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Failed to fetch waitlist: " + e.getMessage()))
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
