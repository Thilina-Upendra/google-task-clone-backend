package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.entity.User;

import java.util.Optional;

public interface UserDAO  extends CurdDAO<User, String> {
    public boolean existsUserByEmailOrId(String emailOrId);
    public Optional<User> findUserByIdOrEmail(String userIdOrMail);

}
