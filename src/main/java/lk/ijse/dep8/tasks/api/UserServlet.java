package lk.ijse.dep8.tasks.api;

import jdk.nashorn.internal.ir.CallNode;
import lk.ijse.dep8.tasks.listener.DBInitializer;
import lk.ijse.dep8.tasks.util.HttpServlet2;
import lk.ijse.dep8.tasks.util.ResponseStatusException;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

@MultipartConfig(location = "/tmp", maxFileSize = 10 * 1024 * 1024)
@WebServlet(name = "UserServlet", value = "/v1/users/*")
public class UserServlet extends HttpServlet2 {


    final Logger logger = Logger.getLogger(UserServlet.class.getName());

    @Resource(name = "java:comp/env/jdbc/pool")
    private volatile DataSource pool;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getContentType() == null || !request.getContentType().startsWith("multipart/form-data")){
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }


        if(request.getPathInfo() != null && !request.getPathInfo().equals("/")){
            throw new ResponseStatusException(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Invalid url");
        }

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Part picture = request.getPart("picture");

        if(name == null || !name.matches("[A-Za-z ]+")){
            throw new ResponseStatusException(HttpServletResponse.SC_BAD_REQUEST, "Invalid name or name is empty");
        }else if(email == null || !email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")){
            throw new ResponseStatusException(HttpServletResponse.SC_BAD_REQUEST, "Invalid email or email is empty");
        }else if(password == null || password.trim().isEmpty()){
            throw new ResponseStatusException(HttpServletResponse.SC_BAD_REQUEST, "Email is empty");
        }else if(picture != null && !picture.getContentType().startsWith("image")){
            throw new ResponseStatusException(HttpServletResponse.SC_BAD_REQUEST, "Invalid picture");
        }

        String application = getServletContext().getRealPath("/");
        Path path = Paths.get(application, "uploads");
        if(Files.notExists(path)){
            Files.createDirectory(path);
        }

        try(Connection connection = pool.getConnection()){

            connection.setAutoCommit(false);

            PreparedStatement stm = connection.prepareStatement("INSERT INTO user (id, emali, password, full_name) VALUES (UUID(), ?, ?, ?)");
            stm.setString(1, email);
            stm.setString(2, password);
            stm.setString(3, name);

            if(stm.executeUpdate() != 1){
                throw new SQLException("Failed to Register User");
            }


            stm = connection.prepareStatement("SELECT id FROM user WHERE email = ?");
            stm.setString(1, email);
            ResultSet rst = stm.executeQuery();
            rst.next();
            String uuid = rst.getString("id");

            Path imagePath = path.resolve(uuid);
            System.out.println(imagePath);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
