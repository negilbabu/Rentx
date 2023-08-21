package com.innovature.rentx.view;

import com.innovature.rentx.entity.Store;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class VendorStoreDetailView {

    private final int id;
    private final int user;
    private final byte userStatus;
    private final String name;
    private final String mobile;
    private final String pincode;
    private final String city;
    private final String state;
    private final String district;
    private final String country;
    private final String buildingName;
    private final byte status;
    private final String lattitude;
    private final String longitude;

    public VendorStoreDetailView(Store store) {
        this.id = store.getId();
        this.user = store.getUser().getId();
        this.userStatus = store.getUser().getStatus();

        this.name = store.getName();
        this.mobile = store.getMobile();
        this.pincode = store.getPincode();
        this.city = store.getCity();
        this.state=store.getState();
        this.district=store.getDistrict();
        this.country=store.getCountry();
        this.buildingName=store.getBuildingName();
        this.status=store.getStatus();
        this.lattitude=store.getLattitude();
        this.longitude=store.getLongitude();
    }
    
}

