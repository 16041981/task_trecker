package Manager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    int addTask(Task task);


    int addSubtask(Subtask subtask);

    int addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void cleanTask();

    void cleanSubtask(Subtask subtask);

    void cleanEpic();

    void removeTask(int id);

    void removeSubtask(int id);

    void removeEpic(int idEpic);

    Map<Integer, Task> printTask(int id);

    Map<Integer, Subtask> printSubtask(int id);

    Map<Integer, Epic> printEpic(int id);

    Map<Integer, Task> listTask();

    Map<Integer, Subtask> listSubtask();

    Map<Integer, Epic> listEpic();

    void listSubtaskForEpik(int idEpic);

    void updateStatusEpic(Subtask subtask);

}