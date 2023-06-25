package com.epam.shop.controller;


import com.epam.shop.entity.Category;
import com.epam.shop.entity.Product;
import com.epam.shop.service.impl.CategoryServiceImpl;
import com.epam.shop.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class TestUtils {

    @Autowired
    private CategoryServiceImpl _categoryService;

    @Autowired
    private ProductServiceImpl _productService;

    private static CategoryServiceImpl categoryService;
    private static ProductServiceImpl productService;


    @PostConstruct
    public void init() {
        categoryService = _categoryService;
        productService = _productService;
    }


    protected static List<Product> createProducts() {
        Category categoryToSave = createCategory();
        int categoryId = categoryService.save(categoryToSave).getId();
        List<Product> products = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Product product = new Product();
            product.setProductName(i + "product");
            product.setPrice(i*1000);
            product.setQuantity(i*10);
            product.setCategoryId(categoryId);

            Product savedProduct = productService.save(product);
            products.add(savedProduct);
        }
        return products;
    }

    protected static Category saveCategory(Category category) {
        return categoryService.save(category);
    }

    protected static Category createCategory() {
        Category category = new Category();
        category.setName("1st Category");
        category.setProducts(new ArrayList<>());
        return category;
    }

    protected static Product createProduct() {
        Category categoryToSave = createCategory();
        int categoryId = saveCategory(categoryToSave).getId();
        Product product = new Product();
        product.setProductName("product");
        product.setPrice(1000);
        product.setQuantity(10);
        product.setCategoryId(categoryId);
        return product;
    }


    protected static void createCategories() {
        IntStream.range(0, 5).forEachOrdered(i -> {
            Category category = new Category();
            category.setName(i + " category");
            categoryService.save(category);
        });
    }

}
