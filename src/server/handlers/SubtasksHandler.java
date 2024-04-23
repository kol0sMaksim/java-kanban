package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Endpoint;
import manager.TaskManager;
import model.Subtask;
import server.TaskGson;

import java.io.IOException;
import java.util.regex.Pattern;

public class SubtasksHandler extends BaseHandler implements HttpHandler {

    private TaskManager manager;

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_SUBTASKS:
                handleGetSubtasks(exchange);
                break;
            case GET_SUBTASK:
                handleGetSubtask(exchange);
                break;
            case POST_SUBTASK:
                handlePostSubtask(exchange);
                break;
            case DELETE_SUBTASK:
                handleDeleteSubtask(exchange);
                break;
            default:
                writeResponse(exchange, "Такого эндпоинта нет", 404);
        }
    }

    public Endpoint getEndpoint(String path, String method) {
        try {
            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        return Endpoint.GET_SUBTASK;
                    } else if (Pattern.matches("^/subtasks$", path)) {
                        return Endpoint.GET_SUBTASKS;
                    } else {
                        return Endpoint.UNKNOWN;
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/subtasks$", path)) {
                        return Endpoint.POST_SUBTASK;
                    }
                }
                case "DELETE": {
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        return Endpoint.DELETE_SUBTASK;
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

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        writeResponse(exchange, TaskGson.GSON.toJson(manager.getAllSubtask()), 200);
    }

    private void handleGetSubtask(HttpExchange exchange) throws IOException {
        String url = exchange.getRequestURI().toString();
        String[] urlPath = url.split("/");

        int id = parsePathId(urlPath[2]);

        if (manager.getEntitySubtask(id) == null) {
            writeResponse(exchange, "Запрашиваемая подзадача не существует", 404);
        } else if (id == -1) {
            writeResponse(exchange, "Некорректный номер запрашиваемй подзадачи", 404);
        } else {
            Subtask subtask = manager.getEntitySubtask(id);
            writeResponse(exchange, TaskGson.GSON.toJson(subtask), 200);
        }
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Subtask subtask;

        subtask = TaskGson.GSON.fromJson(body, Subtask.class);

        if (manager.getEntitySubtask(subtask.getId()) == null) {
            manager.create(subtask);
            writeResponse(exchange, "Подадача создана", 201);
        } else {
            manager.update(subtask);
            writeResponse(exchange, "Подадача обновлена", 201);
        }
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String url = exchange.getRequestURI().toString();
        String[] urlPath = url.split("/");

        int id = parsePathId(urlPath[2]);

        if (manager.getEntitySubtask(id) == null) {
            writeResponse(exchange, "Удаляемая подзадача не существует", 404);
        } else if (id == -1) {
            writeResponse(exchange, "Некорректный номер удаляемой подзадачи", 404);
        } else {
            manager.deleteSubtask(id);
            writeResponse(exchange, "Подзадача " + id + " удалена", 200);
        }
    }
}
