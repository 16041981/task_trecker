package com.yandex.app.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.Exception.ManagerSaveException;
import com.yandex.app.Manager.*;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Status;
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
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import static com.yandex.app.Model.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HistoryManager historyManager = new InMemoryHistoryManager();
    protected TaskManager taskManager;
    protected HttpTaskServer taskServer;
    protected KVTaskClient kvTaskClient;
    protected KVServer kvServer;

    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    Gson gson = Manager.getGson();

    @BeforeEach
    void init() throws  IOException{
        kvServer = new KVServer();
        kvServer.start();
        kvTaskClient = new KVTaskClient(8078);
        taskManager = new HttpTaskManager(8078);
        taskServer = new  HttpTaskServer(taskManager);
        taskServer.start();
        task = new Task("task", NEW, "description task", LocalDateTime.now());
        taskManager.addTask(task);

    }

    @AfterEach
    void stop(){
        taskServer.stop();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
        assertEquals(200, response.statusCode());

        final List<Task> tasks = gson.fromJson(response.body() , new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertNotNull(tasks, "Где задачи?");
    }

    @Test
    void putSingleTaskStandardBehavior() throws ManagerSaveException{
        TaskManager manager = Manager.getDefault();

        manager.cleanTask();
        Task expectedTask = new Task("task", NEW, "description task", LocalDateTime.now());
        manager.addTask(expectedTask);
        Task actualTask = manager.getTask(expectedTask.getId());
        assertEquals(expectedTask, actualTask);
    }




    @Test
    void addHistory() {
        task = new Task(1, "","", Status.NEW);
        historyManager.addHistory(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(1, history.size(), "Неверное количество записей в истории.");

        historyManager.addHistory(task);
        historyManager.addHistory(task);
        final List<Task> history2 = historyManager.getHistory();
        assertEquals(1, history2.size(), "Запись в истории дублируется.");

        historyManager.addHistory(task);
        Task task1 = new Task(2, "","", Status.NEW);
        historyManager.addHistory(task1);
        final List<Task> history3 = historyManager.getHistory();
        assertEquals(2, history3.size(), "Запись не добавилась.");
    }

}