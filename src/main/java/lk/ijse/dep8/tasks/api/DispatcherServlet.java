package lk.ijse.dep8.tasks.api;

import lk.ijse.dep8.tasks.util.HttpServlet2;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@MultipartConfig(location = "/tmp", maxFileSize = 10 * 1024 * 1024)
@WebServlet(name = "DispatcherServlet", value = "/v1/users/*")
public class DispatcherServlet extends HttpServlet2 {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if(req.getPathInfo() == null || req.getPathInfo().equals("/")){
            /*UserServlet*/
            /*/v1/users*/
            /*/v1/users/*/

            getServletContext().getNamedDispatcher("UserServlet").forward(req, res);
        }else{

            if(req.getPathInfo().matches("/[A-Fa-f0-9\\-]{36}/?")){
                /*UserServlet*/
                /*/v1/users/{{user_id}}*/
                /*/v1/users/{{user_id}}/*/

                getServletContext().getNamedDispatcher("UserServlet").forward(req, res);
            }else if(req.getPathInfo().matches("/[A-Fa-f0-9\\-]{36}/lists(/\\d+)?/?")){
                /*UserTaskListServlet*/
                /*/v1/users/{{user_id}}/lists*/
                /*/v1/users/{{user_id}}/lists/*/
                /*/v1/users/{{user_id}}/lists/{{list_id}}*/
                /*/v1/users/{{user_id}}/lists/{{list_id}}/*/

                getServletContext().getNamedDispatcher("TaskListServlet").forward(req, res);
            }
        }
    }
}
