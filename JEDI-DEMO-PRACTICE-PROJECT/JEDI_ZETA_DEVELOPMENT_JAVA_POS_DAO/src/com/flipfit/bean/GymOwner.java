package com.flipfit.bean;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class GymOwner.
 *
 * @author Zeta
 * @ClassName  "GymOwner"
 */
public class GymOwner extends User {
    private String panNumber;
    private boolean isApproved = false;
    private String centerId;
    private String gstNumber;
    private String aadharNumber;
    private String location;

    /**
     * Gets the pan number.
     *
     * @return the pan number
     */
    public String getPanNumber() { return panNumber; }
    
    /**
     * Sets the pan number.
     *
     * @param panNumber the new pan number
     */
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }
    
    /**
     * Checks if is approved.
     *
     * @return true, if is approved
     */
    public boolean isApproved() { return isApproved; }
    
    /**
     * Sets the approved.
     *
     * @param approved the new approved
     */
    public void setApproved(boolean approved) { isApproved = approved; }
    
    /**
     * Gets the center id.
     *
     * @return the center id
     */
    public String getCenterId() { return centerId; }
    
    /**
     * Sets the center id.
     *
     * @param centerId the new center id
     */
    public void setCenterId(String centerId) { this.centerId = centerId; }
    
    /**
     * Gets the gst number.
     *
     * @return the gst number
     */
    public String getGstNumber() { return gstNumber; }
    
    /**
     * Sets the gst number.
     *
     * @param gstNumber the new gst number
     */
    public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }
    
    /**
     * Gets the aadhar number.
     *
     * @return the aadhar number
     */
    public String getAadharNumber() { return aadharNumber; }
    
    /**
     * Sets the aadhar number.
     *
     * @param aadharNumber the new aadhar number
     */
    public void setAadharNumber(String aadharNumber) { this.aadharNumber = aadharNumber; }
    
    /**
     * Gets the location.
     *
     * @return the location
     */
    public String getLocation() { return location; }
    
    /**
     * Sets the location.
     *
     * @param location the new location
     */
    public void setLocation(String location) { this.location = location; }
}