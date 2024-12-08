package com.example.lab5.repository;

import com.example.lab5.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    // FIND USERS WITH TASKS
    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.tasks")
    List<Users> findAllUsersWithTasks();

    // FIND BY USERNAME
    Users findByUsername(String Username);

    Users findByUserId(Long Id);

    Users findByEmail(String Email);

    @Query("SELECT u FROM Users u WHERE u.resetToken = :resetToken")
    Users findByResetToken(@Param("resetToken") String resetToken);
}
