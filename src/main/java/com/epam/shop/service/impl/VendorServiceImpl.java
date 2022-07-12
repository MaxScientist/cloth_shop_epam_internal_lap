package com.epam.shop.service.impl;

import com.epam.shop.entity.Product;
import com.epam.shop.entity.Vendor;
import com.epam.shop.repository.ProductRepository;
import com.epam.shop.repository.VendorRepository;
import com.epam.shop.service.GenericServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VendorServiceImpl extends GenericServiceImpl<Vendor, Integer, VendorRepository> {

    private ProductRepository productRepository;

    public void updateVendorById(int id, Vendor vendor) {
        Vendor oldVendor = super.findById(id);
        oldVendor.setName(vendor.getName());
        oldVendor.setProducts(vendor.getProducts());
        super.save(oldVendor);
    }

    public List<Product> findProductsByVendorId(int id) {
        Vendor vendor = super.findById(id);
        return vendor.getProducts();
    }

    public void saveProductToVendor(int id, Product product) {
        Vendor vendor = super.findById(id);
        List<Product> products = vendor.getProducts();
        productRepository.save(product);
        products.add(product);
        vendor.setProducts(products);
        super.save(vendor);
    }
}
