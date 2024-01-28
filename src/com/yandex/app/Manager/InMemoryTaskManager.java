package com.yandex.app.Manager;
import com.sun.source.util.SourcePositions;
import com.yandex.app.Exception.ManagerSaveException;
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
    protected final Map<LocalDateTime, Task> prioritisedTasks = new TreeMap<>();
    protected final HistoryManager historyManager = Manager.getDefaultHistory();

    protected int nextId = 1;

    @Override
    public int addTask(Task task) {
        task.setId(nextId);
        tasks.put(task.getId(), task);
        checkIntersectionOfTime(task);
        putPrioritizedTasks(task);
        historyManager.addHistory(task);
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
            checkIntersectionOfTime(subtask);
            updateTimeEpic(epic.getId());
            putPrioritizedTasks(subtask);
            updateStatusEpic(subtask.getIdEpic());
            historyManager.addHistory(subtask);
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
        historyManager.addHistory(epic);
        ++nextId;
        epic.setStatus(NEW);
        updateTimeEpic(epic.getId());
        return epic.getId();
    }

    @Override
    public void updateTask(Task task) {
        prioritisedTasksRemove(task);
        tasks.put(task.getId(), task);
        checkIntersectionOfTime(task);
        prioritisedTasks.put(task.getEndTime(), task);
    }

    public void updateSubtask(Subtask subtask) {
        prioritisedTasksRemove(subtask);
        subtasks.put(subtask.getId(), subtask);
        checkIntersectionOfTime(subtask);
        updateStatusEpic(subtask.getIdEpic());
        updateTimeEpic(subtask.getIdEpic());
        prioritisedTasks.put(subtask.getEndTime(), subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
    }

    @Override
    public void cleanTask() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
            prioritisedTasksRemove(task);
        }
        tasks.clear();
    }

    @Override
    public void cleanSubtask() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateStatusEpic(epic.getId());
            updateTimeEpic(epic.getId());
        }
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
            prioritisedTasksRemove(subtask);
        }
        subtasks.clear();
    }

    @Override
    public void cleanEpic() {
        for (Subtask subtask : subtasks.values()){
            historyManager.remove(subtask.getId());
            prioritisedTasksRemove(subtask);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeTask(int id) {
        Task task = tasks.get(id);
        prioritisedTasksRemove(task);
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        Subtask subtask1 = subtasks.get(id);
        prioritisedTasksRemove(subtask1);
        Subtask subtask = subtasks.remove(id);
        Epic epic = epics.get(subtask.getIdEpic());
        updateTimeEpic(epic.getId());
        epic.getSubtaskIds().remove((Integer) id);
        updateStatusEpic(epic.getId());
        historyManager.remove(id);

    }

    @Override
    public void removeEpic(int idEpic) {
        Epic epic = epics.remove(idEpic);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            prioritisedTasks.remove(subtask.getEndTime());
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

    protected void updateTimeEpic(int epicId){
        Epic epic = epics.get(epicId);
        if(subtasks.isEmpty()){
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(0);
        }else{
            long dur = 0;
            LocalDateTime localDateTime = LocalDateTime.of(1,1,1,1,1);
            for (Integer subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask.getIdEpic() == epicId
                        && subtask.getStartTime().isAfter(epic.getStartTime())
                        && localDateTime.isBefore(epic.getStartTime().minusNanos(1))
                        && subtask.getEndTime() != null) {
                    localDateTime = subtask.getStartTime();
                    epic.setStartTime(subtask.getStartTime());
                }else if(subtask.getIdEpic() == epicId
                        && subtask.getEndTime().isAfter(epic.getEndTime())
                        && subtask.getEndTime() != null) {
                    epic.setEndTime(subtask.getStartTime());
                    dur = subtask.getDuration() + dur;
                }
            }
        }
    }

    protected void checkIntersectionOfTime(Task task){
        if (task.getStartTime() == null) {
            return;
        }
        for (Map.Entry<LocalDateTime, Task> val : prioritisedTasks.entrySet()) {
            Task task1 = val.getValue();
            if (!task1.getEndTime().isAfter(task.getStartTime()))
                continue;
            if (!task1.getStartTime().isBefore(task.getEndTime()))
                continue;
            else
                throw new ManagerSaveException("Пересечение задач " + task + " || " + task1);
        }
    }

    private void putPrioritizedTasks(Task task){
        if (task.getStartTime() != null) {
            prioritisedTasks.put(task.getStartTime(), task);
        }
    }


    private void prioritisedTasksRemove(Task task) {
        prioritisedTasks.remove(task.getStartTime());
    }

    public Map<LocalDateTime, Task> getPrioritizedTasks(){
        return new HashMap<>(prioritisedTasks);
    }
}