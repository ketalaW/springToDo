package com.toDo.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

//@RepositoryRestResource(path = "todos", collectionResourceRel = "todos" )
@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
  //  @RestResource(path = "done", rel = "done")
}