package com.toDo.demo.reports;

import com.toDo.demo.model.Task;
import com.toDo.demo.model.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
class ReportController {
    private final TaskRepository taskRepository;
    private final PersistedTaskEventRepository persistedTaskEventRepository;

    ReportController(TaskRepository taskRepository, PersistedTaskEventRepository persistedTaskEventRepository) {
        this.taskRepository = taskRepository;
        this.persistedTaskEventRepository = persistedTaskEventRepository;
    }


    @GetMapping("/count/{id}")
    ResponseEntity<TaskWithChangesCount> readTaskWithCount(@PathVariable int id){
        return taskRepository.findById(id)
                .map(task -> new TaskWithChangesCount(task, persistedTaskEventRepository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static class TaskWithChangesCount {
        public String description;
        public boolean done;
        public int changesCount;

        TaskWithChangesCount(final Task task, final List<PersistedTaskEvent> events) {
            description = task.getDescription();
            done = task.isDone();
            changesCount = events.size();
        }
    }
}
