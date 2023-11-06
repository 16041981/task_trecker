package Manager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.util.HashMap;
import java.util.List;

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

    String printTask(int id);

    String printSubtask(int id);

    String printEpic(int id);

    HashMap<Integer, Task> listTask();

    HashMap<Integer, Subtask> listSubtask();

    HashMap<Integer, Epic> listEpic();

    void listSubtaskForEpik(int idEpic);

    void updateStatusEpic(Subtask subtask);

}