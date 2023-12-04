package manager;
/*
    Основной класс, в котором прописана вся логика работы с Тасками, эпиками и сабтасками
*/
import model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TaskManager {

    private static final String STATUS_NEW = "NEW";
    private static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    private static final String STATUS_DONE = "DONE";

    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();

    private int nextId = 1;

    private void updateStatus(int epicId) {

        HashSet<String> statuses = new HashSet<>();

        for (Integer subTaskId : epicMap.get(epicId).getSubTaskIds()) {
            Subtask subtask = subtaskMap.get(subTaskId);
            statuses.add(subtask.getStatus());
        }

        Epic epic = epicMap.get(epicId);

        if (statuses.size() == 1 && statuses.contains(STATUS_NEW)) {
            epic.setStatus(STATUS_NEW);
            epicMap.put(epicId, epic);
        } else if (statuses.size() == 1 && statuses.contains(STATUS_DONE)) {
            epic.setStatus(STATUS_DONE);
            epicMap.put(epicId, epic);
        } else {
            epic.setStatus(STATUS_IN_PROGRESS);
            epicMap.put(epicId, epic);
        }
    }

    public ArrayList<Integer> updateSubtasksInEpic(Epic epic) {
        ArrayList<Integer> lisForSubtaskIds = new ArrayList<>();
        for (Integer subtaskId : epic.getSubTaskIds()) {
            Subtask subtask = subtaskMap.get(subtaskId);
            lisForSubtaskIds.add(subtask.getId());
        }
        epic.setSubTaskIds(lisForSubtaskIds);

        return epic.getSubTaskIds();
    }

    public ArrayList<Task> getAllTask() {
        ArrayList<Task> list = new ArrayList<>();
        for (Integer task : taskMap.keySet()) {
            list.add(taskMap.get(task));
        }

        return list;
    }

    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> list = new ArrayList<>();
        for (Integer epic : epicMap.keySet()) {
            list.add(epicMap.get(epic));
        }

        return list;
    }

    public ArrayList<Subtask> getAllSubtask() {
        ArrayList<Subtask> list = new ArrayList<>();
        for (Integer subtask : subtaskMap.keySet()) {
            list.add(subtaskMap.get(subtask));
        }

        return list;
    }

    public void clearAllTask() {
        taskMap.clear();
    }

    public void clearAllEpic() {
        epicMap.clear();
    }

    public void clearAllSubtask() {
        subtaskMap.clear();
    }

    public Task getEntityTask(int id){
        return taskMap.get(id);
    }

    public Epic getEntityEpic(int id){
        return epicMap.get(id);
    }

    public Subtask getEntitySubtask(int id){
        return subtaskMap.get(id);
    }

    public int create(Task task) {
        task.setId(nextId);
        nextId++;
        taskMap.put(task.getId(), task);

        return task.getId();
    }

    public int create(Epic epic) {
        epic.setId(nextId);
        nextId++;
        ArrayList<Integer> list = updateSubtasksInEpic(epic);
        epic.setSubTaskIds(list);
        epicMap.put(epic.getId(), epic);

        return epic.getId();
    }

    public int create(Subtask subtask) {
        subtask.setId(nextId);
        nextId++;
        subtaskMap.put(subtask.getId(), subtask);
        ArrayList<Integer> list = updateSubtasksInEpic(epicMap.get(subtask.getEpicId()));
        Epic epic = epicMap.get(subtask.getEpicId());
        list.add(subtask.getId());
        epic.setSubTaskIds(list);
        epicMap.put(subtask.getEpicId(), epic);

        return subtask.getId();
    }

    public void update(Task task) {
        taskMap.put(task.getId(), task);

    }

    public void update(Epic epic) {
        ArrayList<Integer> list = epicMap.get(epic.getId()).getSubTaskIds();
        epic.setSubTaskIds(list);
        epicMap.put(epic.getId(), epic);
        updateStatus(epic.getId());
    }

    public void update(Subtask subtask) {
        subtaskMap.put(subtask.getId(), subtask);
        Epic epic = epicMap.get(subtask.getEpicId());
        ArrayList<Integer> list = epicMap.get(epic.getId()).getSubTaskIds();
        epic.setSubTaskIds(list);
        updateStatus(subtask.getEpicId());
    }

    public void deleteTask(int id) {
        taskMap.remove(id);
    }

    public void deleteEpic(int id) {
        epicMap.remove(id);
    }

    public void deleteSubtask(int id) {
        subtaskMap.remove(id);
    }

    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> list = new ArrayList<>();
        for (Integer subTaskId : epicMap.get(epicId).getSubTaskIds()) {
            list.add(subtaskMap.get(subTaskId));
        }

        return list;
    }
}
