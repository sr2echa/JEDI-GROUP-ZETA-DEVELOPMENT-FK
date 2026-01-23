/**
 * 
 */
package com.flipfit.business;
import com.flipfit.bean.Booking;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 */
public class CustomerService implements CustomerInterface {
    @Override
    public void bookWorkout(String userId, String scheduleId) {
        System.out.println("[CUSTOMER] Booking successful for User: " + userId);
    }
    @Override
    public void cancelWorkout(String bookingId) {
        System.out.println("[CUSTOMER] Booking " + bookingId + " cancelled. Waitlist check triggered.");
    }
    @Override
    public List<Booking> getCustomerPlan(String userId) {
        return new ArrayList<>();
    }
}