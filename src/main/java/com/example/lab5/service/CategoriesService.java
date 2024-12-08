package com.example.lab5.service;

import com.example.lab5.entity.Categories;
import com.example.lab5.entity.Users;

import java.util.List;
import java.util.Optional;

public interface CategoriesService {
    List<Categories> getAllCategories();

    void saveCategories(Categories categories);

    Categories getCategoriesById(Long id);

    void deleteCategoryById(Long id);

    Categories getCategoryById(Long id);
}
