package com.innovature.rentx.view;

import com.innovature.rentx.entity.Address;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressView {

    private final Integer id;
    private final String name;
    private final String houseName;
    private final String phone;
    private final String pinCode;
    private final String city;
    private final String state;
    private final byte type;

    public AddressView(Address addressView){
        this.id = addressView.getId();
        this.name = addressView.getName();
        this.houseName = addressView.getHouseName();
        this.phone = addressView.getPhone();
        this.pinCode = addressView.getPinCode();
        this.city = addressView.getCity();
        this.state=addressView.getState();
        this.type = addressView.getType();

    }

    
}
