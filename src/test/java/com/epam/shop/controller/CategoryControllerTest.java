package com.epam.shop.controller;


import com.epam.shop.dto.CategoryDTO;
import com.epam.shop.mapper.DTOMapper;
import com.epam.shop.dto.ProductDTO;
import com.epam.shop.entity.Category;
import com.epam.shop.entity.Product;
import com.epam.shop.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
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

        List<CategoryDTO> categoryDTOS = objectMapper.readValue(httpResponse, new TypeReference<>(){});
        List<Category> categories =  categoryDTOS.stream().
                map(categoryMapper::fromDTO).collect(toList());

        assertEquals(fromDB.toString(), categories.toString());
    }

    private void createCategories() {
        IntStream.range(0, 5).forEachOrdered(i -> {
            Category category = new Category();
            category.setName(i + " category");
            categoryService.save(category);
        });
    }


}
