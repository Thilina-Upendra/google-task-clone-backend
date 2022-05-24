package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dto.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    public static boolean existUser(Connection connection, String email) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT id FROM user WHERE email = ?");
        stm.setString(1, email);
        return  (stm.executeQuery().next());
    }

    public static void saveUser(Connection connection,UserDTO user)throws SQLException {}
    public static void updateUser(Connection connection,UserDTO user)throws SQLException {}

    public static void deleteUser(Connection connection,String userId)throws SQLException {}

    public static UserDTO getUser(Connection connection,String userId)throws SQLException {
        return  null;
    }
}
