package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import helpers.DurationAdapter;
import helpers.LocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskGson {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
}
