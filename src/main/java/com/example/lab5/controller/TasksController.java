package com.example.lab5.controller;

import com.example.lab5.entity.Categories;
import com.example.lab5.entity.Tasks;
import com.example.lab5.entity.Users;
import com.example.lab5.service.CategoriesService;
import com.example.lab5.service.TasksService;
import com.example.lab5.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class TasksController {
    private final TasksService tasksService;

    private final CategoriesService categoryService;

    private final UsersService usersService;

    @Autowired
    public TasksController(TasksService tasksService, CategoriesService categoryService, UsersService usersService) {
        this.tasksService = tasksService;
        this.categoryService = categoryService;
        this.usersService = usersService;
    }

    @GetMapping("/tasks")
    public String getTasks(@RequestParam(required = false) String status,
                           @RequestParam(required = false) String priority,
                           Model model, Principal principal) {
        String username = principal.getName();
        Users user = usersService.findByUsername(username);
        model.addAttribute("user", user);

        List<Tasks> tasks;

        if (status != null && !status.isEmpty() && priority != null && !priority.isEmpty()) {
            tasks = tasksService.getTasksByStatusAndPriority(username, status, priority);
        } else if (status != null && !status.isEmpty()) {
            tasks = tasksService.getTasksByStatus(username, status);
        } else if (priority != null && !priority.isEmpty()) {
            tasks = tasksService.getTasksByPriority(username, priority);
        } else {
            tasks = tasksService.getTasksByUsername(username);
        }

        if (status != null) {
            status = status.trim().toLowerCase();
        }
        if (priority != null) {
            priority = priority.trim().toLowerCase();
        }

        List<Categories> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        model.addAttribute("listTasks", tasks);
        model.addAttribute("status", status);
        model.addAttribute("priority", priority);
        return "index-tasks";
    }



    @GetMapping("/showNewTaskForm")
    public String showNewTaskForm(Model model, Principal principal) {
        String username = principal.getName();
        Users user = usersService.findByUsername(username);
        model.addAttribute("user", user);

        Tasks task = new Tasks();
        model.addAttribute("task", task);
        List<Users> users = usersService.getAllUsers();
        model.addAttribute("users", users);
        List<Categories> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "new-task";
    }

    @PostMapping("/saveTask")
    public String saveTask(@Valid @ModelAttribute("task") Tasks task, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "new-task";
        }

        String username = principal.getName();
        Users user = usersService.findByUsername(username);
        task.setUser(user);
        tasksService.saveTask(task);
        return "redirect:/tasks";
    }


    @GetMapping("/showFormForUpdateTask/{id}")
    public String showFormForUpdate(@PathVariable Long id, Model model, Principal principal) {
        String username = principal.getName();
        Users user = usersService.findByUsername(username);
        model.addAttribute("user", user);

        Tasks task = tasksService.getTaskById(id);
        model.addAttribute("task", task);
        return "update-task";
    }

    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        this.tasksService.deleteTaskById(id);
        return "redirect:/tasks";
    }

    @GetMapping("/viewTask/{id}")
    public String viewTask(@PathVariable Long id, Model model, Principal principal) {
        String username = principal.getName();
        Users user = usersService.findByUsername(username);
        model.addAttribute("user", user);

        Tasks task = tasksService.getTaskById(id);
        if (task == null) {
            return "redirect:/tasks";
        }
        List<Categories> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        List<Users> users = usersService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("task", task);
        return "view-task";
    }

}
