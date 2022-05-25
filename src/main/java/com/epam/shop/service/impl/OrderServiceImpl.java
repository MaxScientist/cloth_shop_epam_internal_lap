package com.epam.shop.service.impl;


import com.epam.shop.entity.Order;
import com.epam.shop.entity.Product;
import com.epam.shop.entity.Status;
import com.epam.shop.repository.OrderRepository;
import com.epam.shop.repository.ProductRepository;
import com.epam.shop.service.GenericServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl extends GenericServiceImpl<Order, Integer, OrderRepository> {

    private ProductRepository productRepository;

    public List<Product> findProductsByOrderId(int id) {
        return super.r.findProductsByOrderId(id);
    }

    public void saveProductToOrder(int id, Product product) {
        Order order = super.findById(id);
        List<Product> products = order.getItems();
        productRepository.save(product);
        products.add(product);
        order.setItems(products);
        super.save(order);
    }

    public Product findProductByOrderIdItemId(int orderId, int productId) {
        return findProductsByOrderId(orderId).stream()
                .filter(product -> product.getId() == productId)
                .findFirst().orElseThrow(EntityNotFoundException::new);
    }

    public void deleteProductFromOrder(int orderId, int productId) {
        Order order = super.findById(orderId);
        List<Product> products = order.getItems();
        products.remove(findProductByOrderIdItemId(orderId, productId));
        order.setItems(products);
        super.save(order);
    }

    public void cancelOrder(int id) {
        Order order = super.findById(id);
        order.setStatus(Status.CANCELED);
        super.save(order);
    }
}
