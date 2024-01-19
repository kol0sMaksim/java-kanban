package model;

import enums.Status;

import java.util.ArrayList;
import java.util.Collection;
/*
    Класс для хранения методанных по Эпикам
*/
public class Epic extends Task{
    Collection<Integer> subTaskIds;

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
        subTaskIds = new ArrayList<>();
    }

    public Collection<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(Collection<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + "'" +
                ", subtaskIDs=" + subTaskIds +
                ", status='" + getStatus() + "'" +
                '}';
    }
}
