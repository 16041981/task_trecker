package com.yandex.app.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.Exception.ManagerSaveException;
import com.yandex.app.Manager.FileBackedTaskManager;
import com.yandex.app.Manager.Manager;
import com.yandex.app.Manager.TaskManager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import com.yandex.app.Server.HttpTaskManager;
import com.yandex.app.Server.HttpTaskServer;
import com.yandex.app.Server.KVServer;
import com.yandex.app.Server.KVTaskClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.yandex.app.Model.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager>{
    protected TaskManager taskManager;
    protected HttpTaskServer taskServer;
    protected KVTaskClient kvTaskClient;
    protected KVServer kvServer;

    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    Gson gson = Manager.getGson();

    @BeforeEach
    void init() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        kvTaskClient = new KVTaskClient(8078);
        taskManager = new HttpTaskManager(8078);
        taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        taskManager.addTask(task);
    }

    @AfterEach
    void stop() {
        taskServer.stop();
    }

    @Test
    public void TestEqualsLoadAndTaskManager(){
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

        HttpTaskManager taskManager1 = new HttpTaskManager(8078, true);

        assertEquals(taskManager1.listEpic(), taskManager.listEpic(),
                "Список эпиков после выгрузки не совпададает");
        assertEquals(taskManager1.listSubtask(), taskManager.listSubtask(),
                "Список подзадач после выгрузки не совпададает");
        assertEquals(taskManager1.listTask(), taskManager.listTask(),
                "Список задач после выгрузки не совпададает");
        assertEquals(taskManager1.getPrioritizedTasks(), taskManager.getPrioritizedTasks(),
                "Список prioritizedTasks после выгрузки не совпададает");
        assertEquals(taskManager1.getHistory(), taskManager.getHistory(),
                "Список истории после выгрузки не совпададает");
    }
}