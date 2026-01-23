/**
 * 
 */
package com.flipfit.bean;

import java.util.List;

/**
 * 
 */
public class Customer extends User {
    private List<Booking> bookings;
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}
