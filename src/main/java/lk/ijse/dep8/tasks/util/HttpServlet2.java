package lk.ijse.dep8.tasks.util;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;


import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "HttpServlet2", value = "/HttpServlet2")
public class HttpServlet2 extends HttpServlet {

    private Logger logger = Logger.getLogger(HttpServlet2.class.getName());

    protected void doPatch(HttpServletRequest request, HttpServletResponse response){

    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try{
            if(req.getMethod().equals("PATCH")){
                doPatch(req, res);
            }else{
                super.service(req, res);
            }
        }catch (Throwable t){

            if (!(t instanceof ResponseStatusException &&
                    (((ResponseStatusException)t).getStatus() >= 400 &&
                            ((ResponseStatusException)t).getStatus() < 500))){
                logger.logp(Level.SEVERE, t.getStackTrace()[0].getClassName(),
                        t.getStackTrace()[0].getMethodName(), t.getMessage(), t);
            }

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);


            res.setContentType("application/json");

            HttpResponseErrorMessage errorMsg = null;
            if (t instanceof ResponseStatusException){
                ResponseStatusException rse = (ResponseStatusException) t;
                res.setStatus(rse.getStatus());
                errorMsg = new HttpResponseErrorMessage(new Date().getTime(),
                        rse.getStatus(),
                        sw.toString(), t.getMessage(), req.getRequestURI());
            }else{
                res.setStatus(500);
                errorMsg = new HttpResponseErrorMessage(new Date().getTime(),
                        500,
                        sw.toString(), t.getMessage(), req.getRequestURI());
            }

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(errorMsg, res.getWriter());
        }
    }
}
