package com.epam.shop.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class ProductDTO {

    private int id;
    private String name;
    private int price;
    private int quantity;
    private int categoryId;
}
