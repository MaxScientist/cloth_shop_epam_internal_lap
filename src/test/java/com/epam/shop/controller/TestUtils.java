package com.epam.shop.controller;

import com.epam.shop.entity.Category;
import com.epam.shop.service.impl.CategoryServiceImpl;
import com.epam.shop.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class TestUtils {

    private CategoryServiceImpl _categoryService;
    private ProductServiceImpl _productService;

    private static CategoryServiceImpl categoryService;
    private static ProductServiceImpl productService;


    @PostConstruct
    public void init() {
        categoryService = _categoryService;
        productService = _productService;
    }

    public static void populateCategories() {
        IntStream.range(1, 6).mapToObj(i -> new Category()).forEachOrdered(category -> {
            category.setName("my_category");
            category.setProducts(new ArrayList<>());
            categoryService.save(category);
        });
    }
}
