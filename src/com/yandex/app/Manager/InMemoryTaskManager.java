package com.yandex.app.Manager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Service.Status;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private Map<Integer, Subtask> subtaskHashMap = new HashMap<>();
    private Map<Integer, Epic> epicHashMap = new HashMap<>();
    private Map<Integer, Task> taskHashMap = new HashMap<>();
    private final HistoryManager historyManager = Manager.getDefaultHistory();


    private int nextId = 1;

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
        Epic epic = epicHashMap.get(subtask.getIdEpic());
        if (epic != null && epic.getId() == subtask.getIdEpic()) {
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
            epic.setStatus(Status.NEW);
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
        inMemoryHistoryManager.remove(id);
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
                    inMemoryHistoryManager.remove(id);
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
            if (subtask != null && subtask.getIdEpic() == idEpic) {
                subtaskHashMap.remove(i);
                inMemoryHistoryManager.remove(i);
            }
        }
        epicHashMap.remove(idEpic);
        inMemoryHistoryManager.remove(idEpic);
    }

    @Override
    public Map<Integer, Task> printTask(int id) {
        Task task = taskHashMap.get(id);
        historyManager.addHistory(task);
        return Map.copyOf(taskHashMap);
    }

    @Override
    public Map<Integer, Subtask> printSubtask(int id) {
        Subtask subtask = subtaskHashMap.get(id);
        historyManager.addHistory(subtask);
        return Map.copyOf(subtaskHashMap);
    }

    @Override
    public Map<Integer, Epic> printEpic(int id) {
        Epic epic = epicHashMap.get(id);
        historyManager.addHistory(epic);
        return Map.copyOf(epicHashMap);
    }

    @Override
    public Map<Integer, Task> listTask() {
        return Map.copyOf(taskHashMap);
    }

    @Override
    public Map<Integer, Subtask> listSubtask() {
        return Map.copyOf(subtaskHashMap);
    }

    @Override
    public Map<Integer, Epic> listEpic() {
        return Map.copyOf(epicHashMap);
    }

    @Override
    public void listSubtaskForEpik(int idEpic) {
        for (Integer integer : subtaskHashMap.keySet()) {
            Subtask subtask = subtaskHashMap.get(integer);
            if (subtask.getIdEpic() == idEpic) {
                System.out.println(subtask.toString());
            }
        }
    }

    @Override
    public void updateStatusEpic(Subtask subtask) {
        Epic epic = epicHashMap.get(subtask.getIdEpic());
        ArrayList<Integer> epicStatus = epic.getSubtaskIds();
        if (epicStatus.isEmpty()) {
            epic.setStatus(Status.NEW);
        }
        Status status = Status.DONE;
        for (Integer i : epicHashMap.keySet()) {
            if (subtask.getStatus() == Status.NEW && epic.getStatus() == Status.NEW) {
                epic.setStatus(Status.NEW);
                status = Status.NEW;
            } else if (subtask.getStatus() == Status.DONE && status == Status.DONE) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
                status = Status.IN_PROGRESS;
            }
        }
    }
}