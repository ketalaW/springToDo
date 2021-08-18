package com.toDo.demo.adapter;

import com.toDo.demo.model.Task;
import com.toDo.demo.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@RepositoryRestResource(path = "todos", collectionResourceRel = "todos" )
@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
  //  @RestResource(path = "done", rel = "done")

    @Override
    @Query(nativeQuery = true, value = "select  count(*) > 0 from tasks where id=:id")
    boolean existsById(@Param("id") Integer id);

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    @Override
    List<Task> findAllByGroup_Id(Integer groupId);
}
