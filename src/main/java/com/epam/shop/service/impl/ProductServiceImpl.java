package com.epam.shop.service.impl;


import com.epam.shop.entity.Product;
import com.epam.shop.repository.ProductRepository;
import com.epam.shop.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends GenericServiceImpl<Product, Integer, ProductRepository> {

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

    public void updatePhotoOfProduct(int id, byte[] photoBytes){
        Product product = super.findById(id);
        product.setPhoto(photoBytes);
        super.save(product);
    }
}
