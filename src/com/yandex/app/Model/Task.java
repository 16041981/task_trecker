package com.yandex.app.Model;
import com.yandex.app.Service.Status;
import com.yandex.app.Service.TaskTupe;

import java.util.Objects;

public  class Task {

    protected Status status;
    protected int id;
    protected String description;
    protected String name;
    TaskTupe taskTupe = TaskTupe.TASK;
    private int epicId = id;

    public TaskTupe getType() {
        return taskTupe;
    }

    public int getIdEpic() {
        return epicId;
    }

    public Task(int id, String description, String name, Status status) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.status = status;
    }

    public Task(String description, String name, Status status) {
        this.description = description;
        this.name = name;
        this.status = status;
    }

    public Task(String description, String name) {
        this.description = description;
        this.name = name;
    }

    public Task(int id, String description, String name) {
        this.id = id;
        this.description = description;
        this.name = name;
    }

    public Task(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override  public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                Objects.equals(description, task.description) &&
                Objects.equals(name, task.name) &&
                Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name, status);
    }

    @Override
    public String toString() {
        return "Tasks.Task{" +
                "id=" + '\''+id + '\'' +
                ", description=" +'\''+ description + '\'' +
                ", name=" + name + ", status=" + status +'\''+ '}';
    }

}
