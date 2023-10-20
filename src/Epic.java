import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subtaskIds = new ArrayList<>();
    public Epic (String description, String name, String status) {
        super(description, name, status);
    }
    public Epic (int id, String description, String name, String status) {
        super(id, description, name, status);
    }

    public void addSubtaskId(int id){
        subtaskIds.add(id);
    }
}
