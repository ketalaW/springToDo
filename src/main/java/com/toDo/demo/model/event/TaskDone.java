package com.toDo.demo.model.event;

import com.toDo.demo.model.Task;

import java.time.Clock;

public class TaskDone extends  TaskEvent{
     TaskDone(Task source) {
       super(source.getId(), Clock.systemDefaultZone());
    }
}
