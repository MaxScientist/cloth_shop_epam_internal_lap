package com.epam.shop.dto;

import com.epam.shop.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper extends DTOMapper<Order,OrderDTO>{
}
