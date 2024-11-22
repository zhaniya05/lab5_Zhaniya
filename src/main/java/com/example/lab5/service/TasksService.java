package com.example.lab5.service;

import com.example.lab5.entity.Tasks;
import com.example.lab5.entity.Users;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TasksService {
    List<Tasks> getAllTasks();

    List<Tasks> getTasksByUsername(String username);

    void saveTask(Tasks task);

    Tasks getTaskById(Long id);

    void deleteTaskById(Long id);

    List<Tasks> getTasksByStatus(String username, String status);

    List<Tasks> getTasksByPriority(String username, String priority);

    List<Tasks> getTasksByStatusAndPriority(String username, String status, String priority);
}
