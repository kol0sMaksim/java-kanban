/*
    Класс для хранения методанных по Эпикам
*/
import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<Integer> subTaskIds;

    public Epic(String name, String description, int id, String status) {
        super(name, description, id, status);
        subTaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
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
