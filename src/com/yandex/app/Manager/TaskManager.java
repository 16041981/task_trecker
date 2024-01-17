package com.yandex.app.Manager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    int addTask(Task task);


    int addSubtask(Subtask subtask);

    int addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void cleanTask();

    void cleanSubtask();

    void cleanEpic();

    void removeTask(int id);

    void removeSubtask(int id);

    void removeEpic(int idEpic);

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    List<Task> listTask();

    List<Subtask> listSubtask();

    List<Epic> listEpic();

    List<Subtask> listSubtaskForEpic(int idEpic);
}