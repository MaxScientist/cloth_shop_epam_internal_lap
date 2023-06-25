package com.epam.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.epam.shop")
public class ClothShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClothShopApplication.class, args);
    }

}
