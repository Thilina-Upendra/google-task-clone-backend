package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dao.exception.DataAccessException;
import lk.ijse.dep8.tasks.entity.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDAO {
    private Connection connection;

    public TaskDAO(Connection connection) {
        this.connection = connection;
    }

    public Task saveTask(Task task) {
        try{
            if(!existsTaskById(task.getId())){
                PreparedStatement stm = connection.prepareStatement("INSERT INTO task (title, details, position, status, task_list_id) VALUE (?, ?, ?, ?, ?)");
                stm.setString(1, task.getTitle());
                stm.setString(2, task.getDetails());
                stm.setString(3, task.getPosition());
                stm.setString(4, task.getStatus().toString());
                stm.setInt(5, task.getTaskListId());
                if(stm.executeUpdate() != 1){
                    throw new SQLException("Failed to save Task");
                }
            }else{
                PreparedStatement stm = connection.prepareStatement("UPDATE task SET title=?, details=?, position=?, status=?, task_list_id=? WHERE id=?");
                stm.setString(1, task.getTitle());
                stm.setString(2, task.getDetails());
                stm.setString(3, task.getPosition());
                stm.setString(4, task.getStatus().toString());
                stm.setInt(5, task.getTaskListId());
                stm.setInt(6, task.getId());
                if(stm.executeUpdate() != 1){
                    throw new SQLException("Failed to update Task");
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return task;
    }

    public boolean existsTaskById(int taskId) {
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT id FROM task WHERE id=?");
            stm.setInt(1, taskId);
            return stm.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTaskById(int taskId) {
        try {

            if(!existsTaskById(taskId)){
                throw new DataAccessException("No Task found");
            }
            PreparedStatement stm = connection.prepareStatement("DELETE FROM task WHERE id=?");
            stm.setInt(1, taskId);
            if(stm.executeUpdate() != 1){
                throw new SQLException("Failed to delete the task");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Task> findTaskById(int taskId) {
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM task WHERE id=?");
            stm.setInt(1, taskId);
            ResultSet rst = stm.executeQuery();
            if(rst.next()){
                return  Optional.of(new Task(
                        rst.getInt("id"),
                        rst.getString("title"),
                        rst.getString("details"),
                        rst.getString("position"),
                        Task.Status.valueOf(rst.getString("status")),
                        rst.getInt("task_list_id")
                ));
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Task> findAllTask() {
        List<Task> tasks = new ArrayList<>();
        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM user");
            if(rst.next()){
                tasks.add(new Task(
                        rst.getInt("id"),
                        rst.getString("title"),
                        rst.getString("details"),
                        rst.getString("position"),
                        Task.Status.valueOf(rst.getString("status")),
                        rst.getInt("task_list_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }

    public long countTask() {

        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT COUNT(id) AS count FROM task");
            if(rst.next()){
                return rst.getLong("count");
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
