package model;
/*
    Класс для хранения методанных по Сабтаскам
*/
public class Subtask extends Task{

    private int epicId;

    public Subtask(String name, String description,int id, int epicId, String status) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }


    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", epicId=" + epicId +
                ", status='" + getStatus() +
                "'}";
    }
}
