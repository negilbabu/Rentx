package com.innovature.rentx.entity;

import javax.persistence.Entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.innovature.rentx.form.AddressForm;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    public enum Status {

        ACTIVE((byte) 0),
        INACTIVE((byte) 1),
        DELETED((byte) 2);
        public final byte value;
        Status(byte value) {
            this.value = value;
        }
    }

    public enum Type {

        HOME((byte) 0),
        OFFICE((byte) 1),
        OTHERS((byte) 2);

        public final byte value;

        Type(byte value) {this.value = value; }
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String houseName;

    private String city;

    private String state;

    private String phone;

    private String pinCode;

    private byte status;

    private byte type;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    @Column(updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    public Address(AddressForm form,User userId){
        this.name=form.getName();
        this.houseName=form.getHouseName();
        this.city=form.getCity();
        this.state=form.getState();
        this.pinCode=form.getPinCode();
        this.phone=form.getPhone();
        this.type=form.getType();
        this.userId=userId;

    } 
    
}
