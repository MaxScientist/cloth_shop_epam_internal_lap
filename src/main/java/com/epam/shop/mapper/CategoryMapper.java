package com.epam.shop.mapper;

import com.epam.shop.dto.CategoryDTO;
import com.epam.shop.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends DTOMapper<Category, CategoryDTO>{
}
