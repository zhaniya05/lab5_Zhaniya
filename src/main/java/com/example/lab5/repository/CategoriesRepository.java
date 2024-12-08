package com.example.lab5.repository;

import com.example.lab5.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    // FIND CATEGORIES WITH TASKS
    @Query("SELECT c FROM Categories c LEFT JOIN FETCH c.tasks")
    List<Categories> findAllWithTasks();

    Categories findByCategoryId(Long categoryId);
}
