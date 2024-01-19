package manager;
import model.Node;
import model.Task;

import java.util.*;
/*
    Класс для работы с историей просмотра
*/
public class InMemoryHistoryManager implements HistoryManager{

    private final Map<Integer, Node<Task>> nodeMap;
    private Node<Task> head;
    private Node<Task> tail;

    public InMemoryHistoryManager() {
        this.nodeMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public Collection<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(nodeMap.get(id));
    }

    public void linkLast(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            removeNode(nodeMap.get(task.getId()));
        }

        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(task, null, tail);
        tail = newNode;
        nodeMap.put(task.getId(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
    }

    private List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            taskList.add(node.getData());
            node = node.getNext();
        }
        return taskList;
    }

    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }

        final Node<Task> next = node.getNext();
        final Node<Task> prev = node.getPrev();
        node.setData(null);

        if (head == node && tail == node) {
            head = null;
            tail = null;
        } else if (head == node) {
            head = next;
            head.setPrev(null);
        } else if (tail == node) {
            tail = prev;
            tail.setNext(null);
        } else {
            prev.setNext(next);
            next.setPrev(prev);
        }
    }
}
