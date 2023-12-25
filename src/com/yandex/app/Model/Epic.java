package com.yandex.app.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.yandex.app.Service.Status;
import com.yandex.app.Service.TaskTupe;

public class Epic extends Task {

    public ArrayList<Integer> subtaskIds = new ArrayList<>();

    TaskTupe taskTupe = TaskTupe.EPIC;

    @Override
    public TaskTupe getType() {
        return taskTupe;
    }

    public Epic(String description, String name) {
        super(description, name);
    }

    public Epic(int id, String description, String name, Status status) {
        super(id, description, name, status);
    }

    public Epic(int id) {
        super(id);
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

    public Epic(int id, String description, String name, Status status, ArrayList<Integer> subtaskIds) {
        super(id, description, name, status);
        this.subtaskIds = subtaskIds;
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
                ", name=" + name + ", status=" + status +'\''+ '}';
    }
}


