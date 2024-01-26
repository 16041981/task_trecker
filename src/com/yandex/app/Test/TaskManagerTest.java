package com.yandex.app.Test;

import com.yandex.app.Manager.TaskManager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Status;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.yandex.app.Model.Status.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    @Test
    void updateStatusEpic() {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int epicId = taskManager.addEpic(epic);

        assertEquals(NEW, epic.getStatus(), "Неверно определен стаус.");

        subtask = new Subtask(
                2,
                "subtask description",
                NEW,
                "subtask",
                LocalDateTime.of(2024,01,01,01,02),
                epicId
        );
        final int subtaskId = taskManager.addSubtask(subtask);

        Subtask subtask1 = new Subtask(
                3,
                "subtask description",
                NEW,
                "subtask",
                LocalDateTime.of(2024,01,01,01,03),
                epicId
        );

        final int subtaskId1 = taskManager.addSubtask(subtask1);
        taskManager.updateEpic(epic);
        assertEquals(NEW, epic.getStatus(), "Неверно определен стаус.");

        taskManager.updateSubtask(
                //subtask = new Subtask(subtaskId,"subtask description", "subtask", IN_PROGRESS, epicId)
                subtask = new Subtask(
                        2,
                        "subtask description",
                        IN_PROGRESS,
                        "subtask",
                        LocalDateTime.of(2024,01,01,01,02),
                        epicId
                )
        );

        taskManager.updateEpic(epic);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Неверно определен стаус.");

        taskManager.updateSubtask(
                //subtask = new Subtask(subtaskId,"subtask description", "subtask", DONE, epicId)
                subtask = new Subtask(
                        2,
                        "subtask description",
                        DONE,
                        "subtask",
                        LocalDateTime.of(2024,01,01,01,02),
                        epicId
                )
        );

        taskManager.updateEpic(epic);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Неверно определен стаус.");

        taskManager.updateSubtask(
                //subtask1 = new Subtask( subtaskId1,"subtask description1", "subtask1", DONE, epicId)
                subtask1 = new Subtask(
                        3,
                        "subtask description",
                        DONE,
                        "subtask",
                        LocalDateTime.of(2024,01,01,01,03),
                        epicId
                )

        );

        taskManager.updateEpic(epic);
        assertEquals(DONE, epic.getStatus(), "Неверно определен стаус.");

    }

    @Test
    void addTask() {
        task = new Task(
                "Test addNewTask",
                NEW,
                "Test addNewTask description",
                LocalDateTime.now().plusNanos(1));
        final int taskId = taskManager.addTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.listTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        taskManager.cleanTask();
    }

    @Test
    void addSubtask() {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int epicId = taskManager.addEpic(epic);
        subtask = new Subtask(
                "subtask description",
                NEW,
                "subtask",
                LocalDateTime.now().plusNanos(3),
                epicId);
        final int subtaskId = taskManager.addSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Подзадачи не найдены.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = taskManager.listSubtask();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
        taskManager.cleanSubtask();
        taskManager.cleanEpic();
    }

    @Test
    void addEpic() {
        epic = new Epic("Test addNewEpic description", "Test addNewEpic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int taskId = taskManager.addEpic(epic);

        final Task savedEpic = taskManager.getEpic(taskId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.listEpic();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
        taskManager.cleanEpic();
    }

    @Test
    void updateTask() {
        task = new Task(
                "Test addNewTask",
                NEW,
                "Test addNewTask description",
                LocalDateTime.now().plusNanos(1));
        final int taskId = taskManager.addTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        taskManager.updateTask(
        task = new Task(taskId,
                "Test addNewTask1",
                IN_PROGRESS ,
                "Test addNewTask description1",
                LocalDateTime.now().plusNanos(2)));


        assertNotNull(savedTask, "Задача не найдена.");
        assertNotEquals(task, savedTask, "Задача не обновилась.");

        final List<Task> tasks = taskManager.listTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        taskManager.cleanTask();

    }

    @Test
    void updateSubtask() {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int epicId = taskManager.addEpic(epic);
        subtask = new Subtask(
                "subtask description", NEW,"subtask",  LocalDateTime.now().plusNanos(4), epicId
        );
        final int subtaskId = taskManager.addSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        taskManager.updateSubtask(
        subtask = new Subtask(subtaskId,
                "subtask description", IN_PROGRESS,"subtask",  LocalDateTime.now().plusNanos(5), epicId)
        );
        final List<Subtask> subtasks = taskManager.listSubtask();
        final Subtask savedSubtask1 = subtasks.get(0);


        assertNotNull(savedSubtask1, "Подзадачи не найдены.");
        assertNotEquals(savedSubtask1, savedSubtask, "Подзадача не обновилась.");
        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        taskManager.cleanSubtask();
        taskManager.cleanEpic();

    }

    @Test
    void updateEpic() {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int epicId = taskManager.addEpic(epic);

//        subtask = new Subtask(
//                "subtask description", NEW,"subtask",  LocalDateTime.now().plusNanos(6), epicId
//        );
        subtask = new Subtask(
                2,
                "subtask description",
                DONE,
                "subtask",
                LocalDateTime.of(2024,01,01,01,02),
                epicId
        );
        final int subtaskId = taskManager.addSubtask(subtask);
        //Subtask subtask1 = new Subtask( "subtask description1", "subtask1", NEW, epicId);/
        Subtask subtask1 = new Subtask(
                3,
                "subtask description",
                NEW,
                "subtask",
                LocalDateTime.of(2024,01,01,01,03),
                epicId
        );
        taskManager.addSubtask(subtask1);
        taskManager.updateEpic(epic);


        assertNotNull(epic, "Эпик не найден.");
        assertEquals(2, epic.getSubtaskIds().size(), "Неверное количество подзадач.");
        taskManager.cleanSubtask();
        taskManager.cleanEpic();
    }

    @Test
    void cleanTask() {
        task = new Task(
                "Test addNewTask",
                NEW,
                "Test addNewTask description",
                LocalDateTime.now().plusNanos(9));
        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задачи на возвращаются.");
        taskManager.cleanTask();

        List<Task> tasks = taskManager.listTask();
        assertEquals(tasks.size(),0, "Задачи не очищены.");
    }

    @Test
    void cleanSubtask() {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int epicId = taskManager.addEpic(epic);
        subtask = new Subtask(
                "subtask description", IN_PROGRESS,"subtask",  LocalDateTime.now().plusNanos(10), epicId
        );
        final int subtaskId = taskManager.addSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Подзадачи на возвращаются.");
        taskManager.cleanSubtask();

        List<Subtask> subtasks = taskManager.listSubtask();
        assertEquals(subtasks.size(), 0, "Подзадачи не очищены.");
    }

    @Test
    void cleanEpic() {
        epic = new Epic("Test addNewEpic description", "Test addNewEpic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int taskId = taskManager.addEpic(epic);

        final Task savedEpic = taskManager.getEpic(taskId);

        assertNotNull(savedEpic, "Эпик не найден.");
        taskManager.cleanEpic();

        List<Epic> epics = taskManager.listEpic();
        assertEquals(epics.size(), 0 , "Эпики не очищены");
    }

    @Test
    void removeTask() {
        task = new Task(
                "Test addNewTask",
                NEW,
                "Test addNewTask description",
                LocalDateTime.now().plusNanos(10));
        final int taskId = taskManager.addTask(task);

        List<Task> tasks = taskManager.listTask();
        assertNotNull(tasks.get(0), "Задача на возвращается.");
        taskManager.removeTask(taskId);

        List<Task> tasks1 = taskManager.listTask();
        assertEquals(tasks1.size(),0, "Задачa не удалена.");
    }

    @Test
    void removeSubtask() {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int epicId = taskManager.addEpic(epic);
        subtask = new Subtask(
                "subtask description", IN_PROGRESS,"subtask",  LocalDateTime.now().plusNanos(11), epicId
        );
        final int subtaskId = taskManager.addSubtask(subtask);

        List<Subtask> subtasks = taskManager.listSubtask();
        assertNotNull(subtasks.get(0), "Подзадача не возвращается");
        taskManager.removeSubtask(subtaskId);

        List<Subtask> subtasks1 = taskManager.listSubtask();
        assertEquals(subtasks1.size(),0,"Подзадача не удалена.");
    }

    @Test
    void removeEpic() {
        epic = new Epic("Test addNewEpic description", "Test addNewEpic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int taskId = taskManager.addEpic(epic);

        List<Epic> epics = taskManager.listEpic();
        assertNotNull(epics.get(0),"Епик не возвращается.");
        taskManager.removeEpic(taskId);

        List<Epic> epics1 = taskManager.listEpic();
        assertEquals(epics1.size(),0,"Епик не удален.");

    }

    @Test
    void getTask() {
        task = new Task(
                "Test addNewTask",
                NEW,
                "Test addNewTask description",
                LocalDateTime.now().plusNanos(11));
        final int taskId = taskManager.addTask(task);

        Task task1 = taskManager.getTask(taskId);
        assertNotNull(task1, "Задача на возвращается.");
        assertEquals(task, task1, "Возвращается другая задача.");

    }

    @Test
    void getSubtask() {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int epicId = taskManager.addEpic(epic);
        subtask = new Subtask(
                "subtask description", IN_PROGRESS,"subtask",  LocalDateTime.now().plusNanos(12), epicId
        );
        final int subtaskId = taskManager.addSubtask(subtask);

        Subtask subtask1 = taskManager.getSubtask(subtaskId);
        assertNotNull(subtask1, "Подзадача на возвращается.");
        assertEquals(subtask, subtask1, "Возвращается другая подзадача.");
    }

    @Test
    void getEpic() {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int epicId = taskManager.addEpic(epic);

        Epic epic1 = taskManager.getEpic(epicId);
        assertNotNull(epic1, "Эпик не возвращается");
        assertEquals(epic1, epic, "Возвращается другой эпик.");
    }

    @Test
    void listTask() {
        task = new Task(
                "Test addNewTask",
                NEW,
                "Test addNewTask description",
                LocalDateTime.now().plusNanos(13));
        List<Task> tasks = List.of(task);
        taskManager.addTask(task);
        List<Task> tasks1 = taskManager.listTask();

        assertNotNull(tasks1, "Список не возвращается");
        assertEquals(tasks1, tasks, "Возвращаемый список отличаетсяю");
    }

    @Test
    void listSubtask() {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int epicId = taskManager.addEpic(epic);
        subtask = new Subtask(
                "subtask description", IN_PROGRESS,"subtask",  LocalDateTime.now().plusNanos(14), epicId
        );
        List<Subtask> subtasks = List.of(subtask);
        taskManager.addSubtask(subtask);
        List<Subtask> subtasks1 = taskManager.listSubtask();

        assertNotNull(subtasks1, "Список не возвращается");
        assertEquals(subtasks1, subtasks, "Возвращаемый список отличаетсяю");
    }

    @Test
    void listEpic() {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        List<Epic> epics = List.of(epic);
        taskManager.addEpic(epic);
        List<Epic> epics1 = taskManager.listEpic();

        assertNotNull(epics1, "Список не возвращается");
        assertEquals(epics1, epics, "Возвращаемый список отличаетсяю");
    }

    @Test
    void listSubtaskForEpic() {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        final int epicId = taskManager.addEpic(epic);
        subtask = new Subtask(
                "subtask description", NEW,"subtask",  LocalDateTime.now().plusNanos(16), epicId
        );
        taskManager.addSubtask(subtask);
        Subtask subtask1 = new Subtask(
                "subtask description1", NEW,"subtask1",  LocalDateTime.now().plusNanos(17), epicId
        );
        taskManager.addSubtask(subtask1);

        List<Subtask> subtasks = List.of(subtask, subtask1);

        assertNotNull(taskManager.listSubtaskForEpic(epicId), "Список не возвращается");
        assertEquals(taskManager.listSubtaskForEpic(epicId), subtasks, "Возвращаемый список отличаетсяю");

    }
}
