package manager;

import enums.Status;
import exception.ManagerValidationException;
import helpers.StartTimeComparator;
import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static enums.Status.*;
/*
    Класс для работы с Тасками, эпиками и сабтасками
*/
public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer, Task> taskMap;
    protected Map<Integer, Epic> epicMap;
    protected Map<Integer, Subtask> subtaskMap;

    final protected Set<Task> prioritizedTasks;

    protected final HistoryManager historyManager;

    private int nextId = 1;

    public InMemoryTaskManager() {
        this.taskMap = new HashMap<>();
        this.epicMap = new HashMap<>();
        this.subtaskMap = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        prioritizedTasks = new TreeSet<>(new StartTimeComparator());
    }

    public InMemoryTaskManager(HistoryManager defaultHistory) {
        this.taskMap = new HashMap<>();
        this.epicMap = new HashMap<>();
        this.subtaskMap = new HashMap<>();
        historyManager = defaultHistory;
        this.nextId = nextId;
        prioritizedTasks = new TreeSet<>(new StartTimeComparator());
    }

    private void updateStatus(int epicId) {

        Set<Status> statuses = new HashSet<>();

        for (Integer subTaskId : epicMap.get(epicId).getSubTaskIds()) {
            Subtask subtask = subtaskMap.get(subTaskId);
            statuses.add(subtask.getStatus());
        }

        Epic epic = epicMap.get(epicId);

        if (statuses.size() == 1 && statuses.contains(NEW)) {
            epic.setStatus(NEW);
            epicMap.put(epicId, epic);
        } else if (statuses.size() == 1 && statuses.contains(DONE)) {
            epic.setStatus(DONE);
            epicMap.put(epicId, epic);
        } else {
            epic.setStatus(IN_PROGRESS);
            epicMap.put(epicId, epic);
        }
    }

    public Collection<Integer> updateSubtasksInEpic(Epic epic) {
        Collection<Integer> lisForSubtaskIds = new ArrayList<>();
        for (Integer subtaskId : epic.getSubTaskIds()) {
            Subtask subtask = subtaskMap.get(subtaskId);
            lisForSubtaskIds.add(subtask.getId());
        }
        epic.setSubTaskIds(lisForSubtaskIds);

        return epic.getSubTaskIds();
    }

    public void updateTime(int epicId) {
        Epic epic = epicMap.get(epicId);

        if (epic.getSubTaskIds().isEmpty()) {
            epic.setStartTime(LocalDateTime.now());
            epic.setDuration(Duration.ZERO);
        }

        Optional<LocalDateTime> startTime = epic.getSubTaskIds()
                .stream()
                .map(subtaskMap::get)
                .filter(subtask -> subtask.getStatus() != Status.DONE)
                .map(Subtask::getStartTime)
                .min(LocalDateTime::compareTo);

        Optional<LocalDateTime> endTime = epic.getSubTaskIds()
                .stream()
                .map(subtaskMap::get)
                .filter(subtask -> subtask.getStatus() != Status.DONE)
                .map(Subtask::getEndTime)
                .max(LocalDateTime::compareTo);

        if (startTime.isEmpty() || endTime.isEmpty()) {
            return;
        }

        Duration duration = Duration.between(startTime.get(), endTime.get());

        epic.setStartTime(startTime.get());
        epic.setDuration(duration);
    }

    protected void validate() {
        final List<Task> prioritizedTask = getPrioritizedTasks()
                .stream()
                .filter(Task -> Task.getStartTime() != null)
                .collect(Collectors.toList());

        for (int i = 0; i < prioritizedTask.size() - 1; i++) {
            final Task currentTask = prioritizedTask.get(i);
            final Task nextTask = prioritizedTask.get(i + 1);

            if (!currentTask.getEndTime().isBefore(nextTask.getStartTime())) {
                throw new ManagerValidationException("Задачи пересекаются в выполнении");
            }
        }

    }

    @Override
    public Collection<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Collection<Task> getAllTask() {
        List<Task> list = new ArrayList<>();
        for (Integer task : taskMap.keySet()) {
            list.add(taskMap.get(task));
        }

        return list;
    }

    @Override
    public Collection<Epic> getAllEpic() {
        List<Epic> list = new ArrayList<>();
        for (Integer epic : epicMap.keySet()) {
            list.add(epicMap.get(epic));
        }

        return list;
    }

    @Override
    public Collection<Subtask> getAllSubtask() {
        List<Subtask> list = new ArrayList<>();
        for (Integer subtask : subtaskMap.keySet()) {
            list.add(subtaskMap.get(subtask));
        }

        return list;
    }

    @Override
    public void clearAllTask() {
        taskMap.clear();
    }

    @Override
    public void clearAllEpic() {
        epicMap.clear();
    }

    @Override
    public void clearAllSubtask() {
        subtaskMap.clear();
    }

    @Override
    public Task getEntityTask(int id){
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic getEntityEpic(int id){
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Subtask getEntitySubtask(int id){
        historyManager.add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }

    @Override
    public int create(Task task) {
        task.setId(nextId);
        nextId++;
        taskMap.put(task.getId(), task);
        prioritizedTasks.add(task);
        validate();

        return task.getId();
    }

    @Override
    public int create(Epic epic) {
        epic.setId(nextId);
        nextId++;
        Collection<Integer> list = updateSubtasksInEpic(epic);
        epic.setSubTaskIds(list);
        epicMap.put(epic.getId(), epic);

        return epic.getId();
    }

    @Override
    public int create(Subtask subtask) {
        subtask.setId(nextId);
        nextId++;
        subtaskMap.put(subtask.getId(), subtask);
        Collection<Integer> list = updateSubtasksInEpic(epicMap.get(subtask.getEpicId()));
        Epic epic = epicMap.get(subtask.getEpicId());
        list.add(subtask.getId());
        epic.setSubTaskIds(list);
        epicMap.put(subtask.getEpicId(), epic);
        updateStatus(subtask.getEpicId());
        updateTime(subtask.getEpicId());
        prioritizedTasks.add(subtask);
        validate();

        return subtask.getId();
    }

    @Override
    public void update(Task task) {
        taskMap.put(task.getId(), task);
        validate();

    }

    @Override
    public void update(Epic epic) {
        Collection<Integer> list = epicMap.get(epic.getId()).getSubTaskIds();
        epic.setSubTaskIds(list);
        epicMap.put(epic.getId(), epic);
        updateStatus(epic.getId());
        updateTime(epic.getId());
    }

    @Override
    public void update(Subtask subtask) {
        subtaskMap.put(subtask.getId(), subtask);
        Epic epic = epicMap.get(subtask.getEpicId());
        Collection<Integer> list = epicMap.get(epic.getId()).getSubTaskIds();
        epic.setSubTaskIds(list);
        updateStatus(subtask.getEpicId());
        updateTime(epic.getId());
        validate();
    }

    @Override
    public void deleteTask(int id) {
        taskMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Collection<Integer> subTaskIds = epicMap.get(id).getSubTaskIds();

        epicMap.remove(id);

        for (Integer subTaskId : subTaskIds) {
            historyManager.remove(subTaskId);
            subtaskMap.remove(subTaskId);
        }

        historyManager.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtaskMap.remove(id);

        int epicId = subtask.getEpicId();
        Epic epic = epicMap.get(epicId);
        epic.removeSubtask(id);

        historyManager.remove(id);
    }

    @Override
    public Collection<Subtask> getEpicSubtasks(int epicId) {
        Collection<Subtask> list = new ArrayList<>();
        for (Integer subTaskId : epicMap.get(epicId).getSubTaskIds()) {
            list.add(subtaskMap.get(subTaskId));
        }

        return list;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}
