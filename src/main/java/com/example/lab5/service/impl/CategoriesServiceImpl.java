package com.example.lab5.service.impl;

import com.example.lab5.entity.Categories;
import com.example.lab5.repository.CategoriesRepository;
import com.example.lab5.service.CategoriesService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesServiceImpl(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }
    @Override
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAllWithTasks();
    }

    @Override
    public void saveCategories(Categories categories) {
        this.categoriesRepository.save(categories);
    }

    @Override
    public Categories getCategoriesById(Long id) {
        Optional<Categories> cat = categoriesRepository.findById(id);
        Categories catn = null;
        if(cat.isPresent()) {
            catn = cat.get();
        } else {
            throw new RuntimeException("User not found for id " + id);
        }
        return catn;
    }

    @Override
    public void deleteCategoryById(Long id) {
        this.categoriesRepository.deleteById(id);
    }
}
