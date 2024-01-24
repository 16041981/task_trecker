package com.yandex.app.Test;
import java.util.List;

import com.yandex.app.Manager.HistoryManager;
import com.yandex.app.Manager.InMemoryHistoryManager;
import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Status;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    HistoryManager historyManager = new InMemoryHistoryManager();;


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

    @Test
    void remove() {
        task = new Task(1, "","", Status.NEW);
        historyManager.addHistory(task);
        final List<Task> history = historyManager.getHistory();
        historyManager.remove(1);
        assertNotNull(historyManager.getHistory(), "История не пустая");

        historyManager.addHistory(task);
        Task task1 = new Task(2, "","", Status.NEW);
        historyManager.addHistory(task1);
        Task task2 = new Task(3, "","", Status.NEW);
        historyManager.addHistory(task2);
        assertEquals(3, historyManager.getHistory().size(), "Запись не добавилась.");
        historyManager.remove(2);
        final List<Task> history2 = historyManager.getHistory();
        assertEquals(2, historyManager.getHistory().size(), "Неверное количество записей в истории.");
        int taskRemoveId = 0;
        for (int i = 0; i < history2.size(); i++) {
            Task task3 = history2.get(i);
            if (task3.getId() == 2){
                taskRemoveId = task3.getId();
            }
        }
        assertEquals(taskRemoveId, 0,"Удалена не та запись в истории.");

        Task task3 = new Task(2, "","", Status.NEW);
        historyManager.addHistory(task3);
        historyManager.remove(1);
        final List<Task> history3 = historyManager.getHistory();
        int taskRemoveId2 = 0;
        for (int i = 0; i < history3.size(); i++) {
            Task task4 = history3.get(i);
            if (task4.getId() == 1){
                taskRemoveId2 = task4.getId();
            }
        }
          assertEquals(taskRemoveId2, 0,"Удалена не та запись в истории.");

        Task task4 = new Task(2, "","", Status.NEW);
        historyManager.addHistory(task3);
        historyManager.remove(1);
        final List<Task> history4 = historyManager.getHistory();
        int taskRemoveId3 = 0;
        for (int i = 0; i < history3.size(); i++) {
            Task task5 = history3.get(i);
            if (task4.getId() == 1){
                taskRemoveId3 = task5.getId();
            }
        }
        assertEquals(taskRemoveId3, 0,"Удалена не та запись в истории.");
    }
}