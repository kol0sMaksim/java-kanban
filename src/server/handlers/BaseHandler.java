package server.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaseHandler {

    protected int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected void writeResponse(HttpExchange exchange, String response, int code) throws IOException {
        if (response.isBlank()) {
            exchange.sendResponseHeaders(code, 0);
        } else {
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(code, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    protected String readBody(HttpExchange httpExchange) throws IOException {
        try (InputStream os = httpExchange.getRequestBody()) {
            return new String(os.readAllBytes(), StandardCharsets.UTF_8);
        } catch (JsonSyntaxException exp) {
            writeResponse(httpExchange, "Некорректный JSON", 400);
            return "";
        }
    }
}
