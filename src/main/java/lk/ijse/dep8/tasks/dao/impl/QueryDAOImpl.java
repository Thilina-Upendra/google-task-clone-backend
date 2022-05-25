package lk.ijse.dep8.tasks.dao.impl;

import lk.ijse.dep8.tasks.dao.QueryDAO;

import java.sql.Connection;

public class QueryDAOImpl implements QueryDAO {
    private Connection connection;
    public QueryDAOImpl(Connection connection) {
        this.connection = connection;
    }
}
