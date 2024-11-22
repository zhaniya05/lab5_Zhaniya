package com.example.lab5.repository;

import com.example.lab5.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.tasks")
    List<Users> findAllUsersWithTasks();

    Users findByUsername(String Username);

}
