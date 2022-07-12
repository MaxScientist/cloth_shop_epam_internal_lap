package com.epam.shop.mapper;

import com.epam.shop.dto.UserPostDTO;
import com.epam.shop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPostMapper extends DTOMapper<User, UserPostDTO> {
}
