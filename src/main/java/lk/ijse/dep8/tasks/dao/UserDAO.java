package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dto.UserDTO;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    public static boolean existUser(Connection connection, String email) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT id FROM user WHERE email = ?");
        stm.setString(1, email);
        return  (stm.executeQuery().next());
    }

    public static UserDTO saveUser(Connection connection,UserDTO user)throws SQLException {

        PreparedStatement stm = connection.prepareStatement("INSERT INTO user (id, email, password, full_name, profile_pic) VALUES (?,?, ?, ?, ?)");
        stm.setString(1, user.getId());
        stm.setString(2, user.getEmail());
        stm.setString(3, user.getPassword());
        stm.setString(4, user.getName());
        stm.setString(5, user.getPicture());

        if(stm.executeUpdate() != 1){
            throw new SQLException("Failed to save user");
        }
        return user;
    }
    public static void updateUser(Connection connection,UserDTO user)throws SQLException {}

    public static void deleteUser(Connection connection,String userId)throws SQLException {}

    public static UserDTO getUser(Connection connection,String userId)throws SQLException {
        return  null;
    }
}
