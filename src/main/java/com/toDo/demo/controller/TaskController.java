package com.toDo.demo.controller;


import com.toDo.demo.logic.TaskService;
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
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final TaskService service;

    TaskController(final TaskRepository repository, TaskService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping(params = {"!sort", "!page", "size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Poprano wszystkie zadania");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.warn("Read pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }


    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state){
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        logger.warn("Poprano pojedynczo zadanie");

        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<Task> createTask(@Valid @RequestBody Task toCreate) {
        logger.warn("Stworzenie zadania");
        Task result =  repository.save(toCreate);
        return ResponseEntity.created(URI.create("/tasks/" + result.getId())).body(result);
    }

    //@Transactional
    @PutMapping(value = "/{id}")
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
    @PatchMapping(value = "/{id}")
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
