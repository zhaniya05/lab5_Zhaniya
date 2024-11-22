package com.example.lab5.service.impl;

import com.example.lab5.entity.Tasks;
import com.example.lab5.repository.TasksRepository;
import com.example.lab5.service.TasksService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TasksServiceImpl implements TasksService {

    private final TasksRepository tasksRepository;

    public TasksServiceImpl(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @Override
    public List<Tasks> getAllTasks() {
        return tasksRepository.findAll();
    }

    @Override
    public List<Tasks> getTasksByUsername(String username) {
        return tasksRepository.findByUserUsernameOrderByPriorityAscDueDateAsc(username);
    }

    @Override
    public void saveTask(Tasks task) {
        this.tasksRepository.save(task);
    }

    @Override
    public Tasks getTaskById(Long id) {
        Optional<Tasks> task = tasksRepository.findById(id);
        Tasks taskn = null;
        if(task.isPresent()) {
            taskn = task.get();
        } else {
            throw new RuntimeException("User not found for id " + id);
        }
        return taskn;
    }

    @Override
    public void deleteTaskById(Long id) {
        this.tasksRepository.deleteById(id);
    }

    @Override
    public List<Tasks> getTasksByStatus(String username, String status) {
        return tasksRepository.findByUserUsernameAndStatusOrderByPriorityAscDueDateAsc(username, status);
    }

    @Override
    public List<Tasks> getTasksByPriority(String username, String priority) {
        return tasksRepository.findByUserUsernameAndPriorityOrderByPriorityAscDueDateAsc(username, priority);
    }

    @Override
    public List<Tasks> getTasksByStatusAndPriority(String username, String status, String priority) {
        return tasksRepository.findByUserUsernameAndStatusAndPriorityOrderByPriorityAscDueDateAsc(username, status, priority);
    }
}
