package lk.ijse.dep8.tasks.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.dep8.tasks.security.SecurityContextHolder;
import lk.ijse.dep8.tasks.util.HttpResponseErrorMessage;
import lk.ijse.dep8.tasks.util.HttpServlet2;
import lk.ijse.dep8.tasks.util.ResponseStatusException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            String pattern = "/([A-Fa-f0-9\\-]{36})/?.*";
            Matcher matcher = Pattern.compile(pattern).matcher(req.getPathInfo());
            if (matcher.find()){
                String userId = matcher.group(1);
                if (!userId.equals(SecurityContextHolder.getPrincipal().getId())) {
                    res.setContentType("application/json");
                    res.setStatus(403);
                    Jsonb jsonb = JsonbBuilder.create();
                    jsonb.toJson(new HttpResponseErrorMessage(new Date().getTime(), 403, null,
                            "Permission denied", req.getRequestURI()), res.getWriter());
            }
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
            }else{
                res.setContentType("application/json");
                res.setStatus(404);
                Jsonb jsonb = JsonbBuilder.create();
                jsonb.toJson(new HttpResponseErrorMessage(new Date().getTime(), 404, null,
                        "Invalid location", req.getRequestURI()), res.getWriter());
            }
        }
    }
}
