package com.epam.shop.controller;


import com.epam.shop.config.SwaggerConfig;
import com.epam.shop.dto.CategoryDTO;
import com.epam.shop.mapper.DTOMapper;
import com.epam.shop.dto.ProductDTO;
import com.epam.shop.entity.Category;
import com.epam.shop.entity.Product;
import com.epam.shop.service.impl.CategoryServiceImpl;
import com.epam.shop.service.impl.ProductServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@Api(tags = {SwaggerConfig.CATEGORY})
@PreAuthorize("hasAnyAuthority('READ', 'WRITE')")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceImpl categoryService;
    private final ProductServiceImpl productService;
    private final DTOMapper<Category, CategoryDTO> categoryMapper;
    private final DTOMapper<Product, ProductDTO> productMapper;


    @GetMapping
    @ApiOperation(value = "Product Categories")
    @PreAuthorize("WRITE")
    public ResponseEntity<Object> findAllCategories() {
        try {
            List<Category> categories = categoryService.findAll();
            List<CategoryDTO> categoryDTOS = categories.stream()
                    .map(categoryMapper::toDTO).collect(Collectors.toList());
            return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @ApiOperation(value = "Create category")
    public ResponseEntity<Object> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            Category category = categoryMapper.fromDTO(categoryDTO);
            CategoryDTO result = categoryMapper.toDTO(categoryService.save(category));
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Individual Category")
    public ResponseEntity<Object> findCategoryById(@PathVariable("id") int id) {
        try {
            Category category = categoryService.findById(id);
            return new ResponseEntity<>(categoryMapper.toDTO(category), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}/products")
    @ApiOperation(value = "Get the products of a category")
    public ResponseEntity<Object> findProductsByCategoryId(@PathVariable("id") int categoryId) {
        try {
            List<Product> products = productService.findProductsByCategoryId(categoryId);
            List<ProductDTO> productDTOS = products.stream()
                    .map(productMapper::toDTO).collect(Collectors.toList());
            return new ResponseEntity<>(productDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
