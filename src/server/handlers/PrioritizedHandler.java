package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Endpoint;
import manager.TaskManager;
import server.TaskGson;

import java.io.IOException;
import java.util.regex.Pattern;

public class PrioritizedHandler extends BaseHandler implements HttpHandler {

    private TaskManager manager;

    public PrioritizedHandler(TaskManager manager) {
        this.manager = manager;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_PRIORITIZED_TASKS:
                handleGetPrioritizedTasks(exchange);
                break;
            default:
                writeResponse(exchange, "Такого эндпоинта нет", 404);
        }
    }

    public Endpoint getEndpoint(String path, String method) {
        try {
            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/prioritized$", path)) {
                        return Endpoint.GET_PRIORITIZED_TASKS;
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

    private void handleGetPrioritizedTasks(HttpExchange httpExchange) throws IOException {
        writeResponse(httpExchange, TaskGson.GSON.toJson(manager.getPrioritizedTasks()), 200);
    }
}
