package com.innovature.rentx.view;

import com.innovature.rentx.entity.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressView {

    private Integer id;
    private String name;
    private String houseName;
    private String phone;
    private String pinCode;
    private String city;
    private String state;
    private byte type;

    public UserAddressView(Address address){

        this.id=address.getId();
        this.name=address.getName();
        this.houseName=address.getHouseName();
        this.phone=address.getPhone();
        this.pinCode=address.getPinCode();
        this.city=address.getCity();
        this.state=address.getState();
        this.type=address.getType();
       
    }
    
}
