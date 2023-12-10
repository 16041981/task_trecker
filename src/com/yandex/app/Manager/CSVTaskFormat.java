package com.yandex.app.Manager;

import com.yandex.app.Model.Task;
import com.yandex.app.Service.TaskTupe;
import java.util.List;

public class CSVTaskFormat {

    public static String toString (Task task){
        return task.getId() + ",";
    }
    public static Task fromString(String value){
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskTupe tupe = TaskTupe.valueOf(values[1]);


        return null; //заглушка

    }

    public static String tuString (HistoryManager historyManager){
        return null; //заглушка
    }

    public static List<Integer> historyFromString(String valye){
        return null; //заглушка
    }
}
