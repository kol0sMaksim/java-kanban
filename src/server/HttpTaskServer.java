package server;

import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import server.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) {

        this.taskManager = taskManager;
        try {
            httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            httpServer.createContext("/tasks", new TasksHandler(taskManager));
            httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
            httpServer.createContext("/epics", new EpicsHandler(taskManager));
            httpServer.createContext("/history", new HistoryHandler(taskManager));
            httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("http://localhost:" + PORT + "/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }
}
