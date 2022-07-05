package com.epam.shop.dto;


import com.epam.shop.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends DTOMapper<Product, ProductDTO> {
}
