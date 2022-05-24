package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dto.UserDTO;
import lk.ijse.dep8.tasks.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private Connection connection;

    @BeforeEach
    void setUp() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep8_tasks", "root", "mysql");
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @ParameterizedTest
    @ValueSource(strings = {"dula@gmail.com", "2aa109a4-0584-455b-9a78-456fed7b5309"})
    void existUser(String args) throws SQLException {
        boolean result = UserDAO.existUser(connection, args);
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

    @ParameterizedTest
    @ValueSource(strings = {"dula@gmail.com","abc@gmail.com", "2aa109a4-0584-455b-9a78-456fed7b5309"})
    void getUser(/*Given*/String args) throws SQLException {
        //When
        UserDTO user = UserDAO.getUser(connection, args);
        //Then
        assertNotNull(user);
    }

    @Test
    void deleteUser() throws SQLException {
        //Given
        String userId = "3a6fcb32-b03c-4a3d-80e8-7e7d0b656bd2";
        //When
        UserDAO.deleteUser(connection,userId);
        //Then
        assertThrows(AssertionError.class, ()->existUser(userId));
    }

    @Test
    void updateUser() throws SQLException {
        //Given
        UserDTO user = UserDAO.getUser(connection, "4d2432c3-cd34-4083-b4ef-52886ab98760");
        user.setName("Mooda Uppa");
        user.setPassword("Changed password");
        user.setPicture("Changed picture");

        //When
        UserDAO.updateUser(connection, user);
        //Then
        UserDTO updatedUser = UserDAO.getUser(connection, "admin@gmail.com");
//        assertEquals(user.getName(),updatedUser.getName());
//        assertEquals(user.getPassword(),updatedUser.getPassword());
//        assertEquals(user.getPicture(),updatedUser.getPicture());

        assertEquals(user, updatedUser);
    }
}