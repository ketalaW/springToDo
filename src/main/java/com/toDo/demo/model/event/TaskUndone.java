package com.toDo.demo.model.event;

import com.toDo.demo.model.Task;

import java.time.Clock;

public class TaskUndone extends TaskEvent {
     TaskUndone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
