package com.example.lab5.service;

import com.example.lab5.entity.Users;

import java.util.List;

public interface UsersService{
    List<Users> getAllUsers();



    Users getUserById(Long id);

    void deleteUserById(Long id);

    Users save(Users user);

    Users findByUsername(String username);

    boolean existsByUsername(String username);

    void updateRole(Long userId, String role);

    Users findByEmail(String email);

    Users findByResetToken(String resetToken);

    String generateResetToken(Long userId);

    Users findByUserId(Long userId);

    void resetPassword(Long userId, String newPassword);
//
//    void savePasswordResetToken(Users user, String token);
//
//    boolean isPasswordResetTokenValid(String token);
//
//    void resetPassword(String token, String newPassword);

}
