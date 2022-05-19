package lk.ijse.dep8.tasks.api;

import lk.ijse.dep8.tasks.listener.DBInitializer;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "UserServlet", value = "/users/*")
public class UserServlet extends HttpServlet {

    final Logger logger = Logger.getLogger(UserServlet.class.getName());

    @Resource(name = "java:comp/env/jdbc/pool")
    private volatile DataSource pool;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getContentType() == null || !request.getContentType().equals("multipart/form-data")){
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }


    }
}
