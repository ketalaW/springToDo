package com.toDo.demo.controller;


import com.toDo.demo.model.TaskRepository;
import org.hibernate.mapping.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RepositoryRestController
public class TaskController {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/tasks")
    ResponseEntity<?> readAllTasks() {
        logger.warn("Pobrano wszystkie taski");
        return ResponseEntity.ok(repository.findAll());
    }
}
