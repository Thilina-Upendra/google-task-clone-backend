package lk.ijse.dep8.tasks.dto;

import java.io.Serializable;
import java.util.List;

public class TaskListsDTO implements Serializable {
    private List<TaskListDTO> item;

    public TaskListsDTO() {
    }

    public TaskListsDTO(List<TaskListDTO> item) {
        this.item = item;
    }

    public List<TaskListDTO> getItem() {
        return item;
    }

    public void setItem(List<TaskListDTO> item) {
        this.item = item;
    }
}
