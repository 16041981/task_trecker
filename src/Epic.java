import java.util.ArrayList;

public class Epic extends Task {
    Subtask subtask;
    public ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String description, String name) {
        super(description, name);
    }

    public Epic(int id, String description, String name, String status, ArrayList<Integer> subtaskIds) {
        super(id, description, name, status);
        this.subtaskIds = subtaskIds;


    }
}


