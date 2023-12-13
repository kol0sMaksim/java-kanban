package manager;
/*
    Класс для работы с историей просмотра
*/
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private static final int MAX_SIZE = 10;

    private static final List<Task> viewHistory = new ArrayList<>();

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
    public List<Task> getHistory() {
        return viewHistory;
    }
}
