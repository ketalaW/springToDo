package com.toDo.demo.logic;

import com.toDo.demo.TaskConfigurationProperties;
import com.toDo.demo.model.*;
import com.toDo.demo.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.toDo.demo.controller.TaskController.logger;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("Powinien wyrzucić llegalArgumentException kiedy próbujemy stworzyć więcej niż jedna grupe, gdy w konfiguracji ustawione jest pozwolenie tylko na 1 grupę.")
    void createGroup_noMultipleGroupsConfig_And_openGroups_throwsIllegalArgumentException() {
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);

        TaskConfigurationProperties mockConfig = configurationReturning(false);

        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);

        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("jedna grupa zadań");

    }


    @Test
    @DisplayName("Powinien wyrzucić llegalArgumentException kiedy kofiguracja jest okej, ale nie znaleziono projektu o podanym id")
    void createGroup_configurationOk_throwsIllegalArgumentException() {

        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository, null, mockConfig);

        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("projektu o podannym id");

    }


    @Test
    @DisplayName("Powinien wyrzucić llegalArgumentException kiedy kofiguracja jest okej, ale nie ma grupy oraz projektu")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {

        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);

        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository, mockGroupRepository, mockConfig);

        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("projektu o podannym id");

    }

    @Test
    @DisplayName("powinien utworzyć nową grupe bazując na projekcie")
    void createGroup_configurationOk_existinProject_createsAndSavesGroup() {

        var today = LocalDate.now().atStartOfDay();
        var project =  projectWith("xiaomi", Set.of(-1, -2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));

        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        int countBeforeCall = inMemoryGroupRepo.count();

        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, mockConfig);

        GroupReadModel result = toTest.createGroup(1, today);

        assertThat(result)
                .hasFieldOrPropertyWithValue("description", "xiaomi");

        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));

        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("Taki przykładowy step"));

        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());

    }


    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository() {
        };
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }


        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }

    private Project projectWith(String description, Set<Integer> daysToDeadline) {

        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("Taki przykładowy step");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                })
                .collect(Collectors.toSet());

        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(description);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);

        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }
}