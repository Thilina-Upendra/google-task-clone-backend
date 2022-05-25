package lk.ijse.dep8.tasks.dao;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DAOFactoryTest {


    @Test
    void testGetDAO() {
        Connection mockConnection = mock(Connection.class);
        UserDAO userDAO = DAOFactory.getInstance().getDAO(mockConnection, DAOFactory.DAOType.USER);
        TaskListDAO listDAO = DAOFactory.getInstance().getDAO(mockConnection, DAOFactory.DAOType.TASK_LIST);
        TaskDAO taskDAO = DAOFactory.getInstance().getDAO(mockConnection, DAOFactory.DAOType.TASK);
        QueryDAO queryDAO = DAOFactory.getInstance().getDAO(mockConnection, DAOFactory.DAOType.QUERY);

        assertNotNull(userDAO);
        assertNotNull(listDAO);
        assertNotNull(taskDAO);
        assertNotNull(queryDAO);
    }
}