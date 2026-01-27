package com.flipfit.dao;

import com.flipfit.bean.PaymentRecord;
import java.util.List;

public interface PaymentDAO {
    public void savePayment(PaymentRecord payment);

    public List<PaymentRecord> getPaymentHistory(String userId);

    public List<PaymentRecord> getCenterRevenue(String centerId);

    public List<PaymentRecord> getPaymentsByBookingId(String bookingId);
}
