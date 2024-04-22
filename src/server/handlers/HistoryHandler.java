package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Endpoint;
import manager.TaskManager;
import server.TaskGson;

import java.io.IOException;
import java.util.regex.Pattern;

public class HistoryHandler extends BaseHandler implements HttpHandler {

    TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_HISTORY:
                handleGetHistory(exchange);
                break;
            default:
                writeResponse(exchange, "Такого эндпоинта нет", 404);
        }
    }

    public Endpoint getEndpoint(String path, String method) {
        try {
            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/history$", path)) {
                        return Endpoint.GET_HISTORY;
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

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        writeResponse(exchange, TaskGson.GSON.toJson(manager.getHistory()), 200);
    }
}
