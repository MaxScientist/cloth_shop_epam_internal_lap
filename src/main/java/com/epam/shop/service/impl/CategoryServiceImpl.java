package com.epam.shop.service.impl;


import com.epam.shop.entity.Category;
import com.epam.shop.repository.CategoryRepository;
import com.epam.shop.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends GenericServiceImpl<Category, Integer, CategoryRepository> {
}
