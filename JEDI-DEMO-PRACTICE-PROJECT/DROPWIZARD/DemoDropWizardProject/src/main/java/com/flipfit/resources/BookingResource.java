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

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", message);
        return error;
    }
}
