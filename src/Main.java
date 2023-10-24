public class Main {
    public static void main(String[] args){
        TaskManager taskManager = new TaskManager();
        taskManager.addEpic(new Epic("111","151"));
        taskManager.addSubtask(new Subtask("Собрать вещи", "Вещи", "NEW", 5));
        taskManager.addSubtask(new Subtask("Убрать квартиру", "Уборка", "NEW", 5));
        taskManager.addSubtask(new Subtask("Помыть кота", "Кот", "NEW", 2));
        taskManager.addTask(new Task("Купить продукты на неделю", "Покупка продуктов", "NEW"));
        taskManager.updateTask(new Task(4,"Купить продукты на месяц", "Покупка продуктов", "NEW"));
        System.out.println(new Subtask("Собрать вещи", "Вещи", "NEW", 5));


    }
}