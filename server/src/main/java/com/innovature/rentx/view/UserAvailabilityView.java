package com.innovature.rentx.view;

import lombok.Getter;

@Getter
public class UserAvailabilityView{

    private final Boolean isAvailable;
    private final Integer remainingCount;

    public UserAvailabilityView(Boolean isAvailable, Integer remainingCount) {

        this.isAvailable=isAvailable;
        this.remainingCount=remainingCount;
    }


}
