package com.yandex.app.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<Integer> subtaskIds = new ArrayList<>();

    private LocalDateTime endTime;


    public Epic(String description, String name) {
        super(description, name);
    }

    public Epic(int id, String name, String description, Status status, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, name, status,description, startTime);
        this.endTime = endTime;
    }

    public Epic(int id, String name, String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, name,description, startTime);
        this.endTime = endTime;
    }

    public Epic(String name, String description, LocalDateTime startTime, LocalDateTime endTime) {
        super( name, description, startTime);
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void addSubtaskIds (int id){
        subtaskIds.add(id);
    }

    public void removeSubtaskIds (int id){
        subtaskIds.remove(id);
    }

    public List<Integer> getSubtaskIds(){
        return subtaskIds;
    }

    @Override
    public TaskTupe getType() {
        return TaskTupe.EPIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtaskIds);
    }

    @Override
    public String toString() {
        return "Tasks.Epic{" +
                "id=" + '\''+ id + '\'' + "subtaskIds=" + '\''+ subtaskIds + '\'' +
                ", description=" +'\''+ description + '\'' +
                ", name=" + name + ", status=" + status +'\''+ ", startTime=" + startTime +'\''+ ", endTime=" + endTime +'\''+ '}';
    }
}

