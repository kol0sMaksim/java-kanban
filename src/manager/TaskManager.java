package manager;
/*
    Интерфейс для объекта-менеджера
*/
import model.*;
import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getAllTask();

    ArrayList<Epic> getAllEpic();

    ArrayList<Subtask> getAllSubtask();

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

    ArrayList<Subtask> getEpicSubtasks(int epicId);

}
