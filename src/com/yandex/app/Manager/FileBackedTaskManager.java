package com.yandex.app.Manager;

import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager{

    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file){
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
    }

    protected void save(){
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))){
            //тут сохранить все таски и историю
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int addTask(Task task) {
        int task1 = super.addTask(task);
        save();
        return task1;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        return super.addSubtask(subtask);
    }

    @Override
    public int addEpic(Epic epic) {
        return super.addEpic(epic);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public void cleanTask() {
        super.cleanTask();
    }

    @Override
    public void cleanSubtask(Subtask subtask) {
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
