package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dao.exception.DataAccessException;
import lk.ijse.dep8.tasks.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    public User saveUser(User user);

    public boolean existsUserById(String userId) ;
    public boolean existsUserByEmailOrId(String emailOrId);

    public void deleteUserById(String userId);

    public Optional<User> findUserById(String userId) ;

    public Optional<User> findUserByIdOrEmail(String userIdOrMail);

    public List<User> findAllUsers() ;

    public long countUsers();
}
