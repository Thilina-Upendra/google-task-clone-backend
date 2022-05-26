package lk.ijse.dep8.tasks.service.custom.impl;

import lk.ijse.dep8.tasks.service.custom.TaskService;

import java.sql.Connection;

public class TaskServiceImpl implements TaskService {

    private Connection connection;

    public TaskServiceImpl(Connection connection) {
        this.connection = connection;
    }
}
