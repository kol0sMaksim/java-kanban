import com.google.gson.*;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.TaskGson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static enums.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    private TaskManager managers;
    private HttpTaskServer httpTaskServer;
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    Task task1 = new Task("Task1", "For Task1", 1, NEW,
            LocalDateTime.of(2024, Month.FEBRUARY, 15, 18, 30), Duration.ofMinutes(30)
    );
    Task task2 = new Task("Task2", "For Task2", 2, NEW,
            LocalDateTime.of(2024, Month.FEBRUARY, 15, 15, 30), Duration.ofMinutes(30)
    );

    Epic epic1 = new Epic("Epic1", "For Epic1",4, NEW);
    Epic epic2 = new Epic("Epic2", "For Epic2",5, NEW);

    Subtask subtask1 = new Subtask("Subtask1", "For Subtask1",7,1, NEW,
            LocalDateTime.of(2024, Month.FEBRUARY, 13, 14, 30), Duration.ofMinutes(30)
    );

    Subtask subtask2 = new Subtask("Subtask2", "For Subtask2",8,1, NEW,
            LocalDateTime.of(2024, Month.FEBRUARY, 13, 14, 30), Duration.ofMinutes(30)
    );

    @BeforeEach
    public void startServer() throws IOException {
        managers = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(managers);
        httpTaskServer.start();
    }

    @AfterEach
    public void stopServer() {
        httpTaskServer.stop();
    }

    @Test
    void checkGETTasks() throws IOException, InterruptedException {
        managers.create(task1);
        managers.create(task2);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray());

        JsonArray jsonObject = jsonElement.getAsJsonArray();
        Task[] tasksFromJson = TaskGson.GSON.fromJson(jsonObject, Task[].class);

        assertEquals(200, response.statusCode());
        assertArrayEquals(new Task[] {task1, task2}, tasksFromJson);
    }

    @Test
    void checkGetTaskById() throws IOException, InterruptedException {
        managers.create(task1);
        int id = task1.getId();

        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject());

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        int idTask = jsonObject.get("id").getAsInt();
        String nameTask = jsonObject.get("name").getAsString();
        String descriptionTask = jsonObject.get("description").getAsString();
        String statusTask = jsonObject.get("status").getAsString();

        assertEquals(task1.getId(), idTask);
        assertEquals(task1.getName(), nameTask);
        assertEquals(task1.getDescription(), descriptionTask);
        assertEquals(task1.getStatus().toString(), statusTask);
    }

    @Test
    void checkPOSTTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks");
        String json = TaskGson.GSON.toJson(task1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(201, response.statusCode());
        assertEquals(task1.toString(), managers.getEntityTask(task1.getId()).toString());
        assertEquals(managers.getAllTask().size(), 1);
    }

    @Test
    void checkDELETETask() throws IOException, InterruptedException {
        managers.create(task1);
        int id = task1.getId();

        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());
        assertEquals(managers.getAllTask().size(), 0);
    }

    @Test
    void checkGETSubtasks() throws IOException, InterruptedException {
        managers.create(epic1);
        managers.create(subtask1);
        managers.create(subtask2);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray());

        JsonArray jsonObject = jsonElement.getAsJsonArray();
        Subtask[] subtasksFromJson = TaskGson.GSON.fromJson(jsonObject, Subtask[].class);

        assertEquals(200, response.statusCode());
        assertArrayEquals(new Subtask[] {subtask1, subtask2}, subtasksFromJson);
    }

    @Test
    void checkPOSTSubtask() throws IOException, InterruptedException {
        managers.create(epic1);
        URI uri = URI.create("http://localhost:8080/subtasks");
        String json = TaskGson.GSON.toJson(subtask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(201, response.statusCode());
        assertEquals(managers.getAllSubtask().size(), 1);
    }

    @Test
    void checkGetSubtaskById() throws IOException, InterruptedException {
        managers.create(epic1);
        managers.create(subtask1);

        int id = subtask1.getId();

        URI url = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject());

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        int idTask = jsonObject.get("id").getAsInt();
        String nameTask = jsonObject.get("name").getAsString();
        String descriptionTask = jsonObject.get("description").getAsString();
        String typeTask = jsonObject.get("type").getAsString();
        String statusTask = jsonObject.get("status").getAsString();

        assertEquals(subtask1.getId(), idTask);
        assertEquals(subtask1.getName(), nameTask);
        assertEquals(subtask1.getDescription(), descriptionTask);
        assertEquals(subtask1.getType().toString(), typeTask);
        assertEquals(subtask1.getStatus().toString(), statusTask);
    }

    @Test
    void checkDELETESubtask() throws IOException, InterruptedException {
        managers.create(epic1);
        managers.create(subtask1);

        int id = subtask1.getId();

        URI url = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());
        assertEquals(managers.getAllSubtask().size(), 0);
    }

    @Test
    void checkGETEpics() throws IOException, InterruptedException {
        managers.create(epic1);
        managers.create(epic2);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray());

        JsonArray jsonObject = jsonElement.getAsJsonArray();
        Epic[] epicsFromJson = TaskGson.GSON.fromJson(jsonObject, Epic[].class);

        assertEquals(200, response.statusCode());
        assertArrayEquals(new Epic[]{epic1, epic2}, epicsFromJson);
    }

    @Test
    void checkGetEpicById() throws IOException, InterruptedException {
        managers.create(epic1);
        int id = epic1.getId();

        URI url = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject());

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        int idTask = jsonObject.get("id").getAsInt();
        String nameTask = jsonObject.get("name").getAsString();
        String descriptionTask = jsonObject.get("description").getAsString();
        String typeTask = jsonObject.get("type").getAsString();
        String statusTask = jsonObject.get("status").getAsString();

        assertEquals(epic1.getId(), idTask);
        assertEquals(epic1.getName(), nameTask);
        assertEquals(epic1.getDescription(), descriptionTask);
        assertEquals(epic1.getType().toString(), typeTask);
        assertEquals(epic1.getStatus().toString(), statusTask);
    }

    @Test
    void checkPOSTEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics");
        String json = TaskGson.GSON.toJson(epic1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(201, response.statusCode());
        assertEquals(managers.getAllEpic().size(), 1);
    }

    @Test
    void checkDELETEEpic() throws IOException, InterruptedException {
        managers.create(epic1);
        int id = epic1.getId();

        URI url = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());
        assertEquals(managers.getAllEpic().size(), 0);
    }

    @Test
    void checkGETSubtaskFromEpic() throws IOException, InterruptedException {
        managers.create(epic1);
        managers.create(subtask1);
        managers.create(subtask2);

        URI url = URI.create("http://localhost:8080/epics/" + epic1.getId() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray());

        JsonArray jsonArray = jsonElement.getAsJsonArray();
        Subtask[] loadedSubtasks = TaskGson.GSON.fromJson(jsonArray, Subtask[].class);
        assertArrayEquals(loadedSubtasks, new Subtask[]{subtask1, subtask2});
    }

    @Test
    void checkGETHistory() throws IOException, InterruptedException {
        managers.create(task1);
        managers.create(task2);
        managers.getEntityTask(task1.getId());
        managers.getEntityTask(task2.getId());

        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(2, jsonArray.size());
    }

    @Test
    void checkGETPrioritizedTasks() throws IOException, InterruptedException {
        managers.create(task1);
        managers.create(task2);

        task1.setStartTime(LocalDateTime.parse("2024-04-21T00:00"));
        task2.setStartTime(LocalDateTime.parse("2024-04-22T00:00"));

        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray());

        assertEquals(2, jsonElement.getAsJsonArray().size());
    }
}
