package com.example.lab5.controller;


import com.example.lab5.entity.Categories;
import com.example.lab5.entity.Users;
import com.example.lab5.service.CategoriesService;
import com.example.lab5.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class CategoriesController {
    private final CategoriesService categoryService;

    private final UsersService usersService;

    @Autowired
    public CategoriesController(CategoriesService categoryService, UsersService usersService) {
        this.categoryService = categoryService;
        this.usersService = usersService;
    }

    @GetMapping("/categories")
    public String getCategories(Model model, Principal principal) {
        String username = principal.getName();
        Users user = usersService.findByUsername(username);
        model.addAttribute("user", user);
        List<Categories> categories = categoryService.getAllCategories();
        model.addAttribute("listCategories", categories);
        return "index-categories";
    }

    @GetMapping("/showNewCategoryForm")
    public String showNewCategoryForm(Model model, Principal principal) {
        String username = principal.getName();
        Users user = usersService.findByUsername(username);
        model.addAttribute("user", user);
        Categories category = new Categories();
        model.addAttribute("category", category);
        return "new-category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute("category") Categories category) {
        categoryService.saveCategories(category);
        return "redirect:/categories";
    }

    @GetMapping("/showFormForUpdateCategory/{id}")
    public String showFormForUpdate(@PathVariable Long id, Model model, Principal principal) {
        String username = principal.getName();
        Users user = usersService.findByUsername(username);
        model.addAttribute("user", user);
        Categories category = categoryService.getCategoriesById(id);
        model.addAttribute("category", category);
        return "update-category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        this.categoryService.deleteCategoryById(id);
        return "redirect:/categories";
    }
}
