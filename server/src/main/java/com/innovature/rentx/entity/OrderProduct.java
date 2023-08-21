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
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {

    public enum Status{
        ACTIVE((byte) 0),
        INACTIVE((byte) 1),
        DELETED((byte) 2),
        COMPLETED((byte) 3);
        ;

        public  final byte value;
        Status(byte value) {this.value = value;}
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false,fetch= FetchType.EAGER)
    @JoinColumn(name = "orderMasterId", referencedColumnName = "id")
    private OrderProductMaster orderProductMaster;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name =  "productId", referencedColumnName = "id")
    private Product product;

    private Integer quantity;

    private Double totalPrice;

    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;

    private byte status;
    @Column(updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
