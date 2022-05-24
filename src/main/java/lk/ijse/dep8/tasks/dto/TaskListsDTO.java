package lk.ijse.dep8.tasks.dto;

import java.io.Serializable;
import java.util.List;

public class TaskListsDTO implements Serializable {
    private List<TaskDTO> item;

    public TaskListsDTO() {
    }

    public TaskListsDTO(List<TaskDTO> item) {
        this.item = item;
    }

    public List<TaskDTO> getItem() {
        return item;
    }

    public void setItem(List<TaskDTO> item) {
        this.item = item;
    }
}
