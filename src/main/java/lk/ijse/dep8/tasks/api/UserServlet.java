package lk.ijse.dep8.tasks.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.dep8.tasks.dto.UserDTO;
import lk.ijse.dep8.tasks.service.custom.impl.UserServiceImpl;
import lk.ijse.dep8.tasks.util.HttpServlet2;
import lk.ijse.dep8.tasks.util.ResponseStatusException;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;


@WebServlet(name = "UserServlet")
public class UserServlet extends HttpServlet2 {


    final Logger logger = Logger.getLogger(UserServlet.class.getName());

    @Resource(name = "java:comp/env/jdbc/pool")
    private volatile DataSource pool;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /*API Layer */
        if (request.getContentType() == null || !request.getContentType().startsWith("multipart/form-data")) {
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }
        /*API Layer */
        if (request.getPathInfo() != null && !request.getPathInfo().equals("/")) {
            throw new ResponseStatusException(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Invalid url");
        }

        /*API Layer */
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Part picture = request.getPart("picture");

        /*API Layer */
        if (name == null || !name.matches("[A-Za-z ]+")) {
            throw new ResponseStatusException(HttpServletResponse.SC_BAD_REQUEST, "Invalid name or name is empty");
        } else if (email == null || !email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            throw new ResponseStatusException(HttpServletResponse.SC_BAD_REQUEST, "Invalid email or email is empty");
        } else if (password == null || password.trim().isEmpty()) {
            throw new ResponseStatusException(HttpServletResponse.SC_BAD_REQUEST, "Email is empty");
        } else if (picture != null && (picture.getSize() == 0 || !picture.getContentType().startsWith("image"))) {
            throw new ResponseStatusException(HttpServletResponse.SC_BAD_REQUEST, "Invalid picture");
        }


        try (Connection connection = pool.getConnection()) {


            if (new UserServiceImpl(connection).existUser(connection, email)) {
                throw new ResponseStatusException(HttpServletResponse.SC_CONFLICT, "A user has been already registered with this email");
            }

            /*Start transaction*/
//            connection.setAutoCommit(false); //Service

//            String id = UUID.randomUUID().toString(); //Service
//            PreparedStatement stm = connection.prepareStatement("INSERT INTO user (id, email, password, full_name, profile_pic) VALUES (?,?, ?, ?, ?)");
//            stm.setString(1, id);
//            stm.setString(2, email);
//            stm.setString(3, DigestUtils.sha256Hex(password)); //Service
//            stm.setString(4, name);


//            String pictureUrl = null;
//            if(picture!=null){
//                pictureUrl = request.getScheme() + "://" + request.getServerName() + ":"
//                        + request.getServerPort() + request.getContextPath();
//                pictureUrl += "/uploads/" + id ;
//            }
//            stm.setString(5, pictureUrl);


//            if (stm.executeUpdate() != 1) {
//                throw new SQLException("Failed to Register User");
//            }


//            if(picture != null){
//                String appLocation = getServletContext().getRealPath("/");
//                Path path = Paths.get(appLocation, "uploads");
//
//                if (Files.notExists(path)) {
//                    Files.createDirectory(path);
//                }
//
//                String picturePath = path.resolve(id).toAbsolutePath().toString();
//                picture.write(picturePath);
//
//                if (Files.notExists(Paths.get(picturePath))){
//                    throw new ResponseStatusException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save the picture");
//                }
//            }
//            connection.commit();


            String pictureUrl = null;
            if (picture != null) {
                pictureUrl = request.getScheme() + "://" + request.getServerName() + ":"
                        + request.getServerPort() + request.getContextPath() + "/uploads/";
            }

            UserDTO user = new UserDTO(null, name, email, password, pictureUrl);
            user = new UserServiceImpl(connection).registerUser(connection, picture,
                    getServletContext().getRealPath("/"), user);

            /*API layer*/
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(user, response.getWriter());

        } catch (SQLException e) {
            throw new ResponseStatusException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to register the user");
        } finally {
//            try {
//                if (!connection.getAutoCommit()){
//                    connection.rollback();
//                    connection.setAutoCommit(true);
//                }
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Jsonb jsonb = JsonbBuilder.create();
        UserDTO user = getUser(req);
        resp.setContentType("application/json");
        jsonb.toJson(user, resp.getWriter());
    }

    private UserDTO getUser(HttpServletRequest req) {
        if (!(req.getPathInfo() != null && req.getPathInfo().replaceAll("/", "").length() == 36)) {
            throw new ResponseStatusException(404, "Not found");
        }

        String userId = req.getPathInfo().replaceAll("/", "");
        try (Connection connection = pool.getConnection()) {


            if (!new UserServiceImpl(connection).existUser(connection, userId)) {
                throw new ResponseStatusException(404, "Invalid user id");
            } else {
                return new UserServiceImpl(connection).getUser(connection, userId);
            }
        } catch (Throwable e) {
            throw new ResponseStatusException(500, "Failed to fetch the user info", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDTO user = getUser(req);
        try (Connection connection = pool.getConnection()) {
            String appLocation = getServletContext().getRealPath("/");
            new UserServiceImpl(connection).deleteUser(connection, user.getId(), appLocation);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
//            new Thread(() -> {
//                Path imagePath = Paths.get(getServletContext().getRealPath("/"), "uploads",
//                        user.getId());
//                try {
//                    Files.deleteIfExists(imagePath);
//                } catch (IOException e) {
//                    logger.warning("Failed to delete the image: " + imagePath.toAbsolutePath());
//                }
//            }).start();
        } catch (SQLException e) {
            throw new ResponseStatusException(500, e.getMessage(), e);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType() == null || !request.getContentType().startsWith("multipart/form-data")) {
            throw new ResponseStatusException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Invalid content type or no content type is provided");
        }

        UserDTO user = getUser(request);

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        Part picture = request.getPart("picture");

        if (name == null || !name.matches("[A-Za-z ]+")) {
            throw new ResponseStatusException(HttpServletResponse.SC_BAD_REQUEST, "Invalid name or name is empty");
        } else if (password == null || password.trim().isEmpty()) {
            throw new ResponseStatusException(HttpServletResponse.SC_BAD_REQUEST, "Password can't be empty");
        } else if (picture != null && (picture.getSize() == 0 || !picture.getContentType().startsWith("image"))) {
            throw new ResponseStatusException(HttpServletResponse.SC_BAD_REQUEST, "Invalid picture");
        }


        try (Connection connection = pool.getConnection()){

//            connection.setAutoCommit(false);
//
//            PreparedStatement stm = connection.prepareStatement("UPDATE user SET full_name=?, password=?, profile_pic=? WHERE id=?");
//            stm.setString(1, name);
//            stm.setString(2, DigestUtils.sha256Hex(password));
//
//            String pictureUrl = null;
//            if (picture != null) {
//                pictureUrl = request.getScheme() + "://" + request.getServerName() + ":"
//                        + request.getServerPort() + request.getContextPath();
//                pictureUrl += "/uploads/" + user.getId();
//            }
//            stm.setString(3, pictureUrl);
//            stm.setString(4, user.getId());
//
//            if (stm.executeUpdate() != 1) {
//                throw new SQLException("Failed to update the user");
//            }
            String pictureUrl = null;
            if (picture != null) {pictureUrl = request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath();
                pictureUrl += "/uploads/" + user.getId();
            }
            new UserServiceImpl(connection).updateUser(connection, new UserDTO(user.getId(), name, user.getEmail(), password, pictureUrl),
                    picture, getServletContext().getRealPath("/"));

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
