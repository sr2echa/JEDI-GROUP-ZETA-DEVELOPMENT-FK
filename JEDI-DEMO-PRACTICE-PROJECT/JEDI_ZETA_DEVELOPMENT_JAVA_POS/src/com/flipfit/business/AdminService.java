/**
 * 
 */
package com.flipfit.business;

import com.flipfit.bean.GymCenter;

/**
 * 
 */
public class AdminService implements AdminInterface {
    @Override
    public void approveGymOwner(String ownerId) {
        System.out.println("[ADMIN] Gym Owner " + ownerId + " has been approved.");
    }
    @Override
    public void onboardCenter(GymCenter center) {
        System.out.println("[ADMIN] Gym Center " + center.getName() + " onboarded successfully.");
    }
}
