import manager.Managers;
import manager.TaskManager;
import model.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static enums.Status.*;

public class Main {

    public static void main(String[] args) {
    /*
        Далее внизу тесты для самопроверки работы приложения
    */
        File path = new File("src/resource/test.csv");

        TaskManager taskManager = Managers.getDefault(path);

        int task1 = taskManager.create(
                new Task(
                        "Первая таска", "Тест", 1 , NEW,
                        LocalDateTime.now(),
                        Duration.ofMinutes(10)));
        int task2 = taskManager.create(
                new Task("Вторая таска", "Тест",2 , NEW,
                        LocalDateTime.of(2024, Month.FEBRUARY, 21, 10, 0),
                        Duration.ofMinutes(10)));
        int task3 = taskManager.create(
                new Task("Вторая таска", "Тест",2 , NEW,
                        null,
                        Duration.ofMinutes(200)));

        int epic1 = taskManager.create(new Epic("Первый эпик", "Тест", 3, NEW));
        int subtask1 = taskManager.create(
                new Subtask("Сабтаска №1_1", "Тест", 4, epic1, NEW,
                        LocalDateTime.of(2024, Month.FEBRUARY, 21, 11, 0),
                        Duration.ofMinutes(10))
        );
        int subtask2 = taskManager.create(
                new Subtask("Сабтаска №1_2", "Тест",5, epic1, NEW,
                        LocalDateTime.of(2024, Month.FEBRUARY, 21, 12, 0),
                        Duration.ofMinutes(10))
        );
        int subtask3 = taskManager.create(
                new Subtask("Сабтаска №1_3", "Тест",6, epic1, NEW,
                        LocalDateTime.of(2024, Month.FEBRUARY, 21, 13, 0),
                        Duration.ofMinutes(10))
        );

        taskManager.getEntityTask(task1);
        taskManager.getEntityEpic(epic1);
        taskManager.getEntitySubtask(subtask1);
        taskManager.getEntitySubtask(subtask2);
        taskManager.getEntitySubtask(subtask1);

        System.out.println(taskManager.getPrioritizedTasks());

        for (Task prioritizedTask : taskManager.getPrioritizedTasks()) {
            System.out.println(prioritizedTask.getId() + "  " + prioritizedTask.getStartTime());
        }
    }
}
