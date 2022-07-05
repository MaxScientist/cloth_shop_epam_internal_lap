package com.epam.shop.dto;

import com.epam.shop.entity.Vendor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VendorMapper extends DTOMapper<Vendor, VendorDTO>{
}
