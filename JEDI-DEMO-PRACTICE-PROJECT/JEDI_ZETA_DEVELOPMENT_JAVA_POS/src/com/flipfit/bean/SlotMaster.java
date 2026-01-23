/**
 * 
 */
package com.flipfit.bean;

/**
 * 
 */
import java.time.LocalTime;
public class SlotMaster {
    private String slotId;
    private LocalTime startTime;
    private LocalTime endTime;

    public String getSlotId() { return slotId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
}
