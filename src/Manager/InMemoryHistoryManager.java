package Manager;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    public static List<Task> getHistory = new ArrayList<>();

    @Override
    public List<Task> getHistory () {
        return getHistory;
    }

    @Override
    public void addHistory(Task task){
        getHistory.add(task);
        if (getHistory.size()>10) {
            getHistory.remove(0);
        }
    }
}
