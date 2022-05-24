package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dto.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private Connection connection;

    @BeforeEach
    void setUp() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep8_tasks", "root", "mysql");
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    void existUser() throws SQLException {
        boolean result = UserDAO.existUser(connection, "dula@gmail.com");
        assertTrue(result);
    }

    @AfterEach
    void tearDown() {

        try {
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void saveUser() throws SQLException {
        String id = UUID.randomUUID().toString();
        UserDTO givenUser = new UserDTO(
                id,
                "Dulanga Malli",
                " dula@gmail.com",
                "dula$1225",
                null
        );
        UserDTO saveUser = UserDAO.saveUser(connection, givenUser);
        assertEquals(givenUser, saveUser);
        boolean result = UserDAO.existUser(connection, saveUser.getEmail());
        assertTrue(result);
    }
}