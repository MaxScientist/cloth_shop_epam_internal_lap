package com.epam.shop.repository;


import com.epam.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT a FROM Product a WHERE a.categoryId = ?1")
    List<Product> findProductsByCategoryId(int categoryId);
}
