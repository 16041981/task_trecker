package com.yandex.app.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.Manager.InMemoryTaskManager;
import com.yandex.app.Manager.Manager;
import com.yandex.app.Manager.TaskManager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import com.yandex.app.Server.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.yandex.app.Model.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    protected TaskManager taskManager;
    protected HttpTaskServer taskServer;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    Gson gson = Manager.getGson();

    @BeforeEach
    void init() throws  IOException{
        taskManager = new InMemoryTaskManager();
        taskServer = new  HttpTaskServer(taskManager);
        task = new Task("task", NEW, "description task", LocalDateTime.now());
        taskManager.addTask(task);
        taskServer.start();
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
        assertEquals(200, response.statusCode());

        final List<Task> tasks = gson.fromJson(response.body() , new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertNotNull(tasks, "Где задачи?");

    }



}