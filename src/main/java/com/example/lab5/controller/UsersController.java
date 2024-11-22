package com.example.lab5.controller;

import com.example.lab5.entity.Users;
import com.example.lab5.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class UsersController {
    private final UsersService usersService;

    private final PasswordEncoder passwordEncoder;

    public UsersController(UsersService usersService) {
        super();
        this.usersService = usersService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    public String registerUserAccount(@Valid @ModelAttribute("user") Users user,
                                      BindingResult result,
                                      Model model) {

        if (result.hasErrors()) {
            return "new-user";
        }

        if (usersService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "User is already taken");
            return "new-user";
        }

        usersService.save(user);
        model.addAttribute("success", "User successfully signed up! Please log in.");
        return "login";
    }


    @GetMapping("/register")
    public String showNewUserForm(Model model) {
        Users user = new Users();
        model.addAttribute("user", user);
        return "new-user";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Model model) {
        Users user = usersService.findByUsername(username);

        if (user == null) {
            model.addAttribute("error", "User does not exist");
            return "login";
        }

        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid password");
            return "login";
        }

        model.addAttribute("username", user.getUsername());
        return "main-page";
    }


    @GetMapping("/users")
    public String getUsers(Model model, Principal principal) {
        String username = principal.getName();
        Users user = usersService.findByUsername(username);
        model.addAttribute("user", user);

        List<Users> users = usersService.getAllUsers();
        model.addAttribute("listUsers", users);
        return "index-users";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable Long id, Model model) {
        Users user = usersService.getUserById(id);
        user.setPassword("");
        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") Users user) {

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        } else {
            Users existingUser = usersService.getUserById(user.getUserId());
            user.setPassword(existingUser.getPassword());
        }

        usersService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        usersService.deleteUserById(id);
        return "redirect:/users";
    }
}
