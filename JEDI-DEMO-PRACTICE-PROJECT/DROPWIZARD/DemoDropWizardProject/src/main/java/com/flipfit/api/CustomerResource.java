package com.flipfit.api;

import com.flipfit.api.dto.ApiResponse;
import com.flipfit.bean.*;
import com.flipfit.business.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    
    private final CustomerService customerService;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;
    
    public CustomerResource() {
        this.customerService = new CustomerService();
        this.bookingService = new BookingService();
        this.paymentService = new PaymentService();
        this.notificationService = new NotificationService();
    }
    
    @POST
    @Path("/book-slot")
    public Response bookSlot(Booking booking) {
        try {
            boolean success = bookingService.bookSlot(
                booking.getUserId(),
                booking.getSlotId(),
                booking.getBookingDate()
            );
            if (success) {
                return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse(true, "Slot booked successfully"))
                    .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Booking failed. Slot may be full or unavailable."))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Booking failed: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/plan/{customerId}")
    public Response getCustomerPlan(@PathParam("customerId") int customerId) {
        try {
            List<BookingDetails> bookings = customerService.viewMyPlan(customerId);
            return Response.ok(new ApiResponse(true, "Plan retrieved", bookings)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve plan: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/pending-bookings/{customerId}")
    public Response getPendingBookings(@PathParam("customerId") int customerId) {
        try {
            List<BookingDetails> bookings = customerService.viewPendingBookings(customerId);
            return Response.ok(new ApiResponse(true, "Pending bookings retrieved", bookings)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve pending bookings: " + e.getMessage()))
                .build();
        }
    }
    
    @POST
    @Path("/pay-booking/{bookingId}")
    public Response payForBooking(@PathParam("bookingId") int bookingId, @QueryParam("amount") double amount) {
        try {
            boolean success = paymentService.processPayment(bookingId, amount);
            if (success) {
                return Response.ok(new ApiResponse(true, "Payment processed successfully")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Payment processing failed"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Payment failed: " + e.getMessage()))
                .build();
        }
    }
    
    @DELETE
    @Path("/cancel-booking/{bookingId}")
    public Response cancelBooking(@PathParam("bookingId") int bookingId) {
        try {
            boolean success = bookingService.cancelBooking(bookingId);
            if (success) {
                return Response.ok(new ApiResponse(true, "Booking cancelled successfully")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Cancellation failed"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Cancellation failed: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/payment-history/{customerId}")
    public Response getPaymentHistory(@PathParam("customerId") int customerId) {
        try {
            List<PaymentRecord> history = paymentService.getPaymentHistory(customerId);
            return Response.ok(new ApiResponse(true, "Payment history retrieved", history)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve payment history: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/notifications/{userId}")
    public Response getNotifications(@PathParam("userId") int userId) {
        try {
            List<Notification> notifications = notificationService.getNotificationsByUser(userId);
            return Response.ok(new ApiResponse(true, "Notifications retrieved", notifications)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve notifications: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/centers")
    public Response browseCenters(@QueryParam("city") String city) {
        try {
            List<GymCenter> centers;
            if (city != null && !city.isEmpty()) {
                centers = customerService.viewCentersByCity(city);
            } else {
                centers = customerService.viewAllCenters();
            }
            return Response.ok(new ApiResponse(true, "Centers retrieved", centers)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve centers: " + e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/slots/{centerId}")
    public Response getSlotsByCenter(@PathParam("centerId") int centerId) {
        try {
            List<SlotMaster> slots = customerService.viewSlotsByCenter(centerId);
            return Response.ok(new ApiResponse(true, "Slots retrieved", slots)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(false, "Failed to retrieve slots: " + e.getMessage()))
                .build();
        }
    }
}
