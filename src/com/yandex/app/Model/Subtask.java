package com.yandex.app.Model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private final int idEpic;




    public Subtask (int id, String description, String name, Status status, int idEpic){
        super(id, description, name, status);
        this.idEpic = idEpic;
    }

    public Subtask(String description, String name, Status status, int idEpic) {
        super(description, name, status);
        this.idEpic = idEpic;
    }

    public Subtask(String name, Status status, String description, LocalDateTime startTime, int idEpic) {
        super(name, status, description, startTime);
        this.idEpic = idEpic;
    }

    @Override
    public TaskTupe getType() {
        return TaskTupe.SUBTASK;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idEpic == subtask.idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }

    @Override
    public String toString() {
        return "Tasks.Subtask{" +
                "id=" + '\''+id + '\'' + "idEpic=" + '\''+idEpic + '\'' +
                ", description=" +'\''+ description + '\'' +
                ", name=" + name + ", status=" + status +'\''+ '}';
    }


}
