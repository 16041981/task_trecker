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

    HistoryManager historyManager = Manager.getDefaultHistory();



    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new File("TaskHistury.csv"));



        fileBackedTaskManager.addEpic(new Epic( "Собратся в поездку","Путешествие"));
        fileBackedTaskManager.addEpic(new Epic( "Котовьи дела","Кот"));
        fileBackedTaskManager.addTask(new Task("Продукты", "Новая", Status.NEW));
        fileBackedTaskManager.updateTask(new Task(3,"Продукты", "В процессе", Status.IN_PROGRESS));
        fileBackedTaskManager.addSubtask(new Subtask( "Собрать вещи", "Вещи", Status.NEW, 1));
        fileBackedTaskManager.addSubtask(new Subtask( "Купить продукты", "Продукты", Status.NEW, 1));
        fileBackedTaskManager.addSubtask(new Subtask( "Помыть кота", "Кот", Status.DONE, 2));
        fileBackedTaskManager.addSubtask(new Subtask( "Посушить кота", "Кот", Status.NEW, 2));

        FileBackedTaskManager fileBackedTaskManager1 =FileBackedTaskManager.loadFromFile(new File("TaskHistury.csv"));



    }

    private void getTask (int i){

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
            for (Map.Entry<Integer, Subtask> e : taskManager.listSubtask().entrySet()) {
                final Subtask subtask = e.getValue();
                final Epic epic = taskManager.listEpic().get(subtask.getIdEpic());
                epic.addSubtaskIds(subtask.getId());
            }
            for (Integer taskId : history){
                //taskManager.historyManager.add(taskManager.printTask(taskId));
                taskManager.historyManager.addHistory(taskManager.getTask(taskId));
            }
        }catch (IOException e){
            throw new ManagerSaveException("Невозможно прочитать файл: " + file.getName(), e);
        }

        return taskManager;
    }

    protected void save(){
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))){

            bufferedWriter.write(CSVTaskFormat.getHeader());
            bufferedWriter.newLine();

            for (Map.Entry<Integer, Task> entry : listTask().entrySet()){
                final Task task = entry.getValue();
                bufferedWriter.write(CSVTaskFormat.toString(task));
                bufferedWriter.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : listSubtask().entrySet()){
                final Task task = entry.getValue();
                bufferedWriter.write(CSVTaskFormat.toString(task));
                bufferedWriter.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : listEpic().entrySet()){
                final Task task = entry.getValue();
                bufferedWriter.write(CSVTaskFormat.toString(task));
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.write(CSVTaskFormat.historyToString(historyManager));

        }catch (IOException e){
            throw new ManagerSaveException("Ошибка сохранения файла: " + file.getName(), e);
        }
    }

    @Override
    public int addTask(Task task) {
        int addTask1 =  super.addTask(task);
        historyManager.addHistory(task);
        save();
        return addTask1;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int subtask1 = super.addSubtask(subtask);
        historyManager.addHistory(subtask);
        save();
        return subtask1;
    }

    @Override
    public int addEpic(Epic epic) {
        int epic1 = super.addEpic(epic);
        historyManager.addHistory(epic);
        save();
        return epic1;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        historyManager.addHistory(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        historyManager.addHistory(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        historyManager.addHistory(epic);
        save();
    }

    @Override
    public void cleanTask() {
        super.cleanTask();
    }

    @Override
    public void cleanSubtask(Subtask subtask) {
        super.cleanSubtask(subtask);
        historyManager.addHistory(subtask);
        save();
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
}

