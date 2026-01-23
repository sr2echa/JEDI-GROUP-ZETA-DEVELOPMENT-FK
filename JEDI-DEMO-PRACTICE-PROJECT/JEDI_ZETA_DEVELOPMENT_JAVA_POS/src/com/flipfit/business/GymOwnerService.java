/**
 * 
 */
package com.flipfit.business;

/**
 * 
 */
public class GymOwnerService implements GymOwnerInterface {
    @Override
    public void manageSlots(String centerId) {
        System.out.println("[OWNER] Managing slots for Center: " + centerId);
    }
    @Override
    public void updateSlotCapacity(String scheduleId, int newCapacity) {
        System.out.println("[OWNER] Schedule " + scheduleId + " updated to capacity: " + newCapacity);
    }
}
