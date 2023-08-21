package com.innovature.rentx.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wishlist {

    public enum STATUS {

        ACTIVE((byte) 0),
        DELETED((byte) 1);
        public final byte value;

        STATUS(byte value) {this.value = value; }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name =  "productId", referencedColumnName = "id")
    private Product product;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User userId;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Byte status=Wishlist.STATUS.ACTIVE.value;

    public Wishlist(Product product, User user){
        this.product = product;
        this.userId=user;
    }
}
