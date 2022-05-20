package lk.ijse.dep8.tasks.api;

import lk.ijse.dep8.tasks.util.HttpServlet2;
import lk.ijse.dep8.tasks.util.ResponseStatusException;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.rmi.RemoteException;

@WebServlet(name = "TaskListsServlet", urlPatterns = "/lists")
public class TaskListServlet extends HttpServlet2 {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String pattern = "/users/[A-Fa-f0-9\\-]{36}/lists/?.*";
        if(pathInfo.matches(pattern)){
            super.service(req, res);
        }else{
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")){
            throw new ResponseStatusException(415, "Invalid content type or empty content type");
        }

        if(req.getPathInfo().matches("/users/[A-Fa-f0-9\\-]{36}/lists/?")){
            throw new ResponseStatusException(405, "Invalid end point for post request");
        }
    }
}
