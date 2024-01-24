package com.yandex.app.Manager;
import com.sun.source.util.SourcePositions;
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
        checkIntersectionOfTime(task);
        putPrioritizedTasks(task);
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
            updateEndTimeEpic(epic.getId());
            updateDurationEpic(epic.getId());
            putPrioritizedTasks(subtask);
            updateStatusEpic(subtask.getIdEpic());
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
        updateStartTimeEpic(epic.getId());
        updateEndTimeEpic(epic.getId());
        updateDurationEpic(epic.getId());
        putPrioritizedTasks(epic);
        return epic.getId();
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        checkIntersectionOfTime(task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        checkIntersectionOfTime(subtask);
        updateStatusEpic(subtask.getIdEpic());
        updateStartTimeEpic(subtask.getIdEpic());
        updateEndTimeEpic(subtask.getIdEpic());
        updateDurationEpic(subtask.getIdEpic());
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
        updateStartTimeEpic(epic.getId());
        updateEndTimeEpic(epic.getId());
        updateDurationEpic(epic.getId());
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
        putPrioritizedTasks(subtask);
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

    protected void updateEndTimeEpic(int epicId){
        Epic epic = epics.get(epicId);
        if(subtasks.size() == 0){
            return;
        }else{
            for (Integer integer : subtasks.keySet()) {
                Subtask subtask = subtasks.get(integer);
                if (subtask.getIdEpic() == epicId && subtask.getEndTime().isAfter(epic.getEndTime()))
                    epic.setEndTime(subtask.getStartTime());
            }
        }
    }

    protected void updateStartTimeEpic(int epicId){
        Epic epic = epics.get(epicId);
        if(subtasks.size() == 0){
            return;
        }else{
            LocalDateTime localDateTime = LocalDateTime.of(1,1,1,1,1);
            for (Integer integer : subtasks.keySet()) {
                Subtask subtask = subtasks.get(integer);
                if (subtask.getIdEpic() == epicId &&
                        subtask.getStartTime().isAfter(epic.getStartTime()) &&
                        localDateTime.isBefore(epic.getStartTime().minusNanos(1))) {
                    localDateTime = subtask.getStartTime();
                    epic.setStartTime(subtask.getStartTime());
                }
            }
        }
    }

    protected void updateDurationEpic(int epicId){
        Epic epic = epics.get(epicId);
        if(subtasks.size() == 0){
            return;
        }else{
            long dur = 0;
            for (Integer integer : subtasks.keySet()) {
                Subtask subtask = subtasks.get(integer);
                if (subtask.getIdEpic() == epic.getId()) {
                    dur =  subtask.getDuration() + dur;
                }
            }
        }
    }

    protected void checkIntersectionOfTime(Task task){
        for (LocalDateTime localDateTime : getPrioritizedTasks().keySet()) {
            Task task1 = getPrioritizedTasks().get(localDateTime);
            if (task1.getEndTime().isAfter(task.getStartTime())) {
                System.out.println("Задачи пересекаются!");
            }
        }
    }

    void putPrioritizedTasks(Task task){
        prioritisedTasks.put(task.getEndTime(), task);
    }

    Map<LocalDateTime, Task> getPrioritizedTasks(){
        return prioritisedTasks;
    }

    public static void main(String[] args) throws InterruptedException {
        TaskManager taskManager = new InMemoryTaskManager();

        taskManager.addEpic(new Epic( "1e","1ed", LocalDateTime.now(), LocalDateTime.now().plusSeconds(1)));
        Thread.sleep(2000);
        taskManager.addTask(new Task("1t","1td", LocalDateTime.now()));
        Thread.sleep(2000);
        taskManager.addSubtask(new Subtask("1s",NEW,"1sd", LocalDateTime.now(),1));
        Thread.sleep(2000);
        taskManager.addSubtask(new Subtask("2s",NEW,"2sd",LocalDateTime.now(),1));
        Thread.sleep(2000);
        taskManager.updateEpic(new Epic(1,"1e","1ed",LocalDateTime.now() ,LocalDateTime.now().plusSeconds(1)));

        System.out.println(prioritisedTasks.values());

    }
}