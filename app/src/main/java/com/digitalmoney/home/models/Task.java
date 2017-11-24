package com.digitalmoney.home.models;

/**
 * Created by shailesh on 9/11/17.
 */

public class Task {


    private String taskName;
    private String taskId;

    public Task() {
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskId='" + taskId + '\'' +
                '}';
    }

    public Task(String taskName, String taskId) {
        this.taskName = taskName;
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }



}
