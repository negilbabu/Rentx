package com.innovature.rentx.entity;

import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;
import com.innovature.rentx.form.ProductForm;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {

    public enum Status {
        ACTIVE((byte) 0),
        INACTIVE((byte) 1),
        DELETED((byte) 2),
        INACTIVE_BY_STORE_DELETED((byte) 3),
        INACTIVE_BY_VENDOR_BLOCKED((byte) 4),
        DELETED_BY_CATEGORY_DELETED((byte) 5),
        DELETED_BY_SUB_CATEGORY_DELETED((byte) 6),



        
        ;

        public final byte value;

        Status(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    private Category category;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "subCategoryId", referencedColumnName = "id")
    private SubCategory subCategory;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "storeId", referencedColumnName = "id")
    private Store store;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_specification", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    @MapKeyColumn(name = "spec_key")
    @Column(name = "spec_value")
    private Map<String, String> specification;

    private String description;

    private byte status = Status.INACTIVE.value;

    private int stock;

    private int availableStock;

    private double price;

    private String coverImage;

    private String thumbnail;

    @Column(updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Product() {
    }

    public Product(ProductForm form, Category categoryId, SubCategory subCategoryId, Store storeId) {
        this.name = form.getName();
        this.description = form.getDescription();
        this.specification = form.getSpecification();
        this.stock = form.getStock();
        this.availableStock = form.getStock();
        this.price = form.getPrice();
        this.coverImage = form.getCoverImage();
        this.thumbnail = form.getThumbnail();

        this.category = categoryId;
        this.subCategory = subCategoryId;
        this.store = storeId;

    }

}