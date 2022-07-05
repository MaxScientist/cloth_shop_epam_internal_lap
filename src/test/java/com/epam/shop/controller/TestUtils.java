package com.epam.shop.controller;

import com.epam.shop.entity.Category;
import com.epam.shop.service.impl.CategoryService;
import com.epam.shop.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class TestUtils {

    private CategoryService _categoryService;
    private ProductService _productService;

    private static CategoryService categoryService;
    private static ProductService productService;


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
