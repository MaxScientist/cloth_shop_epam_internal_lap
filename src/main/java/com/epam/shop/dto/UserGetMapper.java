package com.epam.shop.dto;


import com.epam.shop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserGetMapper
        extends DTOMapper<User, UserGetDTO> {
}
