package manager;

/*
    Утилитарный класс, отвечает за создание менеджера задач
*/

import model.Task;

import java.util.List;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static List<Task> getDefaultHistory() {
        return new InMemoryHistoryManager().getHistory();
    }
}
