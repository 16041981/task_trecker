package com.yandex.app.Test;

import com.yandex.app.Manager.FileBackedTaskManager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Status;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static com.yandex.app.Model.Status.DONE;
import static com.yandex.app.Model.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File file;

    @BeforeEach
    public void setUp(){
        file = new File("TaskHistury.csv");
        taskManager = new FileBackedTaskManager(file);

    }

    @AfterEach
    protected void tearDown(){assertTrue(file.delete());}

    @Test
    //@BeforeEach
    public void save(){
        epic = new Epic("epic description","epic");
        final int epicId = taskManager.addEpic(epic);
        subtask = new Subtask( "subtask description", "subtask", NEW, epicId);
        taskManager.addSubtask(subtask);
        Subtask subtask1 = new Subtask( "subtask description1", "subtask1", NEW, epicId);
        taskManager.addSubtask(subtask1);

        taskManager.save();
        final List<Task> tasks = taskManager.listTask();

        assertNull(tasks, "Возвращает пустой список задач");
        assertNotEquals(0, tasks.size(), "Возвращает пустой список задач");

    }

    @Test
    public void loadFromFile(){
        epic = new Epic("epic description","epic");
        final int epicId = taskManager.addEpic(epic);
        subtask = new Subtask( "subtask description", "subtask",NEW, epicId);
        taskManager.addSubtask(subtask);
        Subtask subtask1 = new Subtask( "subtask description1", "subtask1", DONE, epicId);
        taskManager.addSubtask(subtask1);
        taskManager.save();
        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);
        final List<Task> tasks = taskManager.listTask();
        assertNotNull(tasks, "Возвращает не пустой список задач");
        assertEquals(0, tasks.size(), "Возвращает не пустой список задач");
    }
}