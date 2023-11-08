import Manager.Manager;
import Tasks.Status;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Manager.TaskManager;
import Manager.HistoryManager;

public class Main {
    public static void main(String[] args){
        TaskManager manager = Manager.getDefault();
        HistoryManager historyManager = Manager.getDefaultHistory();
        manager.addEpic(new Epic("Собратся в поездку","Путешествие"));
        manager.addEpic(new Epic("Котовьи дела","Кот"));
        manager.addSubtask(new Subtask("Собрать вещи", "Вещи", Status.NEW, 1));
        manager.addSubtask(new Subtask("Купить продукты", "Продукты", Status.NEW, 1));
        manager.addSubtask(new Subtask("Помыть кота", "Кот", Status.DONE, 2));
        manager.addSubtask(new Subtask("Посушить кота", "Кот", Status.NEW, 3));
        manager.addTask(new Task("Продукты", "Новая", Status.NEW));
        manager.updateTask(new Task(6,"Продукты", "В процессе", Status.IN_PROGRESS));
        System.out.println(manager.printTask(6));
        System.out.println(manager.listSubtask());
        manager.listSubtaskForEpik(1);
        //manager.removeEpic(1);
        //manager.removeSubtask(5);
        //manager.cleanTask();
        manager.printTask(6);
        manager.printSubtask(3);
        manager.printSubtask(4);
        System.out.println(historyManager.getHistory());
        System.out.println("history");
        manager.printSubtask(5);
        manager.printEpic(2);
        manager.printSubtask(3);
        manager.printSubtask(4);
        manager.printSubtask(5);
        manager.printEpic(2);
        manager.printSubtask(3);
        manager.printSubtask(4);
        manager.printSubtask(5);
        manager.printEpic(2);
        manager.printSubtask(3);
        manager.printSubtask(4);
        manager.printSubtask(5);
        manager.printEpic(2);
        System.out.println("history");
        System.out.println(historyManager.getHistory());

    }
}