package com.toDo.demo.controller;

import com.toDo.demo.logic.TaskGroupService;
import com.toDo.demo.model.Task;
import com.toDo.demo.model.TaskRepository;
import com.toDo.demo.model.projection.GroupReadModel;
import com.toDo.demo.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
class TaskGroupController {
    private final TaskGroupService service;
    private final TaskRepository repository;
    public static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);

    TaskGroupController(TaskGroupService service, TaskRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroup() {
        return ResponseEntity.ok(service.readAll());
    }

    @Transactional
    @PatchMapping("/{id}")
    ResponseEntity<?> toogleGroup(@PathVariable int id) {
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@Valid @RequestBody GroupWriteModel toCreate){
        GroupReadModel group = service.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + group.getId())).body(group);
    }

    @GetMapping("/{id}/tasks")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id){
         return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
