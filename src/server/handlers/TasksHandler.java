package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Endpoint;
import manager.TaskManager;
import model.Task;
import server.TaskGson;

import java.io.IOException;
import java.util.regex.Pattern;

public class TasksHandler extends BaseHandler implements HttpHandler {

    TaskManager manager;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS:
                handleGetTasks(exchange);
                break;
            case GET_TASK:
                handleGetTask(exchange);
                break;
            case POST_TASK:
                handlePostTask(exchange);
                break;
            case DELETE_TASK:
                handleDeleteTasks(exchange);
                break;
            default:
                writeResponse(exchange, "Такого эндпоинта нет", 404);
        }
    }

    public Endpoint getEndpoint(String path, String method) {
        try {
            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        return Endpoint.GET_TASK;
                    } else if (Pattern.matches("^/tasks$", path)) {
                        return Endpoint.GET_TASKS;
                    } else {
                        return Endpoint.UNKNOWN;
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/tasks$", path)) {
                        return Endpoint.POST_TASK;
                    }
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        return Endpoint.DELETE_TASK;
                    }
                }
                default: {
                    return Endpoint.UNKNOWN;
                }
            }
        } catch (Exception e) {
            return Endpoint.UNKNOWN;
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        writeResponse(exchange, TaskGson.GSON.toJson(manager.getAllTask()), 200);
    }

    private void handleGetTask(HttpExchange exchange) throws IOException {
        String url = exchange.getRequestURI().toString();
        String[] urlPath = url.split("/");

        int id = parsePathId(urlPath[2]);

        if (manager.getEntityTask(id) == null) {
            writeResponse(exchange, "Запрашиваемая задача не существует", 404);
        } else if (id == -1) {
            writeResponse(exchange, "Некорректный номер запрашиваемй задачи", 404);
        } else {
            Task task = manager.getEntityTask(id);
            writeResponse(exchange, TaskGson.GSON.toJson(task), 200);
        }
    }

    private void handleDeleteTasks(HttpExchange exchange) throws IOException {
        String url = exchange.getRequestURI().toString();
        String[] urlPath = url.split("/");

        int id = parsePathId(urlPath[2]);

        if (manager.getEntityTask(id) == null) {
            writeResponse(exchange, "Удаляемая задача не существует", 404);
        } else if (id == -1) {
            writeResponse(exchange, "Некорректный номер удаляемой задачи", 404);
        } else {
            manager.deleteTask(id);
            writeResponse(exchange, "Задача " + id + " удалена", 200);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException{
        String body = readBody(exchange);
        Task task;

        task = TaskGson.GSON.fromJson(body, Task.class);

        if (manager.getEntityTask(task.getId()) == null) {
            manager.create(task);
            writeResponse(exchange, "Задача создана", 201);
        } else {
            manager.update(task);
            writeResponse(exchange, "Задача обновлена", 201);
        }
    }
}
