package lk.ijse.dep8.tasks.api;

import lk.ijse.dep8.tasks.util.HttpServlet2;
import lk.ijse.dep8.tasks.util.ResponseStatusException;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.rmi.RemoteException;

@WebServlet(name = "TaskListsServlet")
public class TaskListServlet extends HttpServlet2 {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")){
            throw new ResponseStatusException(415, "Invalid content type or empty content type");
        }

        String pattern = "/[A-Fa-f0-9\\-]{36}/lists/?";

        if(req.getPathInfo().matches(pattern)){
            throw new ResponseStatusException(405, "Invalid end point for post request");
        }
    }
}
