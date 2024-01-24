package com.yandex.app.Manager;

import com.yandex.app.Model.Epic;
import com.yandex.app.Model.Subtask;
import com.yandex.app.Model.Task;
import com.yandex.app.Model.Status;
import com.yandex.app.Model.TaskTupe;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVTaskFormat {

    public static String toString(Task task) {
        StringBuilder result = new StringBuilder().append(task.getId()).append(",")
                .append(task.getType()).append(",").append(task.getName()).append(",")
                .append(task.getStatus()).append(",").append(task.getDescription()).append(",")
                .append(task.getStartTime()).append(task.getEndTime());
        if (task.getType() == TaskTupe.SUBTASK) {
            result.append(((Subtask)(task)).getIdEpic());
        }
        return result.toString();
    }

    public static Task taskFromString(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskTupe tupe = TaskTupe.valueOf(values[1]);
        final String name = values[2];
        final Status status = Status.valueOf(values[3]);
        final String description = values[4];
        final LocalDateTime startTime = LocalDateTime.parse(values[5]);
        final LocalDateTime endTime = LocalDateTime.parse(values [6]);
        if (tupe == TaskTupe.TASK) {
            return new Task(id, name, description, status);
        }
        if (tupe == TaskTupe.SUBTASK) {
            final int epicId = Integer.parseInt(values[7]);
            return new Subtask(id, name, description, status, epicId);
        }
        return new Epic(id, name, description, status, startTime, endTime);
    }

     static String historyToString(HistoryManager manager){
         final List<Task> history = manager.getHistory();
         if (history.isEmpty()){
             return "";
     }
        StringBuilder sb = new StringBuilder();
        sb.append(history.get(0).getId());
        for (int i = 1; i < history.size(); i++){
            Task task = history.get(i);
            sb.append(",");
            sb.append(task.getId());
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String valye){
        if (valye != null) {
            final String[] values = valye.split(",");
            final ArrayList<Integer> ids = new ArrayList<>(values.length);
            for (String id : values) {
                ids.add(Integer.parseInt(id));
            }
            return ids;
        }else {
            return Collections.emptyList();
        }
    }

    public static String getHeader(){
        return "id,type,name,status,description,startTime,endTime,epic";
    }
}

