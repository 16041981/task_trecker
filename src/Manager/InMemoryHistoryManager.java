package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager{
    public static List<Task> history = new ArrayList<>();


    public static String getHistory(){
        if (history.size()>10) {
            history.remove(0);
        }
        return history.toString();
    }
}
