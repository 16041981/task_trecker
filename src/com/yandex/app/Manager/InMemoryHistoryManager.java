package com.yandex.app.Manager;
import com.yandex.app.Model.Task;
import com.yandex.app.Service.Node;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager{

    private static LinkedList<Task> getHistory = new LinkedList<>();

    private static Map<Integer, Node> HashTable = new HashMap<>();

    private int MAX_SIZE_LIST = 10;

    @Override
    public List<Task> getHistory() {
        return List.copyOf(getHistory);
    }


    @Override
    public void addHistory(Task task){
        getHistory.add(task);
        if (getHistory.size()>MAX_SIZE_LIST) {
            getHistory.removeFirst();
        }
    }
    @Override
    public void remove(int id){

    }

    public void CustomLinkedList(Task task){
        int size = 0;
        Node node = new Node(task);
         Node head = node;
         Node tail = node;
         node.next = new Node(task);
         node.prev = tail;


    }

    public void removeNode(Node node){
        HashTable.remove(node);
    }
}
