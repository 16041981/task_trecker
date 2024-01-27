package com.yandex.app.Manager;

import com.yandex.app.Model.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    List<Task> getHistory();

    void addHistory(Task task);

    void remove(int id);
}
