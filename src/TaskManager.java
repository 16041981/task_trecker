import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    public HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    public HashMap<Integer, Task> taskHashMap = new HashMap<>();

    int nextId = 1;

    public TaskManager() {
    }

    public int addTask(Task task) {
        task.setId(this.nextId);
        this.taskHashMap.put(task.getId(), task);
        ++this.nextId;
        return task.getId();
    }

    public int addSubtask(Subtask subtask) {
        subtask.setId(this.nextId);
        Epic epic = (Epic)this.epicHashMap.get(subtask.idEpic);
        if (epic != null && epic.id == subtask.idEpic) {
            epic.addSubtaskIds(subtask.id);
            this.subtaskHashMap.put(subtask.getId(), subtask);
            this.updateStatusEpic(subtask);
            ++this.nextId;
            return subtask.getId();
        } else {
            System.out.println("Создай Epic!");
            return -1;
        }
    }

    public int addEpic(Epic epic) {
        epic.setId(this.nextId);
        this.epicHashMap.put(epic.getId(), epic);
        ++this.nextId;
        ArrayList<Integer> epicStatus = epic.getSubtaskIds();
        if (epicStatus.isEmpty()) {
        }

        epic.setStatus("NEW");
        return epic.getId();
    }

    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskHashMap.put(subtask.getId(), subtask);
        updateStatusEpic(subtask);
    }

    public void updateEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    public void cleanTask() {
        taskHashMap.clear();
    }

    public void cleanSubtask(Subtask subtask) {
        subtaskHashMap.clear();
        updateStatusEpic(subtask);
    }

    public void cleanEpic() {
        epicHashMap.clear();
    }

    public void removeTask(int id) {
        taskHashMap.remove(id);
    }

    public void removeSubtask(int id) {
        subtaskHashMap.remove(id);
    }

    public void removeEpic(int id) {
        epicHashMap.remove(id);
    }

    String printTask(int id) {
        Task task = taskHashMap.get(id);
        return task.toString();
    }

    String printSubtask(int id) {
        Subtask subtask = subtaskHashMap.get(id);
        return subtask.toString();
    }

    String printEpic(int id) {
        epicHashMap.remove(id);
        Epic epic = epicHashMap.get(id);
        return epic.toString();
    }

    public void listTask() {
        for (Integer integer: taskHashMap.keySet()) {
            Task task = taskHashMap.get(integer);
            System.out.println(task.toString());
        }

    }

    public void listSubtask() {
        for (Integer integer: subtaskHashMap.keySet()) {
            Subtask subtask = subtaskHashMap.get(integer);
            System.out.println(subtask.toString());
        }

    }

    public void listEpic() {
        for (Integer integer : epicHashMap.keySet()) {
            Epic epic = epicHashMap.get(integer);
            System.out.println(epic.toString());
        }
    }

    public void listSubtaskForEpik(int idEpic) {
        for (Integer integer : subtaskHashMap.keySet()) {
            Subtask subtask = subtaskHashMap.get(integer);
            if (subtask.idEpic == idEpic) {
                System.out.println(subtask.toString());
            }
        }
    }

    void updateStatusEpic(Subtask subtask) {
        Epic epic = epicHashMap.get(subtask.idEpic);
        ArrayList<Integer> epicStatus = epic.getSubtaskIds();
        if (epicStatus.isEmpty()) {
            epic.setStatus("NEW");
        }
        String status = "DONE";
        for (Integer i : epicHashMap.keySet()) {
            if (subtask.status == "NEW" && epic.status == "NEW") {
                epic.setStatus("NEW");
                    status = "NEW";
            } else if (subtask.status == "DONE" && status == "DONE") {
                epic.setStatus("DONE");
            } else {
                epic.setStatus("IN_PROGRESS");
                    status = "IN_PROGRESS";
            }
        }
    }
}
