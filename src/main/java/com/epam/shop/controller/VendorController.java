package com.epam.shop.controller;


import com.epam.shop.config.SwaggerConfig;
import com.epam.shop.mapper.DTOMapper;
import com.epam.shop.dto.ProductDTO;
import com.epam.shop.dto.VendorDTO;
import com.epam.shop.entity.Product;
import com.epam.shop.entity.Vendor;
import com.epam.shop.service.impl.VendorServiceImpl;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = {SwaggerConfig.VENDOR})
@RequestMapping("/api/vendors")
@PreAuthorize("hasAnyAuthority('READ','WRITE')")
@RequiredArgsConstructor
public class VendorController {

    private final VendorServiceImpl vendorService;
    private final DTOMapper<Vendor, VendorDTO> vendorMapper;
    private final DTOMapper<Product, ProductDTO> productMapper;

    @GetMapping
    public ResponseEntity<?> findAllVendors() {
        try {
            List<Vendor> vendors = vendorService.findAll();
            List<VendorDTO> vendorDTOS = vendors.stream()
                    .map(vendorMapper::toDTO).collect(Collectors.toList());
            return new ResponseEntity<>(vendorDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity<?> saveVendor(@RequestBody VendorDTO vendorDTO) {
        try {
            Vendor vendor = vendorService.save(vendorMapper.fromDTO(vendorDTO));
            VendorDTO vendorDTO1 = vendorMapper.toDTO(vendor);
            return new ResponseEntity<>(vendorDTO1, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findVendorById(@PathVariable("id") int id) {
        try {
            Vendor vendor = vendorService.findById(id);
            return new ResponseEntity<>(vendorMapper.toDTO(vendor), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity<?> updateVendorById(@PathVariable("id") int id, @RequestBody VendorDTO vendorDTO) {
        try {
            vendorService.updateVendorById(id, vendorMapper.fromDTO(vendorDTO));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity<?> deleteVendorById(@PathVariable("id") int id) {
        try {
            vendorService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}/products")
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity<?> findProductsByVendorId(@PathVariable("id") int id) {
        try {
            List<Product> products = vendorService.findProductsByVendorId(id);
            List<ProductDTO> productDTOS = products.stream().map(productMapper::toDTO)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(productDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{id}/products")
    public ResponseEntity<?> saveProductToVendor(@PathVariable("id") int id, @RequestBody ProductDTO productDTO) {
        try {
            vendorService.saveProductToVendor(id, productMapper.fromDTO(productDTO));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
