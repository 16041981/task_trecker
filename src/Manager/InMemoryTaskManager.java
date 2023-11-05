package Manager;


import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    public HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    public HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    public HashMap<Integer, Task> taskHashMap = new HashMap<>();
    public ArrayList<Task> history = new ArrayList<>();

    int nextId = 1;
    int historyCount = 0;


    public InMemoryTaskManager() {
    }

    @Override
    public int addTask(Task task) {
        task.setId(nextId);
        taskHashMap.put(task.getId(), task);
        ++nextId;
        return task.getId();
    }

    @Override
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

    @Override
    public int addEpic(Epic epic) {
        epic.setId(nextId);
        epicHashMap.put(epic.getId(), epic);
        ++nextId;
        ArrayList<Integer> epicStatus = epic.getSubtaskIds();
        if (epicStatus.isEmpty())
            epic.setStatus("NEW");
        return epic.getId();
    }

    @Override
    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskHashMap.put(subtask.getId(), subtask);
        updateStatusEpic(subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    @Override
    public void cleanTask() {
        taskHashMap.clear();
    }

    @Override
    public void cleanSubtask(Subtask subtask) {
        subtaskHashMap.clear();
        updateStatusEpic(subtask);
    }

    @Override
    public void cleanEpic() {
        epicHashMap.clear();
    }

    @Override
    public void removeTask(int id) {
        taskHashMap.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        for (Integer integer : epicHashMap.keySet()) {
            int epicIdSab = 0;
            Epic epic = epicHashMap.get(integer);
            for (Integer subtaskId : epic.subtaskIds) {
                if (subtaskId.equals(id)) {
                    epic.removeSubtaskIds(epicIdSab);
                    subtaskHashMap.remove(id);
                    break;
                }
                epicIdSab++;
            }
        }
    }

    @Override
    public void removeEpic(int idEpic) {
        for (int i = 1; i < 50; i++) {
            Subtask subtask = subtaskHashMap.get(i);
            if (subtask != null && subtask.idEpic == idEpic) {
                subtaskHashMap.remove(i);
            }
        }
        epicHashMap.remove(idEpic);
    }

    @Override
    public String printTask(int id) {
        Task task = taskHashMap.get(id);
        history.add(task);
        getHistory();
        return task.toString();
    }

    @Override
    public String printSubtask(int id) {
        Subtask subtask = subtaskHashMap.get(id);
        history.add(subtask);
        getHistory();
        return subtask.toString();
    }

    @Override
    public String printEpic(int id) {
        Epic epic = epicHashMap.get(id);
        history.add(epic);
        getHistory();
        return epic.toString();
    }

    @Override
    public HashMap<Integer, Task> listTask() {
        return taskHashMap;
    }

    @Override
    public HashMap<Integer, Subtask> listSubtask() {
        return subtaskHashMap;
    }

    @Override
    public HashMap<Integer, Epic> listEpic() {
        return epicHashMap;
    }

    @Override
    public void listSubtaskForEpik(int idEpic) {
        for (Integer integer : subtaskHashMap.keySet()) {
            Subtask subtask = subtaskHashMap.get(integer);
            if (subtask.idEpic == idEpic) {
                System.out.println(subtask.toString());
            }
        }
    }

    @Override
    public void updateStatusEpic(Subtask subtask) {
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
    public String getHistory(){
        if (history.size()>10) {
            history.remove(0);
        }
        return history.toString();
    }
}