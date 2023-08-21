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
public class OrderProductMaster {

    public enum Status {
        ACTIVE((byte) 0),
        INACTIVE((byte) 1),
        DELETED((byte) 2);

        public final byte value;

        Status(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="payment_id", referencedColumnName = "id")
    private PaymentMethod paymentMethod;

    private Double grantTotal;
    private Integer productCount;

    private byte status;
    @Column(updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
