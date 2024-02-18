import enums.Status;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

import static enums.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {

    TaskManager taskManager = Managers.getDefault();

    Epic epic = new Epic("Test", "For Test", 1, NEW);

    Subtask subtask1 = new Subtask("Сабтаска №1_1", "Тест", 4, 1, NEW,
            LocalDateTime.of(2024, Month.FEBRUARY, 13, 14, 30), Duration.ofMinutes(1)
    );
    Subtask subtask2 = new Subtask("Сабтаска №1_1", "Тест", 4, 1, NEW,
            LocalDateTime.of(2024, Month.FEBRUARY, 13, 14, 35), Duration.ofMinutes(1));

    @Test
    public void checkEmptyList() {
        taskManager.create(epic);
        assertEquals(Collections.emptyList(), epic.getSubTaskIds(), "Список подзадач не пуст");
    }

    @Test
    public void checkNewStatusFromEpic() {
        taskManager.create(epic);
        taskManager.create(subtask1);
        taskManager.create(subtask2);

        assertEquals(2, epic.getSubTaskIds().size(), "Неверное количество задач");
        assertEquals(NEW, epic.getStatus(), "В эпике не все подзадачи имеют статус NEW");
    }

    @Test
    public void checkReturnDoneStatusFromEpic() {
        subtask1.setStatus(DONE);
        subtask2.setStatus(DONE);

        taskManager.create(epic);
        taskManager.create(subtask1);
        taskManager.create(subtask2);

        assertEquals(DONE, epic.getStatus(), "В эпике не все подзадачи имеют статус DONE");
    }

    @Test
    public void checkReturnInProgressStatusFromEpic() {
        subtask1.setStatus(NEW);
        subtask2.setStatus(DONE);

        taskManager.create(epic);
        taskManager.create(subtask1);
        taskManager.create(subtask2);

        assertEquals(IN_PROGRESS, epic.getStatus(), "В эпике не все подзадачи имеют статус IN_PROGRESS");
    }

    @Test
    public void checkReturnInProgressStatusFromEpicToo() {
        subtask1.setStatus(IN_PROGRESS);
        subtask2.setStatus(IN_PROGRESS);

        taskManager.create(epic);
        taskManager.create(subtask1);
        taskManager.create(subtask2);

        assertEquals(IN_PROGRESS, epic.getStatus(), "В эпике не все подзадачи имеют статус IN_PROGRESS");
    }
}
