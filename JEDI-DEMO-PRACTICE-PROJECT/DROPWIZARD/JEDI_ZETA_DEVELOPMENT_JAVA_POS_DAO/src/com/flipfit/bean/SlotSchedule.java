package com.flipfit.bean;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class SlotSchedule.
 *
 * @author Zeta
 * @ClassName  "SlotSchedule"
 */
public class SlotSchedule {
    private String scheduleId;
    private int availableSeats;

    /**
     * Gets the schedule id.
     *
     * @return the schedule id
     */
    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * Sets the schedule id.
     *
     * @param scheduleId the new schedule id
     */
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    /**
     * Gets the available seats.
     *
     * @return the available seats
     */
    public int getAvailableSeats() {
        return availableSeats;
    }

    /**
     * Sets the available seats.
     *
     * @param availableSeats the new available seats
     */
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}