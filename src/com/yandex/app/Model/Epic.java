package com.yandex.app.Model;
import java.util.ArrayList;
import java.util.Objects;
import com.yandex.app.Service.Status;

public class Epic extends Task {

    public ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String description, String name) {
        super(description, name);
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

    public ArrayList<Integer>getSubtaskIds(){
        return subtaskIds;
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


