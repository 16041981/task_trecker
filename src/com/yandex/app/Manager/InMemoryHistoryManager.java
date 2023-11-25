package com.yandex.app.Manager;
import com.yandex.app.Model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    private static class Node {

        Task task;
        public Node next;
        public Node prev;

        private Node(Task task, Node next, Node prev) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }

    private final static Map<Integer, Node> nodeMap = new HashMap<>();
    private static Node first;
    private static Node last;

    //private int MAX_SIZE_LIST = 10;

    private ArrayList<Task> getTasks(){
        Node n = first;
        ArrayList<Task>tasks = new ArrayList<>();
        while (n != null){
            tasks.add(n.task);
            n = n.next;
        }
        return tasks;
    }

    public void linkLast(Task task){
        final Node l = last;
        final Node newNode = new Node(task, null, l);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        removeNode(task.getId());
        nodeMap.put(task.getId(), newNode);
    }

    private void removeNode(int id){
        nodeMap.remove(id);
        Node node = nodeMap.remove(id);
        if (node == null){
            return;
        }else if (node == first){
            first = first.next;
            first.prev = null;
        }else if (node == last){
            last = last.prev;
            last.next = null;
        }else{
            node.prev = node.prev.prev;
            node.next = node.next.next;
        }
    }

    @Override
    public void addHistory(Task task){
        linkLast(task);
    }

    @Override
    public void remove(int id){
        //getHistory.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
