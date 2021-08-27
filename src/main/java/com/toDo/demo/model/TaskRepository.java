package com.toDo.demo.model;

import com.toDo.demo.model.projection.GroupReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Optional<Task> findById(Integer id);

    boolean existsById(Integer id);

    List<Task> findAllByGroup_Id(Integer groupId);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    Task save(Task entity);

    Page<Task> findAll(Pageable page);

    List<Task> findByDone(boolean done);

    List<Task> findAllDoneTaskToday(LocalDateTime date);
}
