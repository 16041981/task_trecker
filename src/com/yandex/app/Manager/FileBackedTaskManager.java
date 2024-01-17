package com.yandex.app.Manager;

import com.yandex.app.Exception.ManagerSaveException;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import com.yandex.app.Model.Status;


import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager{

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file){
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());
            int generatorId = 0;
            List<Integer> history = Collections.emptyList();
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if(line.isEmpty()){
                    history = CSVTaskFormat.historyFromString(lines[i + 1]);
                    break;
                }
                final Task task = CSVTaskFormat.taskFromString(line);
                final int id = task.getId();
                if(id > generatorId){
                    generatorId = id;
                }
                switch (task.getType()) {
                    case TASK -> taskManager.tasks.put(id, task);
                    case EPIC -> taskManager.epics.put(id, (Epic)task);
                    case SUBTASK -> taskManager.subtasks.put(id, (Subtask)task);
                }
            }
            taskManager.nextId = generatorId + 1;
            for (Map.Entry<Integer, Subtask> e : taskManager.subtasks.entrySet()) {
                final Subtask subtask = e.getValue();
                final Epic epic = taskManager.epics.get(subtask.getIdEpic());
                epic.addSubtaskIds(subtask.getId());
            }
            for (Integer taskId : history) {
                Task task;
                if ((task = taskManager.tasks.get(taskId)) != null) {
                    taskManager.historyManager.addHistory(task);
                } else if ((task = taskManager.subtasks.get(taskId)) != null){
                    taskManager.historyManager.addHistory(task);
                } else if ((task = taskManager.epics.get(taskId)) != null){
                    taskManager.historyManager.addHistory(task);
                }//Аналогичные условия для подзадач/эпиков
//            for (Integer taskId : history){
//                taskManager.historyManager.addHistory(taskManager.getTask(taskId));
            }
        }catch (IOException e){
            throw new ManagerSaveException("Невозможно прочитать файл: " + file.getName(), e);
        }

        return taskManager;
    }

    public void save(){
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))){

            bufferedWriter.write(CSVTaskFormat.getHeader());
            bufferedWriter.newLine();

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()){
                final Task task = entry.getValue();
                bufferedWriter.write(CSVTaskFormat.toString(task));
                bufferedWriter.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()){
                final Task task = entry.getValue();
                bufferedWriter.write(CSVTaskFormat.toString(task));
                bufferedWriter.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()){
                final Task task = entry.getValue();
                bufferedWriter.write(CSVTaskFormat.toString(task));
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.write(CSVTaskFormat.historyToString(historyManager));

        }catch (IOException e){
            throw new ManagerSaveException("Ошибка сохранения файла: " + file.getName(), e);
        }
    }

    @Override
    public int addTask(Task task) {
        int addTask1 =  super.addTask(task);
        save();
        return addTask1;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int subtask1 = super.addSubtask(subtask);
        save();
        return subtask1;
    }

    @Override
    public int addEpic(Epic epic) {
        int epic1 = super.addEpic(epic);
        save();
        return epic1;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void cleanTask() {
        super.cleanTask();
        save();
    }

    @Override
    public void cleanSubtask() {
        super.cleanSubtask();
        save();
    }

    @Override
    public void cleanEpic() {
        super.cleanEpic();
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void removeEpic(int idEpic) {
        super.removeEpic(idEpic);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }
}

