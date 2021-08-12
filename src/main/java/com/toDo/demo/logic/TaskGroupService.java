package com.toDo.demo.logic;

import com.toDo.demo.TaskConfigurationProperties;
import com.toDo.demo.model.TaskGroup;
import com.toDo.demo.model.TaskGroupRepository;
import com.toDo.demo.model.TaskRepository;
import com.toDo.demo.model.projection.GroupReadModel;
import com.toDo.demo.model.projection.GroupWriteModel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;


    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source){
       TaskGroup reasult = repository.save(source.toGroup());
       return new GroupReadModel(reasult);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId){
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)){
            throw new IllegalArgumentException("Grupa posiada niezakończone zadanie");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupa o takim id nie została znaleziona"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}
