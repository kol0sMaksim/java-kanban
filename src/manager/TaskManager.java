package manager;

import model.*;
import java.util.Collection;
import java.util.List;

/*
    Интерфейс для объекта-менеджера
*/
public interface TaskManager {

    Collection<Task> getAllTask();

    Collection<Epic> getAllEpic();

    Collection<Subtask> getAllSubtask();

    void clearAllTask();

    void clearAllEpic();

    void clearAllSubtask();

    Task getEntityTask(int id);

    Epic getEntityEpic(int id);

    Subtask getEntitySubtask(int id);

    int create(Task task);

    int create(Epic epic);

    int create(Subtask subtask);

    void update(Task task);

    void update(Epic epic);

    void update(Subtask subtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    Collection<Subtask> getEpicSubtasks(int epicId);

    Collection<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
