package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dao.exception.DataAccessException;
import lk.ijse.dep8.tasks.entity.TaskList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TaskListDAO extends SuperDAO<TaskList, Integer>{

}
