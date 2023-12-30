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

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    private List<Task> getTasks(){
        Node n = first;
        List<Task>tasks = new LinkedList<>();
        while (n != null){
            tasks.add(n.task);
            n = n.next;
        }
        return tasks;
    }

    private void linkLast(Task task){
        final Node newNode = new Node(task, null, last);
        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
        removeNode(task.getId());
        nodeMap.put(task.getId(), newNode);
    }

    private void removeNode(int id){
        Node node = nodeMap.remove(id);
        if (node == null){
        }else if (node == first){
            first = first.next;
            first.prev = null;
        }else if (node == last){
            last = last.prev;
        }else{
            node = node.prev;
            node.next = node.next.next;
        }
    }

    @Override
    public void addHistory(Task task){
        if (task == null){
        }
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
