package com.flipfit.bean;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class GymCenter.
 *
 * @author Zeta
 * @ClassName  "GymCenter"
 */
public class GymCenter {
    private String centerId;
    private String name;
    private String city;
    private String address;
    private String ownerId;
    private boolean isApproved;

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
     * Gets the name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() { return city; }

    /**
     * Sets the city.
     *
     * @param city the new city
     */
    public void setCity(String city) { this.city = city; }

    /**
     * Gets the address.
     *
     * @return the address
     */
    public String getAddress() { return address; }

    /**
     * Sets the address.
     *
     * @param address the new address
     */
    public void setAddress(String address) { this.address = address; }

    /**
     * Gets the owner id.
     *
     * @return the owner id
     */
    public String getOwnerId() { return ownerId; }

    /**
     * Sets the owner id.
     *
     * @param ownerId the new owner id
     */
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

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
}