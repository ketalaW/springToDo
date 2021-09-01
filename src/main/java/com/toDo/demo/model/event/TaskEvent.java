package com.toDo.demo.model.event;

import com.toDo.demo.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {
    public static TaskEvent changed(Task source){
        return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }

    private int taskId;
    private Instant occurence;

    TaskEvent(final int taskId, Clock clock) {
        this.taskId = taskId;
        this.occurence = Instant.now();
    }

    public int getTaskId() {
        return taskId;
    }

    public Instant getOccurence() {
        return occurence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "taskId=" + taskId +
                ", occurence=" + occurence +
                '}';
    }
}
