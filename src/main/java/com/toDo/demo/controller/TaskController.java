package com.toDo.demo.controller;


import com.toDo.demo.model.Task;
import com.toDo.demo.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@ RestController
public class TaskController {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Poprano wszystkie zadania");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping(value = "/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.warn("Read pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping(value = "/tasks/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        logger.warn("Poprano pojedynczo zadanie");

        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
//    ResponseEntity<Optional<Task>> readTask(@PathVariable int id) {
//        logger.warn("Poprano pojedynczo zadanie");
//        if(!repository.existsById(id)){
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(repository.findById(id));
    }

    @PostMapping(value = "/tasks")
    ResponseEntity<Task> createTask(@Valid @RequestBody Task toCreate) {
        logger.warn("Stworzenie zadania");
        Task result =  repository.save(toCreate);
        return ResponseEntity.created(URI.create("/tasks/" + result.getId())).body(result);
    }

    //@Transactional
    @PutMapping(value = "/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @Valid @RequestBody Task toUpdate) {
        if(!repository.existsById(id)){
            return  ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping(value = "/tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        logger.warn("Zmianna statusu zadania");
        if(!repository.existsById(id)){
            return  ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));

        return ResponseEntity.noContent().build();
    }
}
