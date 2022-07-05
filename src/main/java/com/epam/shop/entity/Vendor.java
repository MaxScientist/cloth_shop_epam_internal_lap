package com.epam.shop.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "vendor")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = false)
    @JoinColumn(name = "vendor_id", nullable = true)
    private List<Product> products;
}
