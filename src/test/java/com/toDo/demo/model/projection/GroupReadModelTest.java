package com.toDo.demo.model.projection;

import com.toDo.demo.model.Task;
import com.toDo.demo.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {
    @Test
    @DisplayName("Powinien stworzyć pustą date końcową dla group nie posiadających terminu końcowego")
    void constructor_noDeadlines_createsNullDeadline(){
        var source = new TaskGroup();
        source.setDescription("foe");
        source.setTasks(Set.of(new Task("bar", null)));

        var result = new GroupReadModel(source);

        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }
}