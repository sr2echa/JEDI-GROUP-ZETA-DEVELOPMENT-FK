/**
 * 
 */
package com.flipfit.bean;

/**
 * 
 */

public class GymOwner extends User {
    private String panNumber;
    private boolean isApproved = false;
    private String centerId;
    private String gstNumber;
    private String aadharNumber;
    private String location;

    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }
    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean approved) { isApproved = approved; }
    public String getCenterId() { return centerId; }
    public void setCenterId(String centerId) { this.centerId = centerId; }
    public String getGstNumber() { return gstNumber; }
    public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }
    public String getAadharNumber() { return aadharNumber; }
    public void setAadharNumber(String aadharNumber) { this.aadharNumber = aadharNumber; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}