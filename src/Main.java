import manager.Managers;
import manager.TaskManager;
import model.*;
import static emun.Status.*;

public class Main {

    public static void main(String[] args) {
    /*
        Далее внизу тесты для самопроверки работы приложения
    */
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        int task1 = taskManager.create(new Task("Первая таска", "Тест",1 , NEW));
        int task2 = taskManager.create(new Task("Вторая таска", "Тест",2 , NEW));

        int epic1 = taskManager.create(new Epic("Первый эпик", "Тест", 3, NEW));
        int subtask1 = taskManager.create(
                new Subtask("Сабтаска №1_1", "Тест", 4, epic1, NEW)
        );
        int subtask2 = taskManager.create(
                new Subtask("Сабтаска №1_2", "Тест",5, epic1, NEW)
        );

        int epic2 = taskManager.create(new Epic("Второй эпик", "Тест", 6, NEW));
        int subtask3 = taskManager.create(
                new Subtask("Сабтаска №2_1", "Тест", 7, epic2, NEW)
        );

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask());

        taskManager.getEntityTask(task1);
        taskManager.getEntityTask(task2);
        taskManager.getEntityEpic(epic1);
        taskManager.getEntitySubtask(subtask1);
        System.out.println("Исторя просмотров: ");
        System.out.println(managers.getDefaultHistory());
        taskManager.getEntitySubtask(subtask2);
        taskManager.getEntityEpic(epic2);
        taskManager.getEntitySubtask(subtask3);
        System.out.println("Исторя просмотров: ");
        System.out.println(managers.getDefaultHistory());


        System.out.println("После апдейта получается: ");
        taskManager.update(new Task("Up Первая таска", "Тест", task1, IN_PROGRESS));
        taskManager.update(new Task("Up Вторая таска", "Тест", task2, IN_PROGRESS));

        taskManager.update(new Epic("Up Первый эпик", "Тест", epic1, NEW));
        taskManager.update(new Subtask("Up Сабтаска №1_1", "Тест",subtask1, epic1, DONE));
        taskManager.update(new Subtask("Up Сабтаска №1_2", "Тест",subtask2, epic1, DONE));

        taskManager.update(new Epic("Up Второй эпик", "Тест", epic2, NEW));
        taskManager.update(new Subtask(
                "Up Сабтаска №2_1", "Тест",subtask3, epic2, IN_PROGRESS)
        );

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask());

        System.out.println("Проверка получения сабтасов из эпика: ");
        System.out.println(taskManager.getEpicSubtasks(epic1));

        taskManager.deleteTask(task1);
        taskManager.deleteEpic(epic1);

        System.out.println("После удаления: ");
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask());

    }
}
