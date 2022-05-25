package com.epam.shop.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDTO {

    private int id;
    private String shipDate;
    private String dateTime;
    private String status;
    private boolean complete;
    private List<ProductDTO> items;

}
