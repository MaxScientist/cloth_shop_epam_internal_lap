package com.epam.shop.dto;

import com.epam.shop.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends DTOMapper<Category, CategoryDTO>{
}
