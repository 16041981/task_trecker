import Manager.InMemoryHistoryManager;
import Tasks.Status;
import Manager.InMemoryTaskManager;
import Tasks.Epic;

import Tasks.Subtask;
import Tasks.Task;



public class Main {
    public static void main(String[] args){
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        InMemoryTaskManager inMemoryTaskManager= new InMemoryTaskManager();
        inMemoryTaskManager.addEpic(new Epic("Собратся в поездку","Путешествие"));
        inMemoryTaskManager.addEpic(new Epic("Котовьи дела","Кот"));
        inMemoryTaskManager.addSubtask(new Subtask("Собрать вещи", "Вещи", Status.NEW, 1));
        inMemoryTaskManager.addSubtask(new Subtask("Купить продукты", "Продукты", Status.NEW, 1));
        inMemoryTaskManager.addSubtask(new Subtask("Помыть кота", "Кот", Status.DONE, 2));
        inMemoryTaskManager.addSubtask(new Subtask("Посушить кота", "Кот", Status.NEW, 3));
        inMemoryTaskManager.addTask(new Task("Купить продукты на неделю", "Покупка продуктов", Status.NEW));
        inMemoryTaskManager.updateTask(new Task(6,"Купить продукты", "Покупка продуктов", Status.IN_PROGRESS));
        //System.out.println(inMemoryTaskManager.printTask(6));
        //System.out.println(inMemoryTaskManager.listSubtask());
        //inMemoryTaskManager.listSubtaskForEpik(1);
        //inMemoryTaskManager.removeEpic(1);
        //inMemoryTaskManager.removeSubtask(5);
        //inMemoryTaskManager.cleanTask();
        inMemoryTaskManager.printTask(6);
        inMemoryTaskManager.printSubtask(3);
        inMemoryTaskManager.printSubtask(4);
        System.out.println("history");
        System.out.println(InMemoryHistoryManager.getHistory());
        inMemoryTaskManager.printSubtask(5);
        inMemoryTaskManager.printEpic(2);
        inMemoryTaskManager.printSubtask(3);
        inMemoryTaskManager.printSubtask(4);
        inMemoryTaskManager.printSubtask(5);
        inMemoryTaskManager.printEpic(2);
        inMemoryTaskManager.printSubtask(3);
        inMemoryTaskManager.printSubtask(4);
        inMemoryTaskManager.printSubtask(5);
        inMemoryTaskManager.printEpic(2);
        inMemoryTaskManager.printSubtask(3);
        inMemoryTaskManager.printSubtask(4);
        inMemoryTaskManager.printSubtask(5);
        inMemoryTaskManager.printEpic(2);
        System.out.println("history");
        System.out.println(InMemoryHistoryManager.getHistory());
    }
}