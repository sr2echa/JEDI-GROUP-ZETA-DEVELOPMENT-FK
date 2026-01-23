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

    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }
    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean approved) { isApproved = approved; }
    public String getCenterId() { return centerId; }
    public void setCenterId(String centerId) { this.centerId = centerId; }
}