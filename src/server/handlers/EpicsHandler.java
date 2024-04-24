package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Endpoint;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import server.TaskGson;

import java.io.IOException;
import java.util.Collection;
import java.util.regex.Pattern;

public class EpicsHandler extends BaseHandler implements HttpHandler {

    private TaskManager manager;

    public EpicsHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_EPICS:
                handleGetEpics(exchange);
                break;
            case GET_EPIC:
                handleGetEpic(exchange);
                break;
            case GET_EPICS_SUBTASK:
                handleGetSubtaskFromEpic(exchange);
                break;
            case POST_EPIC:
                handlePostEpic(exchange);
                break;
            case DELETE_EPIC:
                handleDeleteEpic(exchange);
                break;
            default:
                writeResponse(exchange, "Такого эндпоинта нет", 404);
        }
    }

    public Endpoint getEndpoint(String path, String method) {
        try {
            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        return Endpoint.GET_EPIC;
                    } else if (Pattern.matches("^/epics$", path)) {
                        return Endpoint.GET_EPICS;
                    } else if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                        return Endpoint.GET_EPICS_SUBTASK;
                    } else {
                        return Endpoint.UNKNOWN;
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/epics$", path)) {
                        return Endpoint.POST_EPIC;
                    }
                }
                case "DELETE": {
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        return Endpoint.DELETE_EPIC;
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

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        writeResponse(exchange, TaskGson.GSON.toJson(manager.getAllEpic()), 200);
    }

    private void handleGetEpic(HttpExchange exchange) throws IOException {
        String url = exchange.getRequestURI().toString();
        String[] urlPath = url.split("/");

        int id = parsePathId(urlPath[2]);

        if (manager.getEntityEpic(id) == null) {
            writeResponse(exchange, "Запрашиваемый эпик не существует", 404);
        } else if (id == -1) {
            writeResponse(exchange, "Некорректный номер запрашиваемого эпика", 404);
        } else {
            Epic epic = manager.getEntityEpic(id);
            writeResponse(exchange, TaskGson.GSON.toJson(epic), 200);
        }
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Epic epic;

        epic = TaskGson.GSON.fromJson(body, Epic.class);

        manager.create(epic);
        writeResponse(exchange, "Эпик создан", 201);
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String url = exchange.getRequestURI().toString();
        String[] urlPath = url.split("/");

        int id = parsePathId(urlPath[2]);

        if (manager.getEntityEpic(id) == null) {
            writeResponse(exchange, "Удаляемая задача не существует", 404);
        } else if (id == -1) {
            writeResponse(exchange, "Некорректный номер удаляемой задачи", 404);
        } else {
            manager.deleteEpic(id);
            writeResponse(exchange, "Задача " + id + " удалена", 200);
        }
    }

    private void handleGetSubtaskFromEpic(HttpExchange exchange) throws IOException {
        String url = exchange.getRequestURI().toString();
        String[] urlPath = url.split("/");

        int id = parsePathId(urlPath[2]);

        if (manager.getEntityEpic(id) == null) {
            writeResponse(exchange, "Запрашиваемый эпик не существует", 404);
        } else if (id == -1) {
            writeResponse(exchange, "Некорректный номер запрашиваемого эпика", 404);
        } else {
            Collection<Subtask> subtasksFromEpic = manager.getEpicSubtasks(id);
            writeResponse(exchange, TaskGson.GSON.toJson(subtasksFromEpic), 200);
        }
    }
}
