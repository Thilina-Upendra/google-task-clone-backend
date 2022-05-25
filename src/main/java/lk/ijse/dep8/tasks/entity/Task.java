package lk.ijse.dep8.tasks.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    private int id;
    private String title;
    private String details;
    private String position;
    private Status status;
    private int taskListId;

    public enum Status{
        complete, needAction
    }
}
