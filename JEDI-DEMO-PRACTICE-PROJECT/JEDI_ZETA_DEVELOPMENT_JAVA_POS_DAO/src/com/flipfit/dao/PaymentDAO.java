package com.flipfit.dao;

import com.flipfit.bean.PaymentRecord;
import java.util.List;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Interface PaymentDAO.
 *
 * @author Zeta
 * @ClassName  "PaymentDAO"
 */
public interface PaymentDAO {
    
    /**
     * Save payment.
     *
     * @param payment the payment
     */
    public void savePayment(PaymentRecord payment);

    /**
     * Gets the payment history.
     *
     * @param userId the user id
     * @return the payment history
     */
    public List<PaymentRecord> getPaymentHistory(String userId);

    /**
     * Gets the center revenue.
     *
     * @param centerId the center id
     * @return the center revenue
     */
    public List<PaymentRecord> getCenterRevenue(String centerId);

    /**
     * Gets the payments by booking id.
     *
     * @param bookingId the booking id
     * @return the payments by booking id
     */
    public List<PaymentRecord> getPaymentsByBookingId(String bookingId);
}