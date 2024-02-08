package com.yandex.app.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.Exception.ManagerSaveException;
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

class HttpTaskManagerTest {
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
        task = new Task("task", NEW, "description task", LocalDateTime.now());
        taskManager.addTask(task);

    }

    @AfterEach
    void stop() {
        taskServer.stop();
    }

}