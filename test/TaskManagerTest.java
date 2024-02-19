import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.List;

import static enums.Status.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;

    Task task1 = new Task("Task1", "For Task1", 1, NEW,
            LocalDateTime.of(2024, Month.FEBRUARY, 13, 10, 30), Duration.ofMinutes(30)
    );
    Task task2 = new Task("Task2", "For Task2", 2, NEW,
            LocalDateTime.of(2024, Month.FEBRUARY, 14, 11, 30), Duration.ofMinutes(30)
    );
    Task task3 = new Task("Task3", "For Task3", 3, NEW,
            LocalDateTime.of(2024, Month.FEBRUARY, 15, 12, 30), Duration.ofMinutes(30)
    );

    Epic epic1 = new Epic("Epic1", "For Epic1",4, NEW);
    Epic epic2 = new Epic("Epic2", "For Epic2",5, NEW);

    Subtask subtask1 = new Subtask("Subtask1", "For Subtask1",7,1, NEW,
            LocalDateTime.of(2024, Month.FEBRUARY, 13, 14, 30), Duration.ofMinutes(30)
    );

    Subtask subtask2 = new Subtask("Subtask2", "For Subtask2",8,1, NEW,
            LocalDateTime.of(2024, Month.FEBRUARY, 13, 14, 30), Duration.ofMinutes(30)
    );

    Subtask subtask3 = new Subtask("Subtask3", "For Subtask3",9,1, NEW,
            LocalDateTime.of(2024, Month.FEBRUARY, 13, 14, 30), Duration.ofMinutes(30)
    );


    @Test
    public void checkCreateTask() {
        taskManager.create(task1);

        Task task = taskManager.getEntityTask(task1.getId());
        assertNotNull(task, "Созданная задача пуста");
        assertEquals(1, task1.getId(), "Id задачь не совпадают");
        assertEquals(task1, task, "Одинаковые задачи не равны между собой");
    }

    @Test
    public void checkCreateEpic() {
        taskManager.create(epic1);

        Epic epic = taskManager.getEntityEpic(epic1.getId());
        assertNotNull(epic, "Созданн пустой эпик");
        assertEquals(1, epic.getId(), "Id задачь не совпадают");
        assertEquals(epic1, epic, "Одинаковые епики не равны между собой");
    }

    @Test
    public void checkCreateSubtask() {
        taskManager.create(epic1);
        taskManager.create(subtask1);

        Epic epic = taskManager.getEntityEpic(epic1.getId());
        Subtask subtask = taskManager.getEntitySubtask(subtask1.getId());

        assertNotNull(subtask, "Созданна пустая подзадача");
        assertEquals(epic.getId(), subtask.getEpicId(), "Id эпика не совпадает с epicId сабраска");

        Collection<Subtask> subtasks = taskManager.getEpicSubtasks(epic.getId());

        assertEquals(1, subtasks.size(), "Количество подзадачь отличается");
        assertEquals(subtask1, subtask, "Одинаковые подзадачи не равны между собой");
    }

    @Test
    public void checkReturnAllTask() {
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(task3);

        assertEquals(
                3,
                taskManager.getAllTask().size(),
                "Количество созданных и возращаемых задач отличается"
        );
    }

    @Test
    public void checkReturnAllEpic() {
        taskManager.create(epic1);

        assertEquals(
                1,
                taskManager.getAllEpic().size(),
                "Количество созданных и возращаемых эпиков отличается"
        );
    }

    @Test
    public void checkReturnAllSubtask() {
        taskManager.create(epic1);
        taskManager.create(subtask1);
        taskManager.create(subtask2);
        taskManager.create(subtask3);

        assertEquals(
                3,
                taskManager.getAllSubtask().size(),
                "Количество созданных и возращаемых эпиков отличается"
        );
    }

    @Test
    public void checkClearAllTask() {
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(task3);

        taskManager.clearAllTask();

        assertEquals(0, taskManager.getAllTask().size(), "Не все задачи удалены");
    }

    @Test
    public void checkClearAllEpic() {
        taskManager.create(epic1);
        taskManager.create(epic2);

        taskManager.clearAllEpic();

        assertEquals(0, taskManager.getAllEpic().size(), "Не все эпики удалены");
    }

    @Test
    public void checkClearAllSubtask() {
        taskManager.create(epic1);
        taskManager.create(subtask1);
        taskManager.create(subtask2);

        taskManager.clearAllSubtask();

        assertEquals(0, taskManager.getAllSubtask().size(), "Не все подзадачи удалены");
    }

    @Test
    public void checkUpdateTask() {
        taskManager.create(task1);
        task1.setName("Task_Update");
        task1.setDescription("Task_Description_Update");
        task1.setStatus(IN_PROGRESS);

        taskManager.update(task1);

        Task task = taskManager.getEntityTask(task1.getId());

        assertEquals(task, task1, "Задача не обновлена");
    }

    @Test
    public void checkUpdateEpic() {
        taskManager.create(epic1);
        epic1.setName("Epic_Update");
        epic1.setDescription("Epic_Description_Update");

        taskManager.update(epic1);

        Epic epic = taskManager.getEntityEpic(epic1.getId());

        assertEquals(epic, epic1, "Задача не обновлена");
    }

    @Test
    public void checkUpdateSubtask() {
        taskManager.create(epic1);
        taskManager.create(subtask1);

        subtask1.setName("Epic_Update");
        subtask1.setDescription("Epic_Description_Update");
        subtask1.setStatus(IN_PROGRESS);

        taskManager.update(subtask1);

        Subtask subtask = taskManager.getEntitySubtask(subtask1.getId());

        assertEquals(subtask, subtask1, "Задача не обновлена");
        assertEquals(
                subtask1.getStatus(),
                epic1.getStatus(),
                "Статус у эпика после апдейта подзадачи не обновился"
        );
    }

    @Test
    public void checkDeleteTask() {
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(task3);

        taskManager.deleteTask(task1.getId());

        assertEquals(2, taskManager.getAllTask().size(), "Задача не удалена");
    }

    @Test
    public void checkDeleteEpic() {
        taskManager.create(epic1);
        taskManager.create(subtask1);
        taskManager.create(subtask2);
        taskManager.create(subtask3);

        taskManager.deleteEpic(epic1.getId());

        assertEquals(0, taskManager.getAllEpic().size(), "Эпик не удлен");
        assertEquals(
                0,
                taskManager.getAllSubtask().size(),
                "После удоления эпика подзадачи не удалились"
        );
    }

    @Test
    public void checkDeleteSubtask() {
        taskManager.create(epic1);
        taskManager.create(subtask1);

        taskManager.deleteSubtask(subtask1.getId());

        Collection<Subtask> subtasks = taskManager.getEpicSubtasks(epic1.getId());

        assertEquals(0, taskManager.getAllSubtask().size(), "Подзадача не удлена");
        assertEquals(0, subtasks.size(), "После удаления подзадачи, эпик не пуст");
    }

    @Test
    public void checkRequestForNotExistTask() {
        assertNull(taskManager.getEntityTask(10), "Происходит обработка несуществующей задачи");
        assertNull(taskManager.getEntityEpic(20), "Происходит обработка несуществующего эпика");
        assertNull(taskManager.getEntitySubtask(30), "Происходит обработка несуществующей подзадачи");
    }

    @Test
    public void checkCalculatingEpicStatus() {
        taskManager.create(epic1);

        assertEquals(NEW, epic1.getStatus(), "У созданного эпика без подзадач статус не NEW");

        taskManager.create(subtask1);
        taskManager.create(subtask2);

        subtask1.setStatus(IN_PROGRESS);
        taskManager.update(subtask1);

        assertEquals(IN_PROGRESS, epic1.getStatus(), "Некорректно расчитывается статус эпика");

        subtask1.setStatus(DONE);
        subtask2.setStatus(IN_PROGRESS);
        taskManager.update(subtask1);
        taskManager.update(subtask2);

        assertEquals(IN_PROGRESS, epic1.getStatus(), "Некорректно расчитывается статус эпика");

        subtask2.setStatus(DONE);
        taskManager.update(subtask2);

        assertEquals(DONE, epic1.getStatus(), "Некорректно расчитывается статус эпика");
    }

    @Test
    public void checkEmptyHistory() {
        assertEquals(0, taskManager.getHistory().size(), "История просмотров не пуста");

        taskManager.create(task1);
        taskManager.create(task2);

        taskManager.getEntityTask(task1.getId());
        taskManager.getEntityTask(task2.getId());

        assertEquals(2, taskManager.getHistory().size(), "История просмотров не совпадает");

        taskManager.deleteTask(task1.getId());
        taskManager.deleteTask(task2.getId());

        assertEquals(0, taskManager.getHistory().size(), "История просмотров не пуста");
    }

    @Test
    public void checkDuplicationTaskInHistory() {
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(epic1);

        Task t1 = taskManager.getEntityTask(task1.getId());
        Task t2 = taskManager.getEntityTask(task2.getId());
        Epic e1 = taskManager.getEntityEpic(epic1.getId());
        t2 = taskManager.getEntityTask(task2.getId());
        t1 = taskManager.getEntityTask(task1.getId());

        assertEquals(
                List.of(e1, t2, t1),
                taskManager.getHistory(),
                "Задачи в истории распологоются не верно, после их дубриловария"
        );
    }

    @Test
    public void checkDeleteTaskInHistory() {
        taskManager.create(epic1);
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(subtask1);
        taskManager.create(subtask2);
        taskManager.create(subtask3);

        Epic e1 = taskManager.getEntityEpic(epic1.getId());
        Task t1 = taskManager.getEntityTask(task1.getId());
        Task t2 = taskManager.getEntityTask(task2.getId());
        Subtask s1 = taskManager.getEntitySubtask(subtask1.getId());
        Subtask s2 = taskManager.getEntitySubtask(subtask2.getId());
        Subtask s3 = taskManager.getEntitySubtask(subtask3.getId());

        assertEquals(
                List.of(e1, t1, t2, s1, s2, s3),
                taskManager.getHistory(),
                "Задачи в истории распологоются не верно"
        );

        taskManager.deleteEpic(epic1.getId());

        assertEquals(
                List.of(t1, t2),
                taskManager.getHistory(),
                "Задачи в истории распологоются не верно после удаления эпика с подзадачами"
        );

        taskManager.deleteTask(task1.getId());

        assertEquals(
                List.of(t2),
                taskManager.getHistory(),
                "Задачи в истории распологоются не верно, после удаления задачи с конца"
        );

        taskManager.deleteTask(task2.getId());

        assertEquals(
                List.of(),
                taskManager.getHistory(),
                "После удаления всех задач их истории, возвращаются задачи"
        );
    }

    @Test
    public  void checkCalculatingEpicTimeFromSubtasks() {
        subtask1.setStartTime(LocalDateTime.of(2024, Month.FEBRUARY, 13, 10, 0));
        subtask1.setDuration(Duration.ofMinutes(60));
        subtask2.setStartTime(LocalDateTime.of(2024, Month.FEBRUARY, 13, 12, 0));
        subtask2.setDuration(Duration.ofMinutes(60));
        subtask3.setStartTime(LocalDateTime.of(2024, Month.FEBRUARY, 13, 14, 0));
        subtask3.setDuration(Duration.ofMinutes(60));

        LocalDateTime start = LocalDateTime.of(2024, Month.FEBRUARY, 13, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, Month.FEBRUARY, 13, 15, 0);

        taskManager.create(epic1);
        taskManager.create(subtask1);
        taskManager.create(subtask2);
        taskManager.create(subtask3);

        assertEquals(start, epic1.getStartTime(), "Время старта эпика не совпадает с его подзадачами");
        assertEquals(end, epic1.getEndTime(), "Время окончания эпика не совпадает с его подзадачами");
    }
}