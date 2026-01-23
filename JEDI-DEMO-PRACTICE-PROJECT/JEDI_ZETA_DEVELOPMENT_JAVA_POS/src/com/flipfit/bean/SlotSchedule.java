/**
 * 
 */
package com.flipfit.bean;
import java.util.Date;
/**
 * 
 */
public class SlotSchedule {
    private String scheduleId;
    private String slotId;
    private Date date;
    private int availableSeats;

    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}
