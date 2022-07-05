package com.epam.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class ClothShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClothShopApplication.class, args);
    }

}
