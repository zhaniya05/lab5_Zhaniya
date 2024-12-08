package com.example.lab5.controller;

import com.example.lab5.entity.Users;
import com.example.lab5.service.EmailService;
import com.example.lab5.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class UsersController {
    private final UsersService usersService;

    private final PasswordEncoder passwordEncoder;

    private final HttpServletRequest request;

    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    //constructor
    public UsersController(UsersService usersService,
                           HttpServletRequest request,
                           EmailService emailService) {
        super();
        this.usersService = usersService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.request = request;
        this.emailService = emailService;

    }



    //REGISTRATION
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

        user.setRole("ROLE_USER");

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


    //LOGIN
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

        if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Invalid password");
            return "login";
        }

        model.addAttribute("username", user.getUsername());
        return "main-page";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgot-password";
    }


    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        Users user = usersService.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "User with this email does not exist.");
            return "forgot-password";
        }


        String token = usersService.generateResetToken(user.getUserId());
        String resetLink = "http://localhost:8080/resetPassword?token=" + token;


        logger.info("Generated reset token: {} for user: {}", token, user.getUsername());
        emailService.sendEmail(user.getEmail(), "Password Reset Request",
                "Click the link to reset your password: " + resetLink);

        model.addAttribute("message", "Password reset link has been sent to your email.");
        return "login";
    }


    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Users user = usersService.findByResetToken(token);
        if (user == null) {
            model.addAttribute("error", "Invalid or expired token.");
            return "reset-password";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        Users user = usersService.findByResetToken(token);
        if (user == null) {
            model.addAttribute("error", "Invalid or expired token.");
            return "reset-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("token", token);
            return "reset-password";
        }

        usersService.resetPassword(user.getUserId(), newPassword);
        model.addAttribute("message", "Password reset successfully. Please log in.");
        return "login";
    }






    //LIST OF USERS
    @GetMapping("/users")
    public String getUsers(Model model, Principal principal) {
        String username = principal.getName();
        Users user = usersService.findByUsername(username);
        model.addAttribute("user", user);

        List<Users> users = usersService.getAllUsers();
        model.addAttribute("listUsers", users);
        return "index-users";
    }



    //UPDATE
    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable Long id, Model model) {
        Users user = usersService.getUserById(id);
        user.setPassword("");
        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") Users user,
                           @RequestParam(value = "profilePhotoFile", required = false) MultipartFile profilePhotoFile) {

        Users existingUser = usersService.getUserById(user.getUserId());


        user.setCreatedAt(existingUser.getCreatedAt());
        user.setRole(existingUser.getRole());
        user.setTasks(existingUser.getTasks());


        if (user.getPassword() != null && !user.getPassword().isEmpty() && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }

        if (profilePhotoFile != null && !profilePhotoFile.isEmpty()) {
            uploadPhoto(user, profilePhotoFile);
        } else {
            user.setProfilePhoto(existingUser.getProfilePhoto());
        }



        usersService.save(user);
        if (user.getRole().equals("ROLE_USER")) {
            return "main-page-for-users";
        } else {
            return "main-page";
        }
    }



    @PostMapping("/upload")
    public String uploadPhoto(Users user, @RequestParam("file") MultipartFile file) {
        try {

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();


            String uploadDir = Paths.get("lab5", "uploads").toAbsolutePath().toString();


            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdir();
            }

            Path filePath = Paths.get(uploadDir, fileName);

            file.transferTo(filePath.toFile());

            user.setProfilePhoto("/uploads/" + fileName);

            usersService.save(user);

            return "redirect:/users";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }





    //DELETE
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        usersService.deleteUserById(id);
        return "redirect:/users";
    }


//    @GetMapping("/updateRole/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public String updateRole(@PathVariable("id") Long userId, @RequestParam("role") String role) {
//        usersService.updateRole(userId, role);
//        return "redirect:/users";
//    }


    //UPDATE
    @GetMapping("/showFormForUpdateUser/{id}")
    public String showFormForUpdateUser(@PathVariable Long id, Model model) {
        Users user = usersService.getUserById(id);
        user.setPassword("");
        model.addAttribute("user", user);
        return "update-user-for-users";
    }


    @GetMapping("/viewProfile/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        Users user = usersService.getUserById(id);
        model.addAttribute("user", user);
        return "view-profile";
    }
}
