import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    File path = new File("test/resource/forTests.csv");

    @BeforeEach
    public void FileBackedTasksManagerInitialization() {
        taskManager = (FileBackedTasksManager) Managers.getDefault(path);
    }


    @Test
    public void checkEmptyListTasks() {
        TaskManager manager = FileBackedTasksManager.loadFromFile(path);

        assertEquals(0, manager.getAllTask().size(), "Из пустого файла сформировались задачи");
    }

    @Test
    public void checkCorrectReadingFromFileWithoutSubtask() {
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(epic1);

        Task t1 = taskManager.getEntityTask(task1.getId());
        Task t2 = taskManager.getEntityTask(task2.getId());
        Epic e1 = taskManager.getEntityEpic(epic1.getId());

        TaskManager manager = FileBackedTasksManager.loadFromFile(path);

        assertEquals(2, manager.getAllTask().size(), "Задачи не востановились из файла");
        assertEquals(1, manager.getAllEpic().size(), "Эпики не востановились из файла");

        Collection<Task> tasksFromFile = manager.getAllTask();
        Collection<Epic> epicsFromFile = manager.getAllEpic();

        Collection<Task> tasks = List.of(task1, task2);
        Collection<Epic> epics = List.of(epic1);

        assertEquals(tasks, tasksFromFile, "Вычетанные задачи не совпадают с исходынми");
        assertEquals(epics, epicsFromFile, "Вычетанные эпики не совпадают с исходынми");

        assertEquals(
                List.of(t1, t2, e1),
                taskManager.getHistory(),
                "Задачи в истории распологоются не верно"
        );
    }

    @Test
    public void checkEmptyHistoryFromFile() {
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(task3);

        TaskManager manager = FileBackedTasksManager.loadFromFile(path);

        assertEquals(Collections.emptyList(), manager.getHistory(), "История просмотров не пуста");
    }
}
