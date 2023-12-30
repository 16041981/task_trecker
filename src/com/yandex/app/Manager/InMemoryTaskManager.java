package com.yandex.app.Manager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Status;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer, Subtask> subtaskHashMap = new HashMap<>();
    protected Map<Integer, Epic> epicHashMap = new HashMap<>();
    protected Map<Integer, Task> taskHashMap = new HashMap<>();
    private final HistoryManager historyManager = Manager.getDefaultHistory();

    private int nextId = 1;

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
        if (epic != null) {
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
        Epic oldEpic = epicHashMap.get(epic.getId());
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
    }

    @Override
    public void cleanTask() {
        for (int i = 0; i < taskHashMap.size(); i++){
            Task task = taskHashMap.get(i);
            int q = task.getId();
            historyManager.remove(q);
        }
        taskHashMap.clear();
    }

    @Override
    public void cleanSubtask() {
        for (int i = 0; i < epicHashMap.size(); i++){
            Epic epic = epicHashMap.get(i);
            epic.getSubtaskIds().clear();
        }
        for (int i = 0; i < subtaskHashMap.size(); i++){
            Subtask subtask = subtaskHashMap.get(i);
            int q = subtask.getId();
            historyManager.remove(q);
            updateStatusEpic(subtask);
        }
        subtaskHashMap.clear();
    }

    @Override
    public void cleanEpic() {
        for (int i = 0; i < subtaskHashMap.size(); i++){
            Subtask subtask1 = subtaskHashMap.get(i);
            int q = subtask1.getId();
            historyManager.remove(q);
        }
        for (int i = 0; i < epicHashMap.size(); i++){
            Epic epic = epicHashMap.get(i);
            int q = epic.getId();
            historyManager.remove(q);
        }
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    @Override
    public void removeTask(int id) {
        taskHashMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
            int epicIdSab = 0;
            Epic epic = epicHashMap.get(id);
            for (Integer subtaskId : epic.getSubtaskIds()) {
                Subtask subtask= subtaskHashMap.get(subtaskId);
                if (subtaskId.equals(id)) {
                    epic.removeSubtaskIds(epicIdSab);
                    subtaskHashMap.remove(id);
                    historyManager.remove(id);
                    break;
                }
                epicIdSab++;
                updateStatusEpic(subtask);
        }

    }

    @Override
    public void removeEpic(int idEpic) {
        Epic epic = epicHashMap.remove(idEpic);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtaskHashMap.get(subtaskId);
            if (subtask != null && subtask.getIdEpic() == idEpic) {
                subtaskHashMap.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
        }
        epicHashMap.remove(idEpic);
        historyManager.remove(idEpic);
    }

    @Override
    public Task getTask(int id) {
        Task task = taskHashMap.get(id);
        historyManager.addHistory(task);
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtaskHashMap.get(id);
        historyManager.addHistory(subtask);
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epicHashMap.get(id);
        historyManager.addHistory(epic);
        return epic;
    }

    @Override
    public List<Task> listTask() {
        return new ArrayList<>(taskHashMap.values());
    }

    @Override
    public List<Subtask> listSubtask() {
        return new ArrayList<>(subtaskHashMap.values());
    }

    @Override
    public List<Epic> listEpic() {
        return new ArrayList<>(epicHashMap.values());
    }

    @Override
    public List<Subtask> listSubtaskForEpik(int idEpic) {
        List <Subtask> list = new ArrayList<>();
        Epic epic = epicHashMap.get(idEpic);
        for (Integer integer : epic.getSubtaskIds()) {
            Subtask subtask = subtaskHashMap.get(integer);
            if (subtask.getIdEpic() == idEpic) {
                list.add(subtask);
            }
        }
        return list;
    }

    @Override
    public void updateStatusEpic(Subtask subtask) {
        Epic epic = epicHashMap.get(subtask.getIdEpic());
        List<Integer> epicStatus = epic.getSubtaskIds();
        if (epicStatus.isEmpty()) {
            epic.setStatus(Status.NEW);
        }
        for (Integer ignored : epic.getSubtaskIds()){
            if (subtask.getStatus() == Status.DONE) {
                epic.setStatus(Status.DONE);
            } else {
                break;
            }
        }
        Status status = Status.DONE;
        for (Integer ignored : epic.getSubtaskIds()) {
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