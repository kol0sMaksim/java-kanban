package model;

import enums.Status;
import enums.Type;

/*
    Класс для хранения методанных по Сабтаскам
*/
public class Subtask extends Task{

    private int epicId;
    private Type type;

    public Subtask(String name, String description,int id, int epicId, Status status) {
        super(name, description, id, status);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s", getId(), getType(), getName(),
                getStatus(), getDescription(), getEpicId());
    }
}
