package com.flipfit.bean;
import java.util.List;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class Customer.
 *
 * @author Zeta
 * @ClassName  "Customer"
 */
public class Customer extends User {
    private List<Booking> bookings;

    /**
     * Gets the bookings.
     *
     * @return the bookings
     */
    public List<Booking> getBookings() { return bookings; }

    /**
     * Sets the bookings.
     *
     * @param bookings the new bookings
     */
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}