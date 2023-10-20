import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public HashMap<Integer, Subtask>subtaskHashMap = new HashMap<>();
    public HashMap<Integer, Epic>epicHashMap = new HashMap<>();
    public HashMap<Integer, Task>taskHashMap = new HashMap<>();
    int nextId = 1;

    public int addTask(Task task) {
        task.setId(nextId);
        taskHashMap.put(task.getId(), task);
        nextId++;
        return task.getId();
    }
    public int addSubtask(Subtask subtask) {
        ArrayList<Integer>subtaskIds = new ArrayList<>();
        subtask.setId(nextId);
        subtaskHashMap.put(subtask.getId(), subtask);
        nextId++;
        return subtask.getId();
    }
    public void addEpic(Epic epic) {
        epic.setId(nextId);
        epicHashMap.put(epic.getId(), epic);
        nextId++;

        String status = null;
        for (Integer subtaskId : epic.subtaskIds) {
            Epic sub = epicHashMap.get(subtaskId);
            if (sub.status == null){
                epic.status = "NEW";
            }else if(sub.status == "DONE"){
                epic.status = "DONE";
            }else {
                epic.status = "IN_PROGRESS";
            }
        }
    }
    public void updateTask(Task task){
        taskHashMap.put(task.getId(), task);
    }
    public void updateSubtask(Subtask subtask){
        subtaskHashMap.put(subtask.getId(), subtask);
    }
    public void updateEpic(Epic epic){
        epicHashMap.put(epic.getId(), epic);
    }
}
