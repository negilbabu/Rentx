package com.innovature.rentx.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vendor_details")
public class VendorDetails {

    @Id
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Integer userId;
    private String accountNumber;
    private String holderName;
    private String ifsc;
    private String gst;
    private String pan;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
