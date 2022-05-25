package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dao.exception.DataAccessException;
import lk.ijse.dep8.tasks.dao.impl.UserDAOImpl;
import lk.ijse.dep8.tasks.entity.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOImplTest {

    private static Connection connection;
    private static UserDAOImpl userDAOImpl;
    @BeforeAll
    static void setUp() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep8_tasks", "root", "mysql");
            connection.setAutoCommit(false);
            userDAOImpl = new UserDAOImpl(connection);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void tearDown() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    static List<User> getDummyUsers(){
        List<User> dummies = new ArrayList<>();
        dummies.add(new User("U001", "uppa@gmail.com", "abc", "Uppa", "sape"));
        dummies.add(new User("U003", "meeya@gmail.com", "abc", "meeya", "sape"));
        dummies.add(new User("U002", "balla@gmail.com", "abc", "balla", null));
        dummies.add(new User("U004", "otuwa@gmail.com", "abc", "otuwa", null));
        dummies.add(new User("U005", "gemba@gmail.com", "abc", "gemba", "sape"));
        return dummies;
    }

    @Order(1)
    @MethodSource("getDummyUsers")
    @ParameterizedTest()
    void saveUser(User givenUser) {
        //when
        User savedUser = userDAOImpl.saveUser(givenUser);
        //then
        assertEquals(givenUser, savedUser);
    }

    @Order(2)
    @ValueSource(strings = {"U001","U002","U100"})
    @ParameterizedTest
    void existsUserById(String params) {

        //when
        boolean exist = userDAOImpl.existsUserById(params);
        //then
        if(params.equals("U100")){
            assertFalse(exist);
        }else{
            assertTrue(exist);
        }
    }

    @Order(3)
    @ValueSource(strings = {"U001","U002","U100"})
    @ParameterizedTest
    void findUserById(String params) {
        //when
        Optional<User> userWrapper = userDAOImpl.findUserById(params);
        //then
        if(params.equals("U100")){
            assertFalse(userWrapper.isPresent());
        }else{
            assertTrue(userWrapper.isPresent());
        }
    }


    @Order(4)
    @Test
    void findAllUsers() {
        //When
        List<User> allUsers = userDAOImpl.findAllUsers();

        //Then
        assertTrue(allUsers.size() >= 5);
    }


    @Order(5)
    @ValueSource(strings = {"U001","U002","U100"})
    @ParameterizedTest
    void deleteUserById(String givenUserId) {
        if(givenUserId.equals("U100")){
            assertThrows(DataAccessException.class, ()-> userDAOImpl.deleteUserById(givenUserId));
        }else{
            userDAOImpl.deleteUserById(givenUserId);
        }
    }


    @Order(6)
    @Test
    void testCountUsers() {
        assertTrue(userDAOImpl.countUsers()>=5);
    }


}