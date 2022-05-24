package lk.ijse.dep8.tasks.service;

import lk.ijse.dep8.tasks.dao.UserDAO;
import lk.ijse.dep8.tasks.dto.UserDTO;
import lk.ijse.dep8.tasks.util.ResponseStatusException;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class UserService {

    private static final Logger logger  = Logger.getLogger(UserService.class.getName());

    public static boolean existUser(Connection connection, String email) throws SQLException {
        return UserDAO.existUser(connection, email);
    }

    public static UserDTO registerUser(Connection connection, Part picture,String appLocation, UserDTO user)throws SQLException {
        try{
            connection.setAutoCommit(false);

            user.setId(UUID.randomUUID().toString());

            if (picture != null) {
                user.setPicture(user.getPicture()+ user.getId());
            }
            System.out.println(user.getPassword());
            user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
            UserDTO savedUser = UserDAO.saveUser(connection, user);

            if(picture != null){
                Path path = Paths.get(appLocation, "uploads");

                if (Files.notExists(path)) {
                    Files.createDirectory(path);
                }

                String picturePath = path.resolve(user.getId()).toAbsolutePath().toString();
                picture.write(picturePath);

                if (Files.notExists(Paths.get(picturePath))){
                    throw new ResponseStatusException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save the picture");
                }
            }
            connection.commit();
            return savedUser;
        }catch (Throwable t){
            connection.rollback();
            throw new RuntimeException(t);
        }finally {
            connection.setAutoCommit(true);
        }
    }

    public static void updateUser(UserDTO user) {
    }

    public static void deleteUser(Connection connection, String userId, String appLocation) throws SQLException {
        UserDAO.deleteUser(connection, userId);
        new Thread(() -> {
            Path imagePath = Paths.get(appLocation, "uploads",
                    userId);
            try {
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                logger.warning("Failed to delete the image: " + imagePath.toAbsolutePath());
            }
        }).start();
    }

    public static UserDTO getUser(Connection connection, String emailOrId) throws SQLException {
        return UserDAO.getUser(connection, emailOrId);
    }
}
