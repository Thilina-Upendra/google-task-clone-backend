package lk.ijse.dep8.tasks.service.custom;

import lk.ijse.dep8.tasks.dto.UserDTO;
import lk.ijse.dep8.tasks.service.SuperService;

import javax.servlet.http.Part;
import java.sql.Connection;

public interface UserService extends SuperService {
    boolean existUser(String userIdOrMail);

    UserDTO registerUser(Part picture, String appLocation, UserDTO user);

    void updateUser( UserDTO user, Part picture, String appLocation);

    void deleteUser(String userId, String appLocation);

    UserDTO getUser(String emailOrId);
}
