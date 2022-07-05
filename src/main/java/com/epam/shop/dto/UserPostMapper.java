package com.epam.shop.dto;

import com.epam.shop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPostMapper extends DTOMapper<User, UserPostDTO> {
}
