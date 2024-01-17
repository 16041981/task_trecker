package com.yandex.app.Manager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Status;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;

import java.time.LocalDateTime;
import java.util.*;

import static com.yandex.app.Model.Status.NEW;

public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected static final Map<LocalDateTime, Task> prioritisedTasks = new TreeMap<>();
    protected final HistoryManager historyManager = Manager.getDefaultHistory();

    protected int nextId = 1;

    @Override
    public int addTask(Task task) {
        task.setId(nextId);
        tasks.put(task.getId(), task);
        getPrioritizedTasks(task);
        ++nextId;
        return task.getId();
    }

    @Override
    public int addSubtask(Subtask subtask) {
        subtask.setId(nextId);
        Epic epic = epics.get(subtask.getIdEpic());
        if (epic != null) {
            epic.addSubtaskIds(subtask.getId());
            subtasks.put(subtask.getId(), subtask);
            updateStatusEpic(subtask.getIdEpic());
            getPrioritizedTasks(subtask);
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
        epics.put(epic.getId(), epic);
        ++nextId;
        epic.setStatus(NEW);
        //getPrioritizedTasks(epic);
        return epic.getId();
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateStatusEpic(subtask.getIdEpic());
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
    }

    @Override
    public void cleanTask() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void cleanSubtask() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateStatusEpic(epic.getId());
        }
        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtasks.clear();
    }

    @Override
    public void cleanEpic() {
        for (Integer subtaskId : subtasks.keySet()){
            historyManager.remove(subtaskId);
        }
        for (Integer subtaskId : subtasks.keySet()){
            historyManager.remove(subtaskId);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        int epicIdSab = 0;
//        Subtask subtask = subtasks.remove(id);
//        Epic epic = epics.get(subtask.getIdEpic());
//        for (Integer subtaskId : epic.getSubtaskIds()) {
//            if (subtaskId.equals(id)) {
//                epic.removeSubtaskIds(epicIdSab);
//                subtasks.remove(id);
//                historyManager.remove(id);
//                break;
//            }
//            epicIdSab++;
//            updateStatusEpic(subtask.getId());
//        }
        Subtask subtask = subtasks.remove(id);
        Epic epic = epics.get(subtask.getIdEpic());
        epic.getSubtaskIds().remove((Integer) id);
        updateStatusEpic(epic.getId());
        historyManager.remove(id);

    }

    @Override
    public void removeEpic(int idEpic) {
        Epic epic = epics.remove(idEpic);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        historyManager.remove(idEpic);
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.addHistory(task);
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.addHistory(subtask);
        getPrioritizedTasks(subtask);
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.addHistory(epic);
        return epic;
    }

    @Override
    public List<Task> listTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> listSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> listEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> listSubtaskForEpic(int idEpic) {
        List <Subtask> list = new ArrayList<>();
        Epic epic = epics.get(idEpic);
        for (Integer integer : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(integer);
            list.add(subtask);
        }
        return list;
    }

    protected void updateStatusEpic(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subtaskIds = epic.getSubtaskIds();
        int statusNew = 0;
        int statusDone = 0;
        for (Integer subtaskId : subtaskIds) {
            Status status = subtasks.get(subtaskId).getStatus();
            if (status == Status.DONE) {
                statusDone++;
            } else if (status == NEW) {
                statusNew++;
            } else {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        if (subtaskIds.size() == statusNew) {
            epic.setStatus(NEW);
        } else if (subtaskIds.size() == statusDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    protected void updateEndTimeEpic(int epicId, Map<Integer, Subtask> map){
        Epic epic = epics.get(epicId);
        for (int i = 0; i < map.size(); i++) {
            Subtask subtask = map.get(i);
            if (subtask.getEndTime().isAfter(epic.getEndTime()) && subtask.getIdEpic() == epic.getId()) {
                epic.setEndTime(subtask.getEndTime());
            }
        }
    }

    protected void updateStartTimeEpic(int epicId, Map<Integer, Subtask> map){
        Epic epic = epics.get(epicId);
        for (int i = 0; i < map.size(); i++) {
            Subtask subtask = map.get(i);
            if (epic.getStartTime().isAfter(subtask.getStartTime()) || epic.getStartTime() == null &&
                            subtask.getIdEpic() == epic.getId()) {
                epic.setStartTime(subtask.getStartTime());
            }
        }
    }

    protected void updateDurationEpic(int epicId, Map<Integer, Subtask> map){
        Epic epic = epics.get(epicId);
        for (int i = 0; i < map.size(); i++) {
            Subtask subtask = map.get(i);
            if (subtask.getIdEpic() == epic.getId()){
                epic.setDuration(subtask.getDuration() + epic.getDuration());
            }
        }
    }

    void getPrioritizedTasks(Task task){
        prioritisedTasks.put(task.getEndTime(), task);
    }

    public static void main(String[] args) throws InterruptedException {
        TaskManager taskManager = new InMemoryTaskManager();

        taskManager.addEpic(new Epic("","",LocalDateTime.now()));
        Thread.sleep(1000);
        taskManager.addTask(new Task("vvvvvvvvvvvvvvvvvv","rrrrrrrrrrrrrr", LocalDateTime.now()));
        Thread.sleep(1000);
        taskManager.addSubtask(new Subtask("qqqqqqqqqqqqqqqqqq",NEW,"ssssssssssssss", LocalDateTime.now(),1));

        System.out.println(prioritisedTasks.values());

    }
}