package com.example.lab5.service;

import com.example.lab5.entity.Users;

import java.util.List;

public interface UsersService{
    List<Users> getAllUsers();

   // void saveUsers(Users user);

    Users getUserById(Long id);

    void deleteUserById(Long id);

    Users save(Users user);

    Users findByUsername(String username);

    boolean existsByUsername(String username);

}
