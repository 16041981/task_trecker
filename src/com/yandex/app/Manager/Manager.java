package com.yandex.app.Manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.app.Server.HttpTaskManager;
import com.yandex.app.adapter.LocalDateTimeAdapter;

import java.io.File;
import java.time.LocalDateTime;

public final class Manager{

    private Manager() {
    }
//  public static TaskManager getDefault() {
//        return new FileBackedTaskManager(new File("resources/task.csv"));
//    }

    public static TaskManager getDefault() {
        return new HttpTaskManager(8080);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
