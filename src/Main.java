import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import model.*;

import java.io.File;

import static enums.Status.*;

public class Main {

    public static void main(String[] args) {
    /*
        Далее внизу тесты для самопроверки работы приложения
    */
        File path = new File("src/resource/test.csv");

        TaskManager taskManager = Managers.getDefault(path);

        int task1 = taskManager.create(new Task("Первая таска", "Тест",1 , NEW));
        int task2 = taskManager.create(new Task("Вторая таска", "Тест",2 , NEW));

        int epic1 = taskManager.create(new Epic("Первый эпик", "Тест", 3, NEW));
        int subtask1 = taskManager.create(
                new Subtask("Сабтаска №1_1", "Тест", 4, epic1, NEW)
        );
        int subtask2 = taskManager.create(
                new Subtask("Сабтаска №1_2", "Тест",5, epic1, NEW)
        );
        int subtask3 = taskManager.create(
                new Subtask("Сабтаска №1_3", "Тест",6, epic1, NEW)
        );

        taskManager.getEntityTask(task1);
        taskManager.getEntityEpic(epic1);
        taskManager.getEntitySubtask(subtask1);
        taskManager.getEntitySubtask(subtask2);
        taskManager.getEntitySubtask(subtask1);

        TaskManager taskManager2 = FileBackedTasksManager.loadFromFile(path);

        System.out.println(taskManager2.getHistory());
    }
}
