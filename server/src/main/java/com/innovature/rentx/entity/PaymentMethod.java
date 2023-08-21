package com.innovature.rentx.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    public enum Status{
        ACTIVE((byte)0),
        INACTIVE((byte)1),
        DELETED((byte)2);

        public  final byte value;
        Status(byte value) {this.value = value;}
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private byte status;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="adminId",referencedColumnName = "id")
    private Admin admin;

    @Column(updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    public PaymentMethod(String name, byte status, Admin admin) {
        this.name = name;
        this.status = status;
        this.admin = admin;
    }
}
