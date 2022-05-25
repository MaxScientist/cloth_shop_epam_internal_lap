package com.epam.shop.mapper;


import org.springframework.stereotype.Service;

@Service
public interface DTOMapper<T, S> {
    T fromDTO(S var);
    S toDTO(T var);
}
