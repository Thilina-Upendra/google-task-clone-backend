package lk.ijse.dep8.tasks.entity;

public class Task {
    private int id;
    private String title;
    private String details;
    private String position;
    private Status status;
    private int taskListId;

    public Task() {
    }

    public Task(int id, String title, String details, String position, Status status, int taskListId) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.position = position;
        this.status = status;
        this.taskListId = taskListId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTaskListId() {
        return taskListId;
    }

    public void setTaskListId(int taskListId) {
        this.taskListId = taskListId;
    }

    public enum Status{
        complete, needAction
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", position='" + position + '\'' +
                ", status=" + status +
                ", taskListId=" + taskListId +
                '}';
    }
}
