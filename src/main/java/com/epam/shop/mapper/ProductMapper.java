package com.epam.shop.mapper;


import com.epam.shop.dto.ProductDTO;
import com.epam.shop.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends DTOMapper<Product, ProductDTO> {
}
