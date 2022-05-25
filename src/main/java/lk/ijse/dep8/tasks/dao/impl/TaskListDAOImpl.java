package lk.ijse.dep8.tasks.dao.impl;

import lk.ijse.dep8.tasks.dao.TaskListDAO;
import lk.ijse.dep8.tasks.dao.exception.DataAccessException;
import lk.ijse.dep8.tasks.entity.TaskList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskListDAOImpl implements TaskListDAO {
    private Connection connection;

    public TaskListDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public TaskList save(TaskList entity) {
        TaskList taskList = (TaskList) entity;
        try{
            if(!existsById(taskList.getId())){
                PreparedStatement stm = connection.prepareStatement("INSERT INTO task_list ( name, user_id) VALUE (?, ?, ?)");
                stm.setString(1, taskList.getName());
                stm.setString(2, taskList.getUserId());
                if(stm.executeUpdate() != 1){
                    throw new SQLException("Failed to save Task List");
                }
            }else{
                PreparedStatement stm = connection.prepareStatement("UPDATE task_list SET name=?, user_id=? WHERE id=?");
                stm.setString(1, taskList.getName());
                stm.setString(2, taskList.getUserId());
                stm.setInt(3, taskList.getId());
                if(stm.executeUpdate() != 1){
                    throw new SQLException("Failed to update Task List");
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return taskList;
    }

    @Override
    public boolean existsById(Integer taskListId) {
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT id FROM task_list WHERE id=?");
            stm.setInt(1, (int)taskListId);
            return stm.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Integer taskListId) {
        try {

            if(!existsById(taskListId)){
                throw new DataAccessException("No Task List found");
            }
            PreparedStatement stm = connection.prepareStatement("DELETE FROM task_list WHERE id=?");
            stm.setInt(1, taskListId);
            if(stm.executeUpdate() != 1){
                throw new SQLException("Failed to delete the Task list");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<TaskList> findById(Integer taskListId) {
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM task_list WHERE id=?");
            stm.setInt(1, taskListId);
            ResultSet rst = stm.executeQuery();
            if(rst.next()){
                return  Optional.of(new TaskList(
                        rst.getInt("id"),
                        rst.getString("name"),
                        rst.getString("userId")
                ));
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TaskList> findAll() {
        List<TaskList> taskLists = new ArrayList<>();
        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM task_list");
            if(rst.next()){
                taskLists.add(new TaskList(
                        rst.getInt("id"),
                        rst.getString("name"),
                        rst.getString("userID")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return taskLists;
    }

    @Override
    public long count() {
        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT COUNT(id) AS count FROM task_list");
            if(rst.next()){
                return rst.getLong("count");
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
