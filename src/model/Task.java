package model;

import enums.Status;
import enums.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
/*
    Класс для хранения методанных по Таскам
*/
public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private transient Type type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;

    public Task(String name, String description, int id, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = Type.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Type getType() {
        return Type.TASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && this.startTime.equals(task.startTime)
                && this.duration.equals(task.duration);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        return hash * Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", getId(), getType(), getName(), getStatus(), getDescription(),
                getStartTime(), getDuration());
    }
}
