package com.epam.shop.service.impl;


import com.epam.shop.entity.Category;
import com.epam.shop.repository.CategoryRepository;
import com.epam.shop.service.GenericService;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends GenericService<Category, Integer, CategoryRepository> {
}
