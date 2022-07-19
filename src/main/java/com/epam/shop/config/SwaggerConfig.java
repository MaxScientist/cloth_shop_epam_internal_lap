package com.epam.shop.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public static final String CATEGORY = "category";
    public static final String ORDER = "order";
    public static final String PRODUCT = "product";
    public static final String USER = "user";
    public static final String VENDOR = "vendor";

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                .apis(Predicate.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .build()
                .tags(new Tag(CATEGORY, "Categories of our products"))
                .tags(new Tag(ORDER, "Access to orders"))
                .tags(new Tag(PRODUCT, "Products and all other stuff"))
                .tags(new Tag(USER, "user base"))
                .tags(new Tag(VENDOR, "vendors make a product"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Cloth shop").description("This is a sample API for Spring Boot application")
                .version("1.0.0").build();
    }
}
