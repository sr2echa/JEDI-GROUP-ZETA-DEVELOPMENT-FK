package com.flipfit.bean;

import java.time.LocalTime;

public class SlotMaster {
    private String slotId;
    private String centerId;
    private LocalTime startTime;
    private LocalTime endTime;
    private int capacity = 5; // Default capacity for testing
    private int availableSeats = 5;
    private double price = 500.0;
    private boolean isApproved = false;
    
    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        this.availableSeats = capacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public double getPrice() {
    	return price;
    }
    
    public void setPrice(double price)
    {
    	this.price = price;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
