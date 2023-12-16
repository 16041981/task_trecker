package com.yandex.app.Manager;

import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import com.yandex.app.Service.Status;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager{
    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new File("TaskHistury.csv"));


        fileBackedTaskManager.addEpic(new Epic("Собратся в поездку","Путешествие"));
        fileBackedTaskManager.addEpic(new Epic("Котовьи дела","Кот"));
        fileBackedTaskManager.addSubtask(new Subtask("Собрать вещи", "Вещи", Status.NEW, 1));
        fileBackedTaskManager.addSubtask(new Subtask("Купить продукты", "Продукты", Status.NEW, 1));
        fileBackedTaskManager.addSubtask(new Subtask("Помыть кота", "Кот", Status.DONE, 2));
        fileBackedTaskManager.addSubtask(new Subtask("Посушить кота", "Кот", Status.NEW, 3));
        fileBackedTaskManager.addTask(new Task("Продукты", "Новая", Status.NEW));
        fileBackedTaskManager.updateTask(new Task(6,"Продукты", "В процессе", Status.IN_PROGRESS));


    }

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file){
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());
            int generatorId = 0;
            List<Integer> history = Collections.emptyList();
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if(line.isEmpty()){
                    history = CSVTaskFormat.historyFromString(lines[i + 1]);
                    break;
                }
                final Task task = CSVTaskFormat.taskFromString(line);
                final int id = task.getId();
                if(id > generatorId){
                    generatorId = id;
                }
                taskManager.addTask(task);
            }
            for (Map.Entry<Integer, Subtask> e : taskManager.subtask.entrySet()) {
                final Subtask subtask = e.getValue();
                final Epic epic = taskManager.epics.get(subtask.getIdEpic());
                epic.addSubtaskIds(subtask.getId());
            }
            for (Integer taskId : history){
                taskManager.historyManager.add(taskManager.findTask(taskId));
            }
            taskManager.generatorId = generatorId;
        }catch (IOException e){

        }

        // прочитать из файла  (с помощью CSVTaskFormat)
        // примечание: нам нужно установить счетчик для новых задач
        int generatorId = 0;
        // поскольку история хранилась просто в виде списка идентификаторов
        List<Integer> history = Collections.emptyList();
        //специальный метод восстановления задач (без истории и сохранения в файл)
        //taskManager.addTask(task);

        // восстановить историю, получив задачу и добавив ее в историю
        return taskManager;
    }

    protected void save(){
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))){

//            final Task task = entry.getValue();
////            CSVTaskFormat csvTaskFormat = new CSVTaskFormat();
////            bufferedWriter.write(csvTaskFormat.toString(task));
//            //тут сохранить все таски и историю
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
            bufferedWriter.write(CSVTaskFormat.getHeader());
            bufferedWriter.newLine();

            for (Map.Entry<Integer, Subtask> entry : tasks.entrySet()){
                final Task task = entry.getValue();
                bufferedWriter.write(CSVTaskFormat.tuString(task));
                bufferedWriter.newLine();
            }

            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()){
                final Task task = entry.getValue();
                bufferedWriter.write(CSVTaskFormat.tuString(task));
                bufferedWriter.newLine();
            }

            for (Map.Entry<Integer, Subtask> entry : epics.entrySet()){
                final Task task = entry.getValue();
                bufferedWriter.write(CSVTaskFormat.tuString(task));
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.write(CSVTaskFormat.tuString(historyManager));
            bufferedWriter.newLine();

        }catch (IOException e){
            throw new ManagerSaveException(file.getName());
        }
    }

    @Override
    public int addTask(Task task) {
        int task1 = super.addTask(task);
        save(task);
        return task1;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        save(subtask);
        return super.addSubtask(subtask);
    }

    @Override
    public int addEpic(Epic epic) {
        save(epic);
        return super.addEpic(epic);
    }

    @Override
    public void updateTask(Task task) {
        save(task);
        super.updateTask(task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        save(subtask);
        super.updateSubtask(subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        save(epic);
        super.updateEpic(epic);
    }

    @Override
    public void cleanTask() {
        super.cleanTask();
    }

    @Override
    public void cleanSubtask(Subtask subtask) {
        save(subtask);
        super.cleanSubtask(subtask);
    }

    @Override
    public void cleanEpic() {
        super.cleanEpic();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
    }

    @Override
    public void removeEpic(int idEpic) {
        super.removeEpic(idEpic);
    }

    @Override
    public Map<Integer, Task> printTask(int id) {

        return super.printTask(id);
    }

    @Override
    public Map<Integer, Subtask> printSubtask(int id) {
        return super.printSubtask(id);
    }

    @Override
    public Map<Integer, Epic> printEpic(int id) {
        return super.printEpic(id);
    }

    @Override
    public Map<Integer, Task> listTask() {
        return super.listTask();
    }

    @Override
    public Map<Integer, Subtask> listSubtask() {
        return super.listSubtask();
    }

    @Override
    public Map<Integer, Epic> listEpic() {
        return super.listEpic();
    }

    @Override
    public void listSubtaskForEpik(int idEpic) {
        super.listSubtaskForEpik(idEpic);
    }

    @Override
    public void updateStatusEpic(Subtask subtask) {
        super.updateStatusEpic(subtask);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//    }
}
