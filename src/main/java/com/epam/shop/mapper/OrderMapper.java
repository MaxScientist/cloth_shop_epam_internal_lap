package com.epam.shop.mapper;

import com.epam.shop.dto.OrderDTO;
import com.epam.shop.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper extends DTOMapper<Order, OrderDTO> {
}
