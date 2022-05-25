package com.epam.shop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product")
@EqualsAndHashCode
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String productName;

    private Integer price;

    private Integer quantity;

    private byte[] photo;


    @Column(name = "category_id")
    private Integer categoryId;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

}
