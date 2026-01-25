package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import java.util.List;

public interface AdminInterface {
    void approveGymOwner(String ownerId);

    void onboardCenter(GymCenter center);

    List<GymOwner> viewPendingGymOwners();

    List<GymCenter> viewPendingGymCenters();
}
