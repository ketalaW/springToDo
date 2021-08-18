package com.toDo.demo.logic;

import com.toDo.demo.model.Task;
import com.toDo.demo.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequestScope
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository repository;

    TaskService(final TaskRepository repository){
        this.repository = repository;
    }

    @Async
    public CompletableFuture<List<Task>> findAllAsync() {
        logger.info("Rozpoczęcie funkcji asynchronicznie");
        return CompletableFuture.supplyAsync(repository::findAll);
    }
}
