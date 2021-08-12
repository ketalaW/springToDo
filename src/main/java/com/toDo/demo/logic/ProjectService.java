package com.toDo.demo.logic;

import com.toDo.demo.TaskConfigurationProperties;
import com.toDo.demo.model.*;
import com.toDo.demo.model.projection.GroupReadModel;
import com.toDo.demo.model.projection.GroupWriteModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class ProjectService {
    private ProjectRepository projectRepository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    public ProjectService(ProjectRepository projectRepository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties config) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    public List<Project> readAll() {
       return projectRepository.findAll();
    }

    public Project save(final Project toSave){
        return projectRepository.save(toSave);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline){
        if(!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)){
            throw new IllegalArgumentException("Tylko jedna grupa zadań jest dozwolana w projektcie.");
        }
        TaskGroup result = projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> new Task(
                                            projectStep.getDescription(),
                                            deadline.plusDays(projectStep.getDaysToDeadline())))
                                    .collect(Collectors.toSet())
                    );
                    targetGroup.setProject(project);
                    return taskGroupRepository.save(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono projektu o podannym id") );

       return new GroupReadModel(result);
    }



}
