package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dao.exception.DataAccessException;
import lk.ijse.dep8.tasks.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO  extends SuperDAO{
    public boolean existsUserByEmailOrId(String emailOrId);
    public Optional<User> findUserByIdOrEmail(String userIdOrMail);

}
