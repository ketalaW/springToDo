package com.toDo.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//@RepositoryRestResource(path = "todos", collectionResourceRel = "todos" )
@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
  //  @RestResource(path = "done", rel = "done")

    @Override
    @Query(nativeQuery = true, value = "select  count(*) > 0 from tasks where id=:id")
    boolean existsById(@Param("id") Integer id);
}
