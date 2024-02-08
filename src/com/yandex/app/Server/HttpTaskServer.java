package com.yandex.app.Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.yandex.app.Manager.Manager;
import com.yandex.app.Manager.TaskManager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException{
        this(Manager.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Manager.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::handler);
    }

    private void handler(HttpExchange h) {
        try (h) {
            System.out.println("\n/tasks: "+h.getRequestURI());
            final String path = h.getRequestURI().getPath().substring(7);
            switch (path){
                case "" -> {
                    if (!h.getRequestMethod().equals("GET")){
                        System.out.println("/ ожидался GET запрос, получен "+ h.getRequestMethod());
                        h.sendResponseHeaders(405,0);
                    }
                    final String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(h, response);
                }
                case "history" -> {
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/history ожидался GET запрос, получен "+ h.getRequestMethod());
                        h.sendResponseHeaders(405,0);
                    }
                    final String response = gson.toJson(taskManager.getHistory());
                    sendText(h, response);
                }
                case "task" -> handleTask(h);
                case "subtask" -> handleSubtask(h);
                case "subtask/epic" -> {
                    if (!h.getRequestMethod().equals("GET")){
                        System.out.println("/subtask/epic ожидался GET запрос, получен "+ h.getRequestMethod());
                        h.sendResponseHeaders(405,0);
                    }
                    final String query = h.getRequestURI().getQuery();
                    String idParam = query.substring(3);
                    final int id = Integer.parseInt(idParam);
                    final List<Subtask> subtasks = taskManager.listSubtaskForEpic(id);
                    final String response = gson.toJson(subtasks);
                    System.out.println("Получены подзадачи " + id + " эпика.");
                    sendText(h, response);
                }
                case "epic" -> handleEpic(h);
                default -> {
                    System.out.println("Запрос " + h.getRequestURI() + " неизвестен.");
                    h.sendResponseHeaders(404,0);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleTask(HttpExchange h) throws IOException{
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()){
            case "GET" -> {
                if (query == null){
                    final List<Task> tasks = taskManager.listTask();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получен список задач");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                final Task task = taskManager.getTask(id);
                final String response = gson.toJson(task);
                System.out.println("Получена задача id " + id);
                sendText(h, response);
            }
            case "DELETE" -> {
                if (query == null){
                    taskManager.cleanTask();
                    System.out.println("Очищен список задач");
                    h.sendResponseHeaders(200,0);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                taskManager.removeTask(id);
                System.out.println("Удалена задача id " + id);
                h.sendResponseHeaders(200,0);
            }
            case "POST" -> {
                String json = readText(h);
                if (json.isEmpty()){
                    h.sendResponseHeaders(400,0);
                    return;
                }
                final Task task = gson.fromJson(json, Task.class);
                final Integer id = task.getId();
                if(id != null){
                    taskManager.updateTask(task);
                    System.out.println("Обновлена задача id " + id);
                    h.sendResponseHeaders(200,0);
                }else {
                    taskManager.addTask(task);
                    System.out.println("Создана задача id " + id);
                    h.sendResponseHeaders(200,0);
                    final String response = gson.toJson(task);
                    sendText(h, response);
                }
            }
        }
    }

    private void handleEpic(HttpExchange h) throws IOException{
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()){
            case "GET" -> {
                if (query == null){
                    final List<Epic> tasks = taskManager.listEpic();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получен список эпиков'");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                final Epic epic = taskManager.getEpic(id);
                final String response = gson.toJson(epic);
                System.out.println("Получен эпик id " + id);
                sendText(h, response);
            }
            case "DELETE" -> {
                if (query == null){
                    taskManager.cleanEpic();
                    System.out.println("Очищен список эпиков'");
                    h.sendResponseHeaders(200,0);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                taskManager.removeEpic(id);
                System.out.println("Удален эпик id " + id);
                h.sendResponseHeaders(200,0);
            }
            case "POST" -> {
                String json = readText(h);
                if (json.isEmpty()){
                    h.sendResponseHeaders(400,0);
                    return;
                }
                final Epic epic = gson.fromJson(json, Epic.class);
                final Integer id = epic.getId();
                if(id != null){
                    taskManager.updateTask(epic);
                    System.out.println("Обновлена задача id " + id);
                    h.sendResponseHeaders(200,0);
                }else {
                    taskManager.addTask(epic);
                    System.out.println("Создана задача id " + id);
                    h.sendResponseHeaders(200,0);
                    final String response = gson.toJson(epic);
                    sendText(h, response);
                }
            }
        }
    }

    private void handleSubtask(HttpExchange h) throws IOException{
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()){
            case "GET" -> {
                if (query == null){
                    final List<Subtask> tasks = taskManager.listSubtask();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получен список подзадач");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                final Subtask subtask = taskManager.getSubtask(id);
                final String response = gson.toJson(subtask);
                System.out.println("Получена подзадача id " + id);
                sendText(h, response);
            }
            case "DELETE" -> {
                if (query == null){
                    taskManager.cleanSubtask();
                    System.out.println("Очищен список подзадач");
                    h.sendResponseHeaders(200,0);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                taskManager.removeSubtask(id);
                System.out.println("Удалена подзадача id " + id);
                h.sendResponseHeaders(200,0);
            }
            case "POST" -> {
                String json = readText(h);
                if (json.isEmpty()){
                    h.sendResponseHeaders(400,0);
                    return;
                }
                final Subtask subtask = gson.fromJson(json, Subtask.class);
                final Integer id = subtask.getId();
                if(id != null){
                    taskManager.updateTask(subtask);
                    System.out.println("Обновлена подзадача id " + id);
                    h.sendResponseHeaders(200,0);
                }else {
                    taskManager.addTask(subtask);
                    System.out.println("Создана подзадача id " + id);
                    h.sendResponseHeaders(200,0);
                    final String response = gson.toJson(subtask);
                    sendText(h, response);
                }
            }
        }
    }


    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public void start(){
        server.start();
    }

    public void stop(){
        server.stop(0);
    }

}

