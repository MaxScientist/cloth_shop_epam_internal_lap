package com.epam.shop.repository;


import com.epam.shop.entity.Order;
import com.epam.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT a.items FROM Order a WHERE a.id = ?1")
    List<Product> findProductsByOrderId(int id);
}
