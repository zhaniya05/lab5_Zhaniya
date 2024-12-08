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

    // CONSTRUCTOR
    public CategoriesServiceImpl(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }


    // METHOD TO GET THE LIST OF ALL CATEGORIES
    @Override
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAllWithTasks();
    }


    @Override
    public Categories getCategoryById(Long id) {
        return categoriesRepository.findByCategoryId(id);
    }

    // METHOD TO SAVE NEW CATEGORIES
    @Override
    public void saveCategories(Categories categories) {
        this.categoriesRepository.save(categories);
    }


    // FIND AND GET CATEGORIES BY ID
    @Override
    public Categories getCategoriesById(Long id) {
        Optional<Categories> cat = categoriesRepository.findById(id);
        Categories catn = null;
        if(cat.isPresent()) {
            catn = cat.get();
        } else {
            throw new RuntimeException("Categories not found for id " + id);
        }
        return catn;
    }


    // DELETE CATEGORY
    @Override
    public void deleteCategoryById(Long id) {
        this.categoriesRepository.deleteById(id);
    }
}
