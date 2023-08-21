package com.innovature.rentx.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressForm {

    @NotBlank(message = "{receiver.name.required}")
    @Size(min = 3, max = 20, message = "{receiver.name.must.be.between.3.and.20}")
    private String name;

    @NotBlank(message = "{houseName.required}")
    @Size(min = 3, max = 20, message = "{houseName.must.be.between.3.and.20}")
    private String houseName;

    @NotBlank(message = "{phone.should.required}")
    @Size(min = 10, max = 15, message = "{size.must.be.between.10.and.15}")
    @Pattern(regexp = "^[0123456789]+$", message = "{phone.should.be.numbers}")
    private String phone;

    @NotBlank(message = "{pincode.should.required}")
    @Size(max = 8, min = 6, message = "{pincode.size.must.be}")
    @Pattern(regexp = "^[0123456789]+$", message = "{pincode.should.be}")
    private String pinCode;

    @NotBlank(message = "{city.should.required}")
    @Size(max = 20, min = 3, message = "{city.size.must.be}")
    private String city;

    @NotBlank(message = "{state.should.required}")
    @Size(min = 3, max = 20, message = "{state.must.be.between}")
    private String state;

    @Min(value = 0, message = "{invalid.type}")
    @Max(value = 2, message = "{invalid.type}")
    private byte type;
    
}
