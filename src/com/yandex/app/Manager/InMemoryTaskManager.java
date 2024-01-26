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
    protected final Map<LocalDateTime, Task> prioritisedTasks = new TreeMap<>();
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
            updateTimeEpic(epic.getId());
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
        updateTimeEpic(epic.getId());
        return epic.getId();
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        prioritisedTasksRemove(task.getId());
        checkIntersectionOfTime(task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        prioritisedTasksRemove(subtask.getId());
        checkIntersectionOfTime(subtask);
        updateStatusEpic(subtask.getIdEpic());
        updateTimeEpic(subtask.getIdEpic());
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
            prioritisedTasksRemove(task.getId());
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
            prioritisedTasksRemove(subtask.getId());
        }
        subtasks.clear();
    }

    @Override
    public void cleanEpic() {
        for (Integer subtaskId : subtasks.keySet()){
            historyManager.remove(subtaskId);
        }
        for (Subtask subtask : subtasks.values()){
            historyManager.remove(subtask.getId());
            prioritisedTasksRemove(subtask.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeTask(int id) {
        prioritisedTasksRemove(id);
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        prioritisedTasksRemove(id);
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
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
            prioritisedTasksRemove(subtaskId);
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
        if(subtasks.size() == 0){
            return;
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
        for (LocalDateTime localDateTime : prioritisedTasks.keySet()) {
            Task task1 = prioritisedTasks.get(localDateTime);
            if (!task1.getEndTime().isAfter(task.getStartTime()))
                continue;
            if (!task1.getStartTime().isBefore(task.getEndTime()))
                continue;
            if (task.getId() == task1.getId())
                continue;
            if (task.getStartTime() == null || task1.getStartTime() == null)
                continue;
            else
                System.out.println("Задачи пересекаются!");
        }
    }

    void putPrioritizedTasks(Task task){
        if (task.getEndTime() != null) {
            prioritisedTasks.put(task.getStartTime(), task);
        }
        LocalDateTime off = LocalDateTime.now();
        for (LocalDateTime localDateTime : prioritisedTasks.keySet()) {
            Task task1 = prioritisedTasks.get(localDateTime);
            if (task.getEndTime().isAfter(task1.getEndTime()));
            off = task1.getEndTime().plusNanos(1);
        }
        prioritisedTasks.put(off, task);
    }

    void prioritisedTasksRemove(int id){
        Iterator<Task> prioritisedTasks1 = prioritisedTasks.values().iterator();
        while(prioritisedTasks1.hasNext()) {
            Task task = prioritisedTasks1.next();
            if (task.getId() == id){
                prioritisedTasks1.remove();
            }
        }
//        for (LocalDateTime localDateTime : prioritisedTasks.keySet()) {
//            Task task = prioritisedTasks.get(localDateTime);
//            if (task.getId() == id){
//                prioritisedTasks.remove(localDateTime);
//            }
//        }
    }

    public Map<LocalDateTime, Task> getPrioritizedTasks(){
        return new HashMap<>(prioritisedTasks);
    }

//    public static void main(String[] args) throws InterruptedException {
//        TaskManager taskManager = new InMemoryTaskManager();
//
//        taskManager.addEpic(new Epic( "1e","1ed", LocalDateTime.now(), LocalDateTime.now().plusSeconds(1)));
//        Thread.sleep(2000);
//        taskManager.addTask(new Task("1t","1td", LocalDateTime.now()));
//        Thread.sleep(2000);
//        taskManager.addSubtask(new Subtask("1s",NEW,"1sd", LocalDateTime.now(),1));
//        Thread.sleep(2000);
//        taskManager.addSubtask(new Subtask("2s",NEW,"2sd",LocalDateTime.now(),1));
//        Thread.sleep(2000);
//        taskManager.updateEpic(new Epic(1,"1e","1ed",LocalDateTime.now() ,LocalDateTime.now().plusSeconds(1)));


   // }
}