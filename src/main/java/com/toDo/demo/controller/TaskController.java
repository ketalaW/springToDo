package com.toDo.demo.controller;


import com.toDo.demo.model.Task;
import com.toDo.demo.model.SqlTaskRepository;
import com.toDo.demo.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Soprano wszystkie zadania");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping(value = "/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.warn("Read pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }
}
