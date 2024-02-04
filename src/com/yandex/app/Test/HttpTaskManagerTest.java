package com.yandex.app.Test;

import com.google.gson.Gson;
import com.yandex.app.Manager.Manager;
import com.yandex.app.Manager.TaskManager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import com.yandex.app.Server.HttpTaskServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.yandex.app.Model.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    protected TaskManager taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    Gson gson = Manager.getGson();


//    @Test
//    void getTasks() throws IOException, InterruptedException {
//        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
//        task = new Task("task", NEW, "description task",  LocalDateTime.now());
//        //taskServer.getTaskManager().addNewTask(task);
//        taskManager.addTask(task);
//        taskServer.start();
//
//
//        HttpClient client = HttpClient.newHttpClient();
//        URI url = URI.create("http://localhost:8080/tasks/task");
//        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertEquals(200, response.statusCode());
//
}