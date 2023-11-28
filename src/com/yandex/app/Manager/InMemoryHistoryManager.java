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

    public static Map<Integer, Node> nodeMap = new HashMap<>();
    private static Node first;
    private static Node last;

    private ArrayList<Task> getTasks(){
        Node n = first;
        ArrayList<Task>tasks = new ArrayList<>();
        while (n != null){
            tasks.add(n.task);
            n = n.next;
        }
        return tasks;
    }

    private void linkLast(Task task){
        final Node node = last;
        final Node newNode = new Node(task, null, node);
        last = newNode;
        if (node == null) {
            first = newNode;
        } else {
            node.next = newNode;
        }
        removeNode(task.getId());
        nodeMap.put(task.getId(), newNode);
    }

    private void removeNode(int id){
        Node node = nodeMap.get(id);
        nodeMap.remove(id);
        if (node == null){
        }else if (node == first){
            first = first.next;
            first.prev = null;
        }else if (node == last){
            last = last.prev;
            last.next = null;
        }else{
            node = node.prev;
            node.next = node.next.next;
        }
    }

    @Override
    public void addHistory(Task task){
        linkLast(task);
    }

    @Override
    public void remove(int id){
        removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
