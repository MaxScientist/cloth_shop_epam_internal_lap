package com.epam.shop.mapper;


import com.epam.shop.dto.UserGetDTO;
import com.epam.shop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserGetMapper
        extends DTOMapper<User, UserGetDTO> {
}
