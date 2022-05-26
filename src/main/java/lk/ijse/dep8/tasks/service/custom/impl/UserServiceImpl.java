package lk.ijse.dep8.tasks.service.custom.impl;

import lk.ijse.dep8.tasks.dao.DAOFactory;
import lk.ijse.dep8.tasks.dao.custom.UserDAO;
import lk.ijse.dep8.tasks.dto.UserDTO;
import lk.ijse.dep8.tasks.entity.User;
import lk.ijse.dep8.tasks.service.custom.UserService;
import lk.ijse.dep8.tasks.service.exception.FailedExecutionException;
import lk.ijse.dep8.tasks.service.util.EntityDTOMapper;
import lk.ijse.dep8.tasks.service.util.ExecutionContext;
import lk.ijse.dep8.tasks.service.util.JNDIConnectionPool;
import lk.ijse.dep8.tasks.util.ResponseStatusException;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    private DataSource pool;

    public UserServiceImpl() {
        pool = JNDIConnectionPool.getInstance().getDataSource();
    }

    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    public boolean existUser(String email) {
        try (Connection connection = pool.getConnection();) {
            UserDAO userDAO = DAOFactory.getInstance().getDAO(connection, DAOFactory.DAOType.USER);
            return userDAO.existsUserByEmailOrId(email);
        } catch (SQLException e) {
            throw new FailedExecutionException("Failed to save the system", e);
        }
    }

    public UserDTO registerUser(Part picture, String appLocation, UserDTO user) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            connection.setAutoCommit(false);

            user.setId(UUID.randomUUID().toString());

            if (picture != null) {
                user.setPicture(user.getPicture() + user.getId());
            }

            user.setPassword(DigestUtils.sha256Hex(user.getPassword()));

            UserDAO userDAO = DAOFactory.getInstance().getDAO(connection, DAOFactory.DAOType.USER);
            /*UserDTO -> User*/
            User userEntity = EntityDTOMapper.getUser(user);

            /*After saving , User -> UserDTO*/
            user = EntityDTOMapper.getUserDTO(userDAO.save(userEntity));


            if (picture != null) {
                Path path = Paths.get(appLocation, "uploads");

                if (Files.notExists(path)) {
                    Files.createDirectory(path);
                }

                String picturePath = path.resolve(user.getId()).toAbsolutePath().toString();
                picture.write(picturePath);

                if (Files.notExists(Paths.get(picturePath))) {
                    throw new ResponseStatusException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save the picture");
                }
            }
            connection.commit();
            return user;
        } catch (Throwable t) {
            if (connection != null)
                ExecutionContext.execute(connection::rollback);
            throw new FailedExecutionException("Failed to save the user");
//            try {
//                connection.rollback();
//            } catch (SQLException e) {
//                throw new FailedExecutionException("Failed to save the user", e);
//            }

        } finally {
            if (connection != null) {
                Connection tempConnection = connection;
                /*Here tempConnection is effectively final*/
                ExecutionContext.execute(() -> tempConnection.setAutoCommit(true));
                ExecutionContext.execute(connection::close);
            }
        }
    }

    public void updateUser(UserDTO user, Part picture, String appLocation) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            connection.setAutoCommit(false);
            user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
            UserDAO userDAO = DAOFactory.getInstance().getDAO(connection, DAOFactory.DAOType.USER);
            User userEntity = userDAO.findById(user.getId()).get();

            userEntity.setPassword(user.getPassword());
            userEntity.setFullName(user.getName());
            userEntity.setProfilePic(user.getPicture());

            userDAO.save(userEntity);

            Path path = Paths.get(appLocation, "uploads");
            String picturePath = path.resolve(user.getId()).toAbsolutePath().toString();

            if (picture != null) {
                if (Files.notExists(path)) {
                    Files.createDirectory(path);
                }

                Files.deleteIfExists(Paths.get(picturePath));
                picture.write(picturePath);

                if (Files.notExists(Paths.get(picturePath))) {
                    throw new ResponseStatusException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save the picture");
                }
            } else {
                Files.deleteIfExists(Paths.get(picturePath));
            }

            connection.commit();
        } catch (Throwable e) {

            ExecutionContext.execute(connection::rollback);
            throw new FailedExecutionException("Failed to update the user");
        } finally {
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
                connection.close();
            } catch (Throwable e) {
                if(connection!=null)
                ExecutionContext.execute(connection::rollback);
                throw new FailedExecutionException("Failed to update the user");
            } finally {
                if(connection!=null){
                    Connection tempConnection = connection;
                    ExecutionContext.execute(() -> tempConnection.setAutoCommit(true));
                    ExecutionContext.execute(connection::close);
                }
            }
        }

    }

    public void deleteUser(String userId, String appLocation) {
        try(Connection connection = pool.getConnection()){
            UserDAO userDAO = DAOFactory.getInstance().getDAO(connection, DAOFactory.DAOType.USER);
            userDAO.deleteById(userId);
            new Thread(() -> {
                Path imagePath = Paths.get(appLocation, "uploads",
                        userId);
                try {
                    Files.deleteIfExists(imagePath);
                } catch (IOException e) {
                    logger.warning("Failed to delete the image: " + imagePath.toAbsolutePath());
                }
            }).start();
        } catch (SQLException e) {
            throw new FailedExecutionException("Failed to save the user", e);
        }
    }

    public UserDTO getUser(String emailOrId) {
        try (Connection connection = pool.getConnection()) {
            UserDAO userDAO = DAOFactory.getInstance().getDAO(connection, DAOFactory.DAOType.USER);
            Optional<User> userWrapper = userDAO.findUserByIdOrEmail(emailOrId);
            //User user = userWrapper.get();
            return EntityDTOMapper.getUserDTO(userWrapper.orElse(null));
//        return userWrapper.map(e->new UserDTO(e.getId(), e.getFullName(), e.getEmail(), e.getPassword(), e.getProfilePic())).orElse(null);
        } catch (SQLException e) {
            throw new FailedExecutionException("Failed to save the user", e);
        }
    }
}
