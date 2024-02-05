package manager;

import enums.Status;
import enums.Type;
import exception.ManagerLoadException;
import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private final File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public void save() {
        try (Writer writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTask()) {
                writer.write(task.toString());
                writer.write("\n");
            }

            for (Epic epic : getAllEpic()) {
                writer.write(epic.toString());
                writer.write("\n");
            }

            for (Subtask subtask : getAllSubtask()) {
                writer.write(subtask.toString());
                writer.write("\n");
            }
            writer.write("\n");
            writer.write(historyToString(historyManager));

        } catch (Exception e) {
            throw new ManagerSaveException("Не удалось сохранить методанные");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        try (BufferedReader fileReader = new BufferedReader( new FileReader(file, StandardCharsets.UTF_8))) {

            fileReader.readLine();

            while (fileReader.ready()) {
                String line = fileReader.readLine();

                if (line.isEmpty()) {
                    break;
                }

                Task task = fromString(line);

                if (task.getType().equals(Type.EPIC)) {
                    fileBackedTasksManager.create((Epic) task);
                } else if (task.getType().equals(Type.SUBTASK)) {
                    fileBackedTasksManager.create((Subtask) task);
                } else {
                    fileBackedTasksManager.create(task);
                }
            }

            String lineHistory = fileReader.readLine();

            if (lineHistory == null || lineHistory.isEmpty()) {
                return fileBackedTasksManager;
            }

            for (Integer id : historyFromString(lineHistory)) {
                if (fileBackedTasksManager.taskMap.containsKey(id)) {
                    Task task = fileBackedTasksManager.getEntityTask(id);
                    fileBackedTasksManager.historyManager.add(task);
                } else if (fileBackedTasksManager.subtaskMap.containsKey(id)) {
                    Task task = fileBackedTasksManager.getEntitySubtask(id);
                    fileBackedTasksManager.historyManager.add(task);
                } else {
                    Task task = fileBackedTasksManager.getEntityEpic(id);
                    fileBackedTasksManager.historyManager.add(task);
                }
            }

        } catch (Exception e) {
            throw new ManagerLoadException("Не удалось прочесть файл");
        }

        return fileBackedTasksManager;
    }

    public static String historyToString(HistoryManager manager) {
        final Collection<Task> listTasksHistory = manager.getHistory();
        StringBuilder historyId = new StringBuilder();

        for (Task task : listTasksHistory) {
            historyId.append(task.getId()).append(",");
        }

        return historyId.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> listTasksHistory = new ArrayList<>();

        String[] taskId = value.split(",");

        for (String id : taskId) {
            listTasksHistory.add(Integer.parseInt(id));
        }

        return listTasksHistory;
    }

    public static Task fromString(String value) {
        String[] attributesFromFile = value.split(",");

        int id = Integer.parseInt(attributesFromFile[0]);
        Type type = Type.valueOf(attributesFromFile[1]);
        String name = attributesFromFile[2];
        Status status = Status.valueOf(attributesFromFile[3]);
        String description = attributesFromFile[4];

        if (type.equals(Type.TASK)) {
            return new Task(name, description, id, status);
        } else if (type.equals(Type.EPIC)) {
            return new Epic(name, description, id, status);
        } else if (type.equals(Type.SUBTASK)) {
            return new Subtask(name, description, id, Integer.parseInt(attributesFromFile[5]), status);
        } else {
            throw new ManagerSaveException("Ошибка вычетки из строки");
        }
    }


    @Override
    public Task getEntityTask(int id) {
        Task task = super.getEntityTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEntityEpic(int id) {
        Epic epic = super.getEntityEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getEntitySubtask(int id) {
        Subtask subtask = super.getEntitySubtask(id);
        save();
        return subtask;
    }

    @Override
    public int create(Task task) {
        int create = super.create(task);
        save();
        return create;
    }

    @Override
    public int create(Epic epic) {
        int create = super.create(epic);
        save();
        return create;
    }

    @Override
    public int create(Subtask subtask) {
        int create = super.create(subtask);
        save();
        return create;
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public Collection<Subtask> getEpicSubtasks(int epicId) {
        Collection<Subtask> subtasks = super.getEpicSubtasks(epicId);
        save();
        return subtasks;
    }

}
