package lk.ijse.dep8.tasks.service.util;

import lk.ijse.dep8.tasks.service.exception.FailedExecutionException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JNDIConnectionPool {
    private static JNDIConnectionPool jndiConnectionPool;
    private JNDIConnectionPool(){}

    public static JNDIConnectionPool getInstance(){
        return (jndiConnectionPool == null)? jndiConnectionPool= new JNDIConnectionPool():jndiConnectionPool;
    }

    public DataSource getDataSource()  {
        InitialContext initialContext = null;
        try {
            initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup("java:comp/env/dep8_tasks");
            return dataSource;
        } catch (NamingException e) {
            throw new FailedExecutionException("Failed to loop up the pool", e);
        }
    }
}
