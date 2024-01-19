package manager;

import model.Task;

import java.util.Collection;
/*
    Интерфейс для истории просмотров
*/
public interface HistoryManager {

    void add(Task task);

    Collection<Task> getHistory();

    void remove(int id);
}
