package Manager;


import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

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
        task.setId(nextId);
        this.taskHashMap.put(task.getId(), task);
        ++this.nextId;
        return task.getId();
    }

    public int addSubtask(Subtask subtask) {
        subtask.setId(nextId);
        Epic epic = epicHashMap.get(subtask.idEpic);
        if (epic != null && epic.getId() == subtask.idEpic) {
            epic.addSubtaskIds(subtask.getId());
            subtaskHashMap.put(subtask.getId(), subtask);
            updateStatusEpic(subtask);
            ++nextId;
            return subtask.getId();
        } else {
            System.out.println("Создай Epic!");
            return -1;
        }
    }

    public int addEpic(Epic epic) {
        epic.setId(nextId);
        epicHashMap.put(epic.getId(), epic);
        ++nextId;
        ArrayList<Integer> epicStatus = epic.getSubtaskIds();
        if (epicStatus.isEmpty())
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
        //Subtask subtask = subtaskHashMap.keySet();
        //for (Integer integer : epicHashMap.keySet()) {
            //Subtask subtask = (Subtask) subtaskHashMap.keySet();
           // if (id == subtask.idEpic){
                //subtaskHashMap.remove(integer);
            //}
        //}
        epicHashMap.remove(id);
    }

    public String printTask(int id) {
        Task task = taskHashMap.get(id);
        return task.toString();
    }

    public String printSubtask(int id) {
        Subtask subtask = subtaskHashMap.get(id);
        return subtask.toString();
    }

    public String printEpic(int id) {
        epicHashMap.remove(id);
        Epic epic = epicHashMap.get(id);
        return epic.toString();
    }

    public HashMap<Integer, Task> listTask() {
        return taskHashMap;
    }

    public HashMap<Integer, Subtask> listSubtask() {
        return subtaskHashMap;
    }

    public HashMap<Integer, Epic> listEpic() {
        return epicHashMap;
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
            if (subtask.getStatus() == "NEW" && epic.getStatus() == "NEW") {
                epic.setStatus("NEW");
                    status = "NEW";
            } else if (subtask.getStatus() == "DONE" && status == "DONE") {
                epic.setStatus("DONE");
            } else {
                epic.setStatus("IN_PROGRESS");
                    status = "IN_PROGRESS";
            }
        }
    }
}
