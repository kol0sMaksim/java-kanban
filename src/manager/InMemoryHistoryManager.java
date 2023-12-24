package manager;
/*
    Класс для работы с историей просмотра
*/
import model.Task;

import java.util.Collection;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager{

    private static final int MAX_SIZE = 10;

    private static final Collection<Task> viewHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (viewHistory.size() < MAX_SIZE) {
            viewHistory.add(task);
        } else {
            viewHistory.remove(0);
            viewHistory.add(task);
        }
    }

    @Override
    public Collection<Task> getHistory() {
        return viewHistory;
    }
}
