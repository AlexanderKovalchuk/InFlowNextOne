package com.kaval.android.inflow.utils;

import com.kaval.android.inflow.Enums.TaskState;
import com.kaval.android.inflow.model.Task;

import java.io.Serializable;
import java.util.Date;

public class TaskBuilder {

    private String name;
    private String description;
    private long duration;
    private Date dueDate;
    private TaskState state;


    public TaskBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TaskBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public TaskBuilder setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public TaskBuilder setState(TaskState state) {
        this.state = state;
        return this;
    }

    public Task createTask() {
        return new Task(name, description, duration, dueDate, state);
    }
}
