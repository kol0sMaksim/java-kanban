package manager;
/*
    Интерфейс для истории просмотров
*/

import model.Task;

import java.util.Collection;

public interface HistoryManager {

    void add(Task task);

    Collection<Task> getHistory();
}
