package com.epam.shop.mapper;

import com.epam.shop.dto.VendorDTO;
import com.epam.shop.entity.Vendor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VendorMapper extends DTOMapper<Vendor, VendorDTO> {
}
