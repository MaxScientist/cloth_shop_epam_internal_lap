package com.epam.shop.controller;


import com.epam.shop.dto.ProductDTO;
import com.epam.shop.entity.Product;
import com.epam.shop.mapper.DTOMapper;
import com.epam.shop.service.impl.ProductServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.shop.controller.TestUtils.createProduct;
import static com.epam.shop.controller.TestUtils.createProducts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DTOMapper<Product, ProductDTO> productMapper;

    @Autowired
    private ProductServiceImpl productService;

    @Test
    void findAllProducts() throws Exception {
        List<Product> storedProducts = createProducts();

        String responseBody = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ProductDTO> productDTOS = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        List<Product> products = productDTOS.stream().map(productMapper::fromDTO).collect(Collectors.toList());

        assertFalse(products.isEmpty());
        assertEquals(storedProducts.toString(), products.toString());
    }

    @Test
    void saveProduct() throws Exception {
        Product product = createProduct();

        String requestBody = objectMapper.writeValueAsString(productMapper.toDTO(product));

        String httpResponse = mockMvc.perform(post("/api/products")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        ProductDTO productDTO = objectMapper.readValue(httpResponse, new TypeReference<>() {
        });
        Product productFromDTO = productMapper.fromDTO(productDTO);

        Product productFromDb = productService.findById(productFromDTO.getId());

        assertEquals(productFromDb, productFromDTO);
    }

    @Test
    void findProductById() throws Exception {
        Product productToSave = createProduct();
        Product productFromDB = productService.save(productToSave);

        String httpResponse = this.mockMvc.perform(get("/api/products/" + productFromDB.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductDTO productDTO = objectMapper.readValue(httpResponse, new TypeReference<>() {
        });
        Product product = productMapper.fromDTO(productDTO);

        assertEquals(productFromDB, product);
    }

    @Test
    void updateProduct() throws Exception {
        Product productToSave = createProduct();
        Product productFromDB = productService.save(productToSave);

        productToSave.setProductName("new_updated_name");
        String requestBody = objectMapper.writeValueAsString(productMapper.toDTO(productToSave));

        this.mockMvc.perform(put("/api/products/" + productFromDB.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk());

        Product checkFromDB = productService.findById(productFromDB.getId());

        assertEquals(productToSave, checkFromDB);
    }

    @Test
    void deleteProduct() throws Exception {
        Product productToSave = createProduct();
        Product savedProduct = productService.save(productToSave);

        this.mockMvc.perform(delete("/api/products/" + savedProduct.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        List<Product> fromDB = productService.findAll();

        assertEquals(0, fromDB.size());
    }

    @Test
    void updateOrAddPhotoOfProduct() throws Exception {
        String photoBase64 = "test";
        Product product = createProduct();
        Product savedProduct = productService.save(product);
        byte[] photoBytes = Base64.decodeBase64(photoBase64);

        String requestBody = objectMapper.writeValueAsString(photoBytes);

        this.mockMvc.perform(put("/api/products/" + savedProduct.getId() + "/photo")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Product checkFromDB = productService.findById(savedProduct.getId());

        assertEquals(checkFromDB.getPhoto(), product.getPhoto());
    }

    @Test
    void getPhotoOfProduct() throws Exception {
        Product product = createProduct();
        byte[] photoBase64 = "test".getBytes();

        product.setPhoto(photoBase64);

        Product savedProduct = productService.save(product);

        String httpResponse = this.mockMvc.perform(get("/api/products/" + savedProduct.getId() + "/photo"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(Base64.encodeBase64String(savedProduct.getPhoto()), httpResponse);
    }
}
