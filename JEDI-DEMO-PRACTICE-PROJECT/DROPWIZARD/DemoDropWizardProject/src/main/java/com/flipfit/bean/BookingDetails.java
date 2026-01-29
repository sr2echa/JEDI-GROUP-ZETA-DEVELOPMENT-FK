package com.flipfit.bean;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class BookingDetails.
 *
 * @author Zeta
 * @ClassName  "BookingDetails"
 */
public class BookingDetails {
    private String detailsId;
    private double paidAmount;

    /**
     * Gets the details id.
     *
     * @return the details id
     */
    public String getDetailsId() { return detailsId; }

    /**
     * Sets the details id.
     *
     * @param detailsId the new details id
     */
    public void setDetailsId(String detailsId) { this.detailsId = detailsId; }

    /**
     * Gets the paid amount.
     *
     * @return the paid amount
     */
    public double getPaidAmount() { return paidAmount; }

    /**
     * Sets the paid amount.
     *
     * @param paidAmount the new paid amount
     */
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }
}