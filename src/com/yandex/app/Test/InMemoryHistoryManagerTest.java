package com.yandex.app.Test;
import java.util.List;

import com.yandex.app.Manager.HistoryManager;
import com.yandex.app.Manager.InMemoryHistoryManager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Status;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    HistoryManager historyManager;

    @BeforeEach
    void setUp(){
        historyManager = new InMemoryHistoryManager();
        task = new Task(1, "","", Status.NEW);
        epic = new Epic(2,"","",Status.NEW);

    }

    @Test
    void addHistory() {
        historyManager.addHistory(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void remove() {
    }
}