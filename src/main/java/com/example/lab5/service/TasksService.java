package com.example.lab5.service;

import com.example.lab5.entity.Tasks;
import com.example.lab5.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TasksService {

    Page<Tasks> getAllTasks(Pageable pageable);

    Page<Tasks> getTasksByUsername(String username, Pageable pageable);

    void saveTask(Tasks task);

    Tasks getTaskById(Long id);

    void deleteTaskById(Long id);

    Page<Tasks> getTasksByStatus(String username, String status, Pageable pageable);

    Page<Tasks> getTasksByPriority(String username, String priority, Pageable pageable);

    Page<Tasks> getTasksByStatusAndPriority(String username, String status, String priority, Pageable pageable);

    Page<Tasks> getTasksByStatusAndPriorityForAdmin(String status, String priority, Pageable pageable);

    Page<Tasks> getTasksByPriorityForAdmin(String priority, Pageable pageable);

    Page<Tasks> getTasksByStatusForAdmin(String status, Pageable pageable);

    Page<Tasks> searchTasks(String keyword, Pageable pageable);
}
