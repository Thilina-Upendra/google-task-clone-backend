package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dao.exception.DataAccessException;
import lk.ijse.dep8.tasks.entity.TaskList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TaskListDAO {


    public TaskList saveTaskList(TaskList taskList) ;

    public boolean existsTaskListById(int taskListId);

    public void deleteTaskListById(int taskListId) ;

    public Optional<TaskList> findTaskListById(int taskListId);

    public List<TaskList> findAllTaskList();

    public long countTaskList();
}
