package Manager;
import Tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private static LinkedList<Task> getHistory = new LinkedList<>();

    private int MAX_SIZE_LIST = 10;

    @Override
    public List<Task> getHistory() {
        return List.copyOf(getHistory);
    }

    @Override
    public void addHistory(Task task){
        getHistory.add(task);
        if (getHistory.size()>MAX_SIZE_LIST) {
            getHistory.removeFirst();
        }
    }
}
