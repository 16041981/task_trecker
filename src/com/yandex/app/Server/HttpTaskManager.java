package com.yandex.app.Server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.Manager.FileBackedTaskManager;
import com.yandex.app.Manager.Manager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import com.yandex.app.Model.TaskTupe;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTaskManager {
    private final Gson gson;
    private final KVTaskClient client;


    public HttpTaskManager(int port){
        this(port,false);
    }

    public HttpTaskManager (int port, boolean load){
        super(null);
        gson = Manager.getGson();
        client = new KVTaskClient(port);
        if(load){
            load();
        }
    }

    protected void addTasks(List<? extends Task> tasks) {
        for(Task task : tasks) {
            final int id = task.getId();
            TaskTupe type = task.getType();
            if (id > nextId) {
                nextId = id +1;
            }
            if (type == TaskTupe.TASK){
                this.tasks.put(id, task);
                prioritisedTasks.put(task.getStartTime(), task);
            }else if (type == TaskTupe.SUBTASK){
                subtasks.put(id, (Subtask) task);
                prioritisedTasks.put(task.getStartTime(), task);
            }else if (type == TaskTupe.EPIC){
                epics.put(id, (Epic) task);
            }
        }
    }

    private void load(){
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>(){
        }.getType());
        addTasks(tasks);

        ArrayList<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>(){
        }.getType());
        addTasks(epics);

        ArrayList<Subtask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>(){
        }.getType());
        addTasks(subtasks);

        ArrayList<Integer> history = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>(){
        }.getType());

         for (Integer taskId : history){
             Task task = tasks.get(taskId);
             if (task != null){
                 historyManager.addHistory(task);
             }else if ((task = subtasks.get(taskId)) != null){
                 historyManager.addHistory(task);
             }else if ((task = epics.get(taskId)) != null){
                 historyManager.addHistory(task);
             }
         }
    }

    @Override
    public void save(){
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        client.put("tasks", jsonTasks);
        String jsonSubtasks = gson.toJson(new ArrayList<>(subtasks.values()));
        client.put("subtasks", jsonSubtasks);
        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        client.put("epics", jsonEpics);
        String jsonHistory = gson.toJson(
                historyManager.getHistory().stream().map(Task::getId).collect(Collectors.toList())
        );
        client.put("history", jsonHistory);
    }

}
