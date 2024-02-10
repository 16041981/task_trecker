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
        taskManager = new InMemoryTaskManager();
        taskServer = new  HttpTaskServer(taskManager);
        taskServer.start();
    }

    @AfterEach
    void stop(){
        taskServer.stop();
    }


    @Test
    void postTask() throws IOException, InterruptedException {
        task = new Task(
                "Test addNewTask",
                NEW,
                "Test addNewTask description",
                LocalDateTime.now().plusNanos(1));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
        assertEquals(200, response.statusCode());

        assertNotNull(taskManager.getTask(task.getId()), "Список задач пуст");
        taskManager.cleanTask();
    }

    @Test
    void postSubtask() throws IOException, InterruptedException {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        taskManager.addEpic(epic);
        final int epicId = epic.getId();
        subtask = new Subtask(
                3,
                "subtask description",
                NEW,
                "subtask",
                LocalDateTime.of(2024,01,01,01,03),
                epicId
        );
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        Gson gson = new Gson();
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
        assertEquals(200, response.statusCode());

        assertNotNull(taskManager.getSubtask(subtask.getId()), "Список подзадач пуст");
        taskManager.cleanEpic();
        taskManager.cleanSubtask();
    }

    @Test
    void postEpic() throws IOException, InterruptedException {
        epic = new Epic("epic description","epic");
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Gson gson = new Gson();
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
        assertEquals(200, response.statusCode());

        assertNotNull(taskManager.getEpic(epic.getId()), "Список эпиков пуст");
        taskManager.cleanEpic();
    }

    @Test
    void postHistory() throws IOException, InterruptedException {
        epic = new Epic("epic description","epic");
        epic.setStartTime(LocalDateTime.now().minusNanos(1));
        epic.setEndTime(LocalDateTime.now().plusNanos(200));
        taskManager.addEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        Gson gson = new Gson();
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
        assertEquals(200, response.statusCode());


        assertNotNull(taskManager.getHistory(), "Список истории пуст");
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
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
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
        assertEquals(200, response.statusCode());
        taskManager.cleanTask();
        taskManager.cleanSubtask();
        taskManager.cleanEpic();
    }

    @Test
    void getTaskId() throws IOException, InterruptedException {
        task = new Task(
                "Test addNewTask",
                NEW,
                "Test addNewTask description",
                LocalDateTime.now().plusNanos(1));
        taskManager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        taskManager.cleanTask();
    }

    @Test
    void deleteTaskId() throws IOException, InterruptedException {
        task = new Task(
                "Test addNewTask",
                NEW,
                "Test addNewTask description",
                LocalDateTime.now().plusNanos(1));
        taskManager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertNull(taskManager.getTask(1), "Задача не удалена");
    }
}