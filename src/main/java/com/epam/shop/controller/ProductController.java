package com.epam.shop.controller;


import com.epam.shop.config.SwaggerConfig;
import com.epam.shop.mapper.DTOMapper;
import com.epam.shop.dto.ProductDTO;
import com.epam.shop.entity.Product;
import com.epam.shop.service.impl.ProductServiceImpl;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = {SwaggerConfig.PRODUCT})
@PreAuthorize("hasAnyAuthority('READ','WRITE')")
@RequestMapping("/api/products")
public class ProductController {

    private final ProductServiceImpl productService;
    private final DTOMapper<Product, ProductDTO> productMapper;

    @GetMapping
    public ResponseEntity<?> findAllProducts() {
        try {
            List<Product> products = productService.findAll();
            List<ProductDTO> productDTOS = products.stream().map(productMapper::toDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(productDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<?> saveProduct(@RequestBody ProductDTO productDTO) {
        try {
            Product product = productService.save(productMapper.fromDTO(productDTO));
            return new ResponseEntity<>(productMapper.toDTO(product), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findProductById(@PathVariable("id") int id) {
        try {
            Product product = productService.findById(id);
            return new ResponseEntity<>(productMapper.toDTO(product), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable("id") int id, @RequestBody ProductDTO productDTO) {
        try {
            productService.updateById(id, productMapper.fromDTO(productDTO));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") int id) {
        try {
            productService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{id}/photo")
    public ResponseEntity<?> getPhotoOfProduct(@PathVariable("id") int id) {
        try {
            Product product = productService.findById(id);
            byte[] photo = product.getPhoto();
            String photoBase64 = Base64.encodeBase64String(photo);
            return new ResponseEntity<>(photoBase64, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/photo")
    public ResponseEntity<?> updateOrAddPhotoToProduct(@PathVariable("id") int id, @RequestBody byte[] photoBytes) {
        try {
            productService.updatePhotoOfProduct(id, photoBytes);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
