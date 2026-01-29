package com.flipfit.bean;
import java.time.LocalTime;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class SlotMaster.
 *
 * @author Zeta
 * @ClassName  "SlotMaster"
 */
public class SlotMaster {
    private String slotId;
    private String centerId;
    private LocalTime startTime;
    private LocalTime endTime;
    private int capacity = 5; 
    private int availableSeats = 5;
    private double price = 500.0;
    private boolean isApproved = false;
    
    /**
     * Gets the slot id.
     *
     * @return the slot id
     */
    public String getSlotId() { return slotId; }

    /**
     * Sets the slot id.
     *
     * @param slotId the new slot id
     */
    public void setSlotId(String slotId) { this.slotId = slotId; }

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
     * Gets the start time.
     *
     * @return the start time
     */
    public LocalTime getStartTime() { return startTime; }

    /**
     * Sets the start time.
     *
     * @param startTime the new start time
     */
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    /**
     * Gets the end time.
     *
     * @return the end time
     */
    public LocalTime getEndTime() { return endTime; }

    /**
     * Sets the end time.
     *
     * @param endTime the new end time
     */
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    /**
     * Gets the capacity.
     *
     * @return the capacity
     */
    public int getCapacity() { return capacity; }

    /**
     * Sets the capacity.
     *
     * @param capacity the new capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
        this.availableSeats = capacity;
    }

    /**
     * Gets the available seats.
     *
     * @return the available seats
     */
    public int getAvailableSeats() { return availableSeats; }

    /**
     * Sets the available seats.
     *
     * @param availableSeats the new available seats
     */
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    
    /**
     * Gets the price.
     *
     * @return the price
     */
    public double getPrice() { return price; }
    
    /**
     * Sets the price.
     *
     * @param price the new price
     */
    public void setPrice(double price) { this.price = price; }

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