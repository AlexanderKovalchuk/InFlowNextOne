package com.kaval.android.inflow.model;

import com.kaval.android.inflow.Enums.TaskState;
import com.orm.SugarRecord;
import com.yahoo.squidb.annotations.ColumnSpec;
import com.yahoo.squidb.annotations.PrimaryKey;
import com.yahoo.squidb.annotations.TableModelSpec;

import java.io.Serializable;
import java.util.Date;

public class Task extends SugarRecord<Task> implements Serializable {

    long creationMs;

    String name;

    String description;

    long duration;

    Date dueDate;

    TaskState state;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getDuration() {
        return duration;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public TaskState getState() {
        return state;
    }

    public Task() {
    }

    public Task(String name, String description, long duration, Date dueDate, TaskState state) {
        this.name = name;
        this.description = description;
        this.duration = duration;

        if (dueDate == null) {
            dueDate = new Date(0);

        }
        this.dueDate = dueDate;
        this.state = state;
        this.creationMs = System.currentTimeMillis();
    }
}

