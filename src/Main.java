import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        TaskManager taskManager = new TaskManager();
        taskManager.addEpic(new Epic("Собратся в поездку","Путешествие"));
        taskManager.addEpic(new Epic("Котовьи дела","Кот"));
        taskManager.addSubtask(new Subtask("Собрать вещи", "Вещи", "NEW", 1));
        taskManager.addSubtask(new Subtask("Купить продукты", "Продукты", "NEW", 1));
        taskManager.addSubtask(new Subtask("Помыть кота", "Кот", "DONE", 2));
        taskManager.addSubtask(new Subtask("Посушить кота", "Кот", "NEW", 3));
        taskManager.addTask(new Task("Купить продукты на неделю", "Покупка продуктов", "NEW"));
        taskManager.updateTask(new Task(6,"Купить продукты", "Покупка продуктов", "IN_PROGRESS"));
        System.out.println(taskManager.printTask(6));
        taskManager.listSubtask();
        taskManager.listSubtaskForEpik(1);
        taskManager.removeSubtask(3);
        taskManager.cleanTask();
    }
}