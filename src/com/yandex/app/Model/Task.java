package com.yandex.app.Model;

import java.time.LocalDateTime;
import java.util.Objects;

public  class Task {

    protected int id;
    protected String name;

    protected Status status;
    protected String description;
    protected long duration;
    protected LocalDateTime startTime ;

    public LocalDateTime getEndTime(){
        if (startTime == null){
            return null;
        }
        LocalDateTime localDateTime = startTime.plusMinutes(duration);
        return localDateTime;
    }

    public TaskTupe getType() {
        return TaskTupe.TASK;
    }

    public Task(int id, String name, Status status, String description, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        this.startTime = startTime;
    }

    public Task(int id, String name, String description, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
    }



    public Task(String name, Status status, String description, LocalDateTime startTime) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.startTime = startTime;
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
        this.status = Status.NEW;
    }

    public Task(String name, String description, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
    }

    public Task(int id, String description, String name) {
        this.id = id;
        this.description = description;
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && Objects.equals(name, task.name) && status == task.status && Objects.equals(description, task.description) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, description, duration, startTime);
    }

    @Override
    public String toString() {
        return "Tasks.Task{" +
                "id=" + '\''+id + '\'' +
                ", description=" +'\''+ description + '\'' +
                ", name=" + name + ", status=" + status +'\''+ '}';
    }

}
