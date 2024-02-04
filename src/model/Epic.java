package model;

import enums.Status;
import enums.Type;

import java.util.ArrayList;
import java.util.Collection;
/*
    Класс для хранения методанных по Эпикам
*/
public class Epic extends Task{
    Collection<Integer> subTaskIds;
    private Type type;

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
        subTaskIds = new ArrayList<>();
        this.type = Type.EPIC;
    }

    public Collection<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(Collection<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s", getId(), getType(), getName(), getStatus(), getDescription());
    }
}
