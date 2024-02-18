package model;

import enums.Status;
import enums.Type;

import java.time.Duration;
import java.time.LocalDateTime;

/*
    Класс для хранения методанных по Сабтаскам
*/
public class Subtask extends Task{

    private int epicId;
    private Type type;

    public Subtask(String name, String description, int id, int epicId, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description, id, status, startTime, duration);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", getId(), getType(), getName(),
                getStatus(), getDescription(), getStartTime(), getDuration(), getEpicId());
    }
}
