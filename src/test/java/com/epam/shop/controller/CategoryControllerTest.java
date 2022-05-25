package com.epam.shop.controller;


import com.epam.shop.dto.CategoryDTO;
import com.epam.shop.dto.ProductDTO;
import com.epam.shop.entity.Category;
import com.epam.shop.entity.Product;
import com.epam.shop.mapper.DTOMapper;
import com.epam.shop.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.epam.shop.controller.TestUtils.*;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryServiceImpl categoryService;


    @Autowired
    private DTOMapper<Category, CategoryDTO> categoryMapper;

    @Autowired
    private DTOMapper<Product, ProductDTO> productMapper;


    @Test
    void findAllCategories() throws Exception {
        createCategories();

        List<Category> fromDB = categoryService.findAll();

        String httpResponse = mockMvc.perform(get("/api/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<CategoryDTO> categoryDTOS = objectMapper.readValue(httpResponse, new TypeReference<>() {
        });

        List<Category> categories = categoryDTOS.stream().
                map(categoryMapper::fromDTO).collect(toList());

        assertEquals(fromDB.toString(), categories.toString());
    }

    @Test
    void createAndSaveCategory() throws Exception {
        Category categoryToSaveToDB = createCategory();
        String requestBody = objectMapper.writeValueAsString(categoryMapper.toDTO(categoryToSaveToDB));

        String httpResponse = mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CategoryDTO categoryDTO = objectMapper.readValue(httpResponse, new TypeReference<>() {
        });
        Category category = categoryMapper.fromDTO(categoryDTO);
        Category fromDB = categoryService.findById(category.getId());

        assertEquals(fromDB.toString(), category.toString());
    }

    @Test
    void findCategoryViaId() throws Exception {
        Category categoryToSaveToDB = createCategory();

        Category categorySavedVersion = categoryService.save(categoryToSaveToDB);

        String httpResponse = mockMvc.perform(get("/api/categories/" + categorySavedVersion.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CategoryDTO categoryDTO = objectMapper.readValue(httpResponse, new TypeReference<>() {
        });
        Category categoryFromDTO = categoryMapper.fromDTO(categoryDTO);

        assertEquals(categorySavedVersion.toString(), categoryFromDTO.toString());
    }

    @Test
    void findProductsByCategoryId() throws Exception {
        List<Product> savedProducts = createProducts();

        String httpResponse = mockMvc.perform(get("/api/categories/"+savedProducts.get(0).getCategoryId()+"/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ProductDTO> productDTOS = objectMapper.readValue(httpResponse, new TypeReference<>() {});
        List<Product> productList = productDTOS.stream().map(productMapper::fromDTO).collect(toList());

        assertFalse(productList.isEmpty());
        assertEquals(savedProducts.toString(), productList.toString());
    }

}
