package com.yandex.app.Manager;

import java.io.File;

public final class Manager{

    private Manager() {
    }

    public static TaskManager getDefault() {
        //return new InMemoryTaskManager();
        return new FileBackedTaskManager(new File("resources/task.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
