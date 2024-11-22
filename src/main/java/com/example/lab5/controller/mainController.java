package com.example.lab5.controller;

import com.example.lab5.entity.Categories;
import com.example.lab5.entity.Users;
import com.example.lab5.service.CategoriesService;
import com.example.lab5.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class mainController {

    private final UsersService usersService;

    @Autowired
    public mainController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/main-page")
    public String mainPage(Principal principal, Model model) {
        String username = principal.getName();
        Users user = usersService.findByUsername(username);
        model.addAttribute("user", user);
        return "main-page";
    }
}
