import java.util.ArrayList;

public class Epic extends Task {

    public ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String description, String name) {
        super(description, name);
    }

    public void addSubtaskIds (int id){
        subtaskIds.add(id);
    }

    public ArrayList<Integer>getSubtaskIds(){
        return subtaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + '\''+ id + '\'' + "subtaskIds=" + '\''+ subtaskIds + '\'' +
                ", description=" +'\''+ description + '\'' +
                ", name=" + name + ", status=" + status +'\''+ '}';
    }
}


