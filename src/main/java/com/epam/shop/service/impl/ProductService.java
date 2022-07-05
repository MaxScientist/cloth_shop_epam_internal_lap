package com.epam.shop.service.impl;


import com.epam.shop.entity.Product;
import com.epam.shop.repository.ProductRepository;
import com.epam.shop.service.GenericService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService extends GenericService<Product, Integer, ProductRepository> {

    public List<Product> findProductsByCategoryId(int id) {
        return super.r.findProductsByCategoryId(id);
    }

    public void updateById(int id, Product product) {
        Product oldProduct = super.findById(id);
        oldProduct.setProductName(product.getProductName());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setQuantity(product.getQuantity());
        oldProduct.setCategoryId(product.getCategoryId());
        super.save(oldProduct);
    }
}
