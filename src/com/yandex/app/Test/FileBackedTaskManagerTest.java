package com.yandex.app.Test;

import com.yandex.app.Manager.FileBackedTaskManager;
import com.yandex.app.Manager.HistoryManager;
import com.yandex.app.Manager.Manager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;

import static com.yandex.app.Model.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    protected final HistoryManager historyManager = Manager.getDefaultHistory();

    private File file;

    @BeforeEach
    public void setUp(){
        file = new File("TaskHistury.csv");
        taskManager = new FileBackedTaskManager(file);

    }

    @AfterEach
    protected void tearDown(){assertTrue(file.delete());}

    @Test
    public void TestEqualsLoadFromFileAndTaskManager(){
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        taskManager.addEpic(epic);
        final int epicId = epic.getId();

        subtask = new Subtask(
                2,
                "subtask description",
                NEW,
                "subtask",
                LocalDateTime.of(2024,01,01,01,02),
                epicId
        );
        taskManager.addSubtask(subtask);

        Subtask subtask1 = new Subtask(
                3,
                "subtask description",
                NEW,
                "subtask",
                LocalDateTime.of(2024,01,01,01,03),
                epicId
        );
        taskManager.addSubtask(subtask1);

        task = new Task(
                "Test addNewTask",
                NEW,
                "Test addNewTask description",
                LocalDateTime.now().plusNanos(1));
        taskManager.addTask(task);
        taskManager.save();


        FileBackedTaskManager taskManager1 = FileBackedTaskManager.loadFromFile(file);

        assertEquals(taskManager1.listEpic(), taskManager.listEpic(),
                "Список задач после выгрузки не совпададает");
        assertEquals(taskManager1.listSubtask(), taskManager.listSubtask(),
                "Список задач после выгрузки не совпададает");
        assertEquals(taskManager1.listTask(), taskManager.listTask(),
                "Список задач после выгрузки не совпададает");

    }

    @Test
    public void TestSaveAndloadFromFile(){
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        taskManager.addEpic(epic);
        final int epicId = epic.getId();
        //subtask = new Subtask( "subtask description", "subtask",NEW, epicId);
        subtask = new Subtask(
                2,
                "subtask description",
                NEW,
                "subtask",
                LocalDateTime.of(2024,01,01,01,02),
                epicId
        );

        taskManager.addSubtask(subtask);
        //Subtask subtask1 = new Subtask( "subtask description1", "subtask1", DONE, epicId);
        Subtask subtask1 = new Subtask(
                3,
                "subtask description",
                NEW,
                "subtask",
                LocalDateTime.of(2024,01,01,01,03),
                epicId
        );

        taskManager.addSubtask(subtask1);

        task = new Task(
                "Test addNewTask",
                NEW,
                "Test addNewTask description",
                LocalDateTime.now().plusNanos(1)
        );

        taskManager.addTask(task);
        taskManager.save();

        taskManager.cleanEpic();
        taskManager.cleanSubtask();
        taskManager.cleanTask();


        assertEquals(taskManager.listEpic().size(),0, "Возвращает не пустой список Эпиков");
        assertEquals(taskManager.listSubtask().size(),0, "Возвращает не пустой список подзадач");
        assertEquals(taskManager.listTask().size(),0, "Возвращает не пустой список задач");
        assertEquals(historyManager.getHistory().size(),0, "Возвращает не пустой список задач");

        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);

        assertNotNull(taskManager.listEpic(), "Возвращает пустой список Эпиков");
        assertNotNull(taskManager.listSubtask(), "Возвращает пустой список подзадач");
        assertNotNull(taskManager.listTask(), "Возвращает пустой список задач");
        assertNotNull(historyManager.getHistory(), "Возвращает пустой список истории");
    }
}