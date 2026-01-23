package com.flipfit.business;

import com.flipfit.bean.GymCenter;

public interface AdminInterface {
    void approveGymOwner(String ownerId);
    void onboardCenter(GymCenter center);
}
