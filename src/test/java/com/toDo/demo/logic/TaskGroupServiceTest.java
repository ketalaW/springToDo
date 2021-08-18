package com.toDo.demo.logic;

import com.toDo.demo.model.TaskGroup;
import com.toDo.demo.model.TaskGroupRepository;
import com.toDo.demo.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {


    @Test
    @DisplayName("Powinien wyrzucić IllegalStateException, gdy grupa posiada niezakończone zadanie")
    void toggleGroup_notAllDoneTask_throwsIllegalArgumentException() {
        var mockTaskRepositorium = mock(TaskRepository.class);
        when(mockTaskRepositorium.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);

        var mockTaskGroupRepository = mock(TaskGroupRepository.class);

        var toTest = new TaskGroupService(null, mockTaskRepositorium);

        var exception =  catchThrowable(()->toTest.toggleGroup(1));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("niezakończone zadanie");
    }


    @Test
    @DisplayName("Powinien wyrzucić IllegalArgumentException, gdy grupa o pdanym id nie została znaleziona")
    void toggleGroup_AllDoneTask_and_groupNotFound_throwsIllegalArgumentException() {
        var mockTaskRepositorium = mock(TaskRepository.class);
        when(mockTaskRepositorium.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);

        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());

        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepositorium);

        var exception =  catchThrowable(()->toTest.toggleGroup(1));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("takim id nie");
    }

    @Test
    @DisplayName("Powinien zmienić status grupy")
    void toggleGroup_correct_throwsIllegalArgumentException() {
        var mockTaskRepositorium = mock(TaskRepository.class);
        when(mockTaskRepositorium.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);

        TaskGroup group = new TaskGroup();
        var beforeToggle= group.isDone();

        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));

        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepositorium);

        toTest.toggleGroup(1);

        assertThat(group.isDone()).isEqualTo(!beforeToggle);

    }

//    @Test
//    void toggleGroup() {
//    }
}