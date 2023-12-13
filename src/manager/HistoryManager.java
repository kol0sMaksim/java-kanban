package manager;
/*
    Интерфейс для истории просмотров
*/

import model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
