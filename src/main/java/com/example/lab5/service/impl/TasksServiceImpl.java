package com.example.lab5.service.impl;

import com.example.lab5.entity.Tasks;
import com.example.lab5.repository.TasksRepository;
import com.example.lab5.service.TasksService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TasksServiceImpl implements TasksService {

    private final TasksRepository tasksRepository;

    //CONSTRUCTOR
    public TasksServiceImpl(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }


    // GET ALL TASKS
    @Override
    public Page<Tasks> getAllTasks(Pageable pageable) {
        return tasksRepository.findAll(pageable);
    }


    // GET LIST OF TASKS FOR EACH USERNAME
    @Override
    public Page<Tasks> getTasksByUsername(String username, Pageable pageable) {
        return tasksRepository.findByUserUsernameOrderByPriorityAscDueDateAsc(username, pageable);
    }


    // SAVE NEW TASK
    @Override
    public void saveTask(Tasks task) {
        this.tasksRepository.save(task);
    }


    // GET A TASK BY ITS ID
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


    // DELETE TASK
    @Override
    public void deleteTaskById(Long id) {
        this.tasksRepository.deleteById(id);
    }


    // GET TASKS BY STATUS
    @Override
    public Page<Tasks> getTasksByStatus(String username, String status, Pageable pageable) {
        return tasksRepository.findByUserUsernameAndStatusOrderByPriorityAscDueDateAsc(username, status, pageable);
    }


    // GET TASKS BY PRIORITY
    @Override
    public Page<Tasks> getTasksByPriority(String username, String priority, Pageable pageable) {
        return tasksRepository.findByUserUsernameAndPriorityOrderByPriorityAscDueDateAsc(username, priority, pageable);
    }


    // GET TASKS BY PRIORITY AND STATUS
    @Override
    public Page<Tasks> getTasksByStatusAndPriority(String username, String status, String priority, Pageable pageable) {
        return tasksRepository.findByUserUsernameAndStatusAndPriorityOrderByPriorityAscDueDateAsc(username, status, priority, pageable);
    }

    @Override
    public Page<Tasks> getTasksByStatusAndPriorityForAdmin(String status, String priority, Pageable pageable) {
        return tasksRepository.findByStatusAndPriority(status, priority, pageable);
    }

    @Override
    public Page<Tasks> getTasksByStatusForAdmin(String status, Pageable pageable) {
        return tasksRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Tasks> getTasksByPriorityForAdmin(String priority, Pageable pageable) {
        return tasksRepository.findByPriority(priority, pageable);
    }

    @Override
    public Page<Tasks> searchTasks(String keyword, Pageable pageable) {
        return tasksRepository.searchByKeyword(keyword, pageable);
    }

}
